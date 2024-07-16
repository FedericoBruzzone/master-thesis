package neverlang.core.typesystem.defaults;

import com.github.dexecutor.core.DefaultDexecutor;
import com.github.dexecutor.core.DexecutorConfig;
import com.github.dexecutor.core.ExecutionConfig;
import com.github.dexecutor.core.ExecutionListener;
import com.github.dexecutor.core.support.ThreadPoolUtil;
import com.github.dexecutor.core.task.Task;
import com.github.dexecutor.core.task.TaskProvider;
import java.util.PriorityQueue;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import neverlang.core.typesystem.*;
import neverlang.core.typesystem.compiler.Compiler;
import org.jetbrains.annotations.NotNull;

public class DexecutorCompilationUnitExecutor implements CompilationUnitExecutor {

  private final ExecutionListener<AbstractCompilationUnit<?>, Void> executionListener;

  @NotNull
  private DefaultDexecutor<AbstractCompilationUnit<?>, Void> executor;

  @NotNull
  private PriorityQueue<PriorityEntry<?>> compilationUnitToExecute = new PriorityQueue<>();

  @NotNull
  private ExecutorService executorService;

  private CompilationUnitTaskProvider provider;

  private ExecutorService newExecutor() {
    return Executors.newFixedThreadPool(ThreadPoolUtil.ioIntesivePoolSize());
  }

  public DexecutorCompilationUnitExecutor(AbstractCompilationHelper<?, ?> compilationHelper) {
    this.executionListener = new ExecutionListener<>() {
      @Override
      public void onSuccess(Task<AbstractCompilationUnit<?>, Void> task) {}

      @Override
      public void onError(Task<AbstractCompilationUnit<?>, Void> task, Exception exception) {
        var logRecord = new LogRecord(Level.SEVERE, exception.getMessage());
        logRecord.setThrown(exception);
        compilationHelper.submit(logRecord);
      }
    };
    initExecutor();
  }

  private void initExecutor() {
    this.executorService = newExecutor();
    this.provider = new CompilationUnitTaskProvider();
    var config = new DexecutorConfig<>(executorService, provider, executionListener);
    this.executor = new DefaultDexecutor<>(config);
  }

  @Override
  public synchronized void registerDependency(
      AbstractCompilationUnit<?> dependee, AbstractCompilationUnit<?> dependent) {
    registerLazyDependency(dependent, () -> executor.addDependency(dependee, dependent));
  }

  @Override
  public synchronized void registerIndependent(AbstractCompilationUnit<?> independent) {
    registerLazyDependency(independent, () -> executor.addIndependent(independent));
  }

  private void registerLazyDependency(AbstractCompilationUnit<?> compilationUnit, Runnable task) {
    compilationUnit
        .getTask()
        .ifPresentOrElse(
            e -> compilationUnitToExecute.add(new PriorityEntry<>(e, task)),
            () -> Compiler.logger.warning(
                "This should not happen, check you are setting the task to the compilation unit before registering deps"));
  }

  private static void executeDAG(DefaultDexecutor<?, ?> executor, ExecutorService executorService) {
    try {
      executor.execute(ExecutionConfig.TERMINATING);
    } finally {
      executorService.shutdownNow();
      try {
        executorService.awaitTermination(2, TimeUnit.HOURS);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
  }

  @Override
  public void execute(CompilationContext compilationContext) {
    while (!compilationUnitToExecute.isEmpty()) {
      initLazyDependencies();
      provider.setCompilationContext(compilationContext);
      var oldExecutorService = this.executorService;
      var oldExecutor = this.executor;
      // REINIT ALL
      initExecutor();
      executeDAG(oldExecutor, oldExecutorService);
    }
  }

  private void initLazyDependencies() {
    PriorityEntry<?> old = null;
    PriorityEntry<?> current;
    while (!compilationUnitToExecute.isEmpty()) {
      current = compilationUnitToExecute.peek();
      if (old != null && current != null && old.compareTo(current) != 0) {
        break;
      }
      old = compilationUnitToExecute.poll();
      old.run();
    }
  }

  private static class CompilationUnitTaskProvider
      implements TaskProvider<AbstractCompilationUnit<?>, Void> {

    private CompilationContext compilationContext;

    public Task<AbstractCompilationUnit<?>, Void> provideTask(
        final AbstractCompilationUnit<?> compilationUnit) {

      return new Task<>() {

        public Void execute() {
          if (compilationContext == null) {
            throw new RuntimeException("Compilation context is not set");
          }
          compilationUnit.getTask().ifPresent(e -> e.run(compilationContext));
          return null;
        }
      };
    }

    public void setCompilationContext(CompilationContext compilationContext) {
      this.compilationContext = compilationContext;
    }
  }

  private record PriorityEntry<P extends Comparable<P>>(
      CompilationUnitTask<P> compilationUnitTask, Runnable lazyInit)
      implements Comparable<PriorityEntry<P>>, Runnable {

    @Override
    public void run() {
      lazyInit.run();
    }

    @Override
    public int compareTo(@NotNull PriorityEntry o) {
      return this.compilationUnitTask().compareTo(o.compilationUnitTask());
    }
  }
}
