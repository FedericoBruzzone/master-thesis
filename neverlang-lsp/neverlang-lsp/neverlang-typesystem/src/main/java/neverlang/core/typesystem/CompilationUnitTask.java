package neverlang.core.typesystem;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import neverlang.core.typesystem.compiler.Compiler;
import neverlang.core.typesystem.symbols.Location;
import neverlang.runtime.ASTNode;
import neverlang.runtime.Context;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record CompilationUnitTask<PRIORITY extends Comparable<PRIORITY>>(
    @NotNull AtomicReference<CompilationUnitToken> token,
    @NotNull Consumer<Context> consumer,
    @NotNull Context context,
    @NotNull Class<? extends AbstractCompilationHelper<?, ?>> aClass,
    @NotNull PRIORITY priority)
    implements Comparable<CompilationUnitTask<PRIORITY>> {
  public CompilationUnitTask(
      Consumer<Context> consumer,
      Context context,
      Class<? extends AbstractCompilationHelper<?, ?>> aClass,
      PRIORITY priority) {
    this(new AtomicReference<>(), consumer, context, aClass, priority);
  }

  /**
   * Method consumer will be executed before the previous registered consumer
   *
   * @param consumer
   * @return
   */
  public CompilationUnitTask<PRIORITY> compose(Consumer<Context> consumer) {
    return new CompilationUnitTask<>(
        token(), consumer.andThen(consumer()), context, aClass(), priority());
  }

  public CompilationUnitTask<PRIORITY> emptyTask() {
    return new CompilationUnitTask<>(ctx -> {}, context(), aClass(), priority());
  }

  public void run(CompilationContext compilationContext) {

    var oldToken = token.getAndSet(compilationContext.token());
    if (oldToken == null) {
      Compiler.logger.fine("Compilation unit run");
      consumer.accept(context);
    } else if (oldToken.equals(compilationContext.token())) {
      Compiler.logger.fine("Already ran");
    } else if (compilationContext.incremental()) {
      Compiler.logger.fine("Incremental run");
      var helper =
          context.root().<AbstractCompilationHelper<?, ?>>getValue("$" + aClass.getSimpleName());
      compilationContext
          .incrementalCompilationHelper()
          .ifPresent(e ->
              e.beforeCompilationUnitRecompilation(helper, context, compilationContext.token()));
      consumer.accept(context);
    }
  }

  @Override
  public int compareTo(@NotNull CompilationUnitTask<PRIORITY> o) {
    return this.priority().compareTo(o.priority());
  }

  public static class Builder<ID, PRIORITY extends Comparable<PRIORITY>> {
    private final Class<? extends AbstractCompilationUnit<ID>> aClass;
    private final Class<? extends AbstractCompilationHelper<ID, PRIORITY>> helperClass;
    private ASTNode[] astNodes;
    private Context context;
    //        private Scope<ID> scope;
    private AbstractCompilationUnit<ID> parentCompilationUnit;
    private AbstractCompilationUnit<ID> compilationUnit;

    private PRIORITY priority;
    private InferencingStrategy inferencingStrategy;

    private CompilationUnitExecutor executor;
    private SymbolTableEntry symbolTableEntry;

    @Nullable
    private Runnable callback;

    @Nullable
    private String id;

    Builder(
        Class<? extends AbstractCompilationUnit<ID>> aClass,
        Class<? extends AbstractCompilationHelper<ID, PRIORITY>> helperClass) {
      this.aClass = aClass;
      this.helperClass = helperClass;
    }

    public Builder<ID, PRIORITY> withInferencingStrategy(InferencingStrategy inferencingStrategy) {
      this.inferencingStrategy = inferencingStrategy;
      return this;
    }

    public Builder<ID, PRIORITY> withAstNodes(ASTNode... astNodes) {
      this.astNodes = astNodes;
      return this;
    }

    public Builder<ID, PRIORITY> withContext(Context context) {
      this.context = context;
      return this;
    }

    public Builder<ID, PRIORITY> withPriority(PRIORITY priority) {
      this.priority = priority;
      return this;
    }

    public Builder<ID, PRIORITY> withCallback(Runnable callback) {
      this.callback = callback;
      return this;
    }

    public Builder<ID, PRIORITY> withParentCompilationUnit(
        AbstractCompilationUnit<ID> parentCompilationUnit) {
      this.parentCompilationUnit = parentCompilationUnit;
      return this;
    }

    public Builder<ID, PRIORITY> insideScope(SymbolTableEntry symbolTableEntry) {
      this.symbolTableEntry = symbolTableEntry;
      return this;
    }

    public Builder<ID, PRIORITY> withExecutor(CompilationUnitExecutor executor) {
      this.executor = executor;
      return this;
    }

    public Builder<ID, PRIORITY> withCompilationUnit(AbstractCompilationUnit<ID> compilationUnit) {
      this.compilationUnit = compilationUnit;
      return this;
    }

    public Builder<ID, PRIORITY> withId(String id) {
      this.id = id;
      return this;
    }

    public void registerIndependentTask() {
      assert executor != null;
      assert compilationUnit.getTask().isPresent();
      executor.registerIndependent(compilationUnit);
    }

    public void registerTask() {
      assert executor != null;
      assert compilationUnit.getTask().isPresent();
      compilationUnit
          .getCompilationUnitParent()
          .ifPresentOrElse(
              parent -> executor.registerDependency(parent, compilationUnit),
              () -> executor.registerIndependent(compilationUnit));
    }

    public void createAndRegisterTask() {
      assert executor != null;
      var compilationUnit = getCompilationUnit();
      compilationUnit.setTask(getTask());
      var scope = getScope();
      scope.ifPresent(
          s -> compilationUnit.getCompilationUnitParent().ifPresent(e -> e.enterScope(s)));
      registerTask();
      scope
          .flatMap(s -> compilationUnit.getCompilationUnitParent())
          .ifPresent(AbstractCompilationUnit::exitScope);
    }

    public Optional<Scope<ID>> getScope() {
      return Optional.ofNullable(symbolTableEntry)
          .map(e -> e.type() instanceof Scope ? e.type() : null);
    }

    private CompilationUnitTask<PRIORITY> getTask() {
      var derivedContext = deriveNeverlangRuntimeContext();
      if (astNodes != null) {
        return new CompilationUnitTask<>(
            $ctx -> {
              Arrays.stream(astNodes).forEachOrdered($ctx::eval);
              Optional.ofNullable(callback).ifPresent(Runnable::run);
            },
            derivedContext,
            helperClass,
            priority);
      } else {
        return null;
      }
    }

    private AbstractCompilationUnit<ID> getCompilationUnit() {
      if (compilationUnit == null) {
        var currentCompilationUnit = getCurrentCompilationUnit(context);
        var scope = getScope().orElseGet(currentCompilationUnit::currentScope);
        this.compilationUnit = newCompilationUnit(scope, currentCompilationUnit);
      }
      return compilationUnit;
    }

    public AbstractCompilationUnit<ID> getCurrentCompilationUnit(Context context) {
      return Optional.ofNullable(parentCompilationUnit)
          .orElseGet(() -> context.root().getValue(getCompilationUnitKey()));
    }

    public AbstractCompilationUnit<ID> newCompilationUnit(
        @NotNull Scope<ID> scope, @Nullable AbstractCompilationUnit<ID> compilationUnit) {
      try {
        return aClass
            .getConstructor(
                Scope.class,
                InferencingStrategy.class,
                AbstractCompilationUnit.class,
                Location.class,
                String.class)
            .newInstance(
                scope,
                inferencingStrategy,
                compilationUnit,
                Location.of(context, astNodes).orElse(null),
                id);
      } catch (NoSuchMethodException
          | InstantiationException
          | IllegalAccessException
          | InvocationTargetException e) {
        throw new RuntimeException(e);
      }
    }

    public Context deriveNeverlangRuntimeContext() {
      var compilationUnit = getCompilationUnit();

      var currentNode = context.node();
      var map = new HashMap<>(context.root().getValues());
      map.put(getCompilationUnitKey(), compilationUnit);
      currentNode.setValues(map);
      var derivedContext = context.derive(currentNode);
      derivedContext.setRoot(currentNode);
      return derivedContext;
    }

    private String getCompilationUnitKey() {
      return "$" + aClass.getSimpleName();
    }
  }
}
