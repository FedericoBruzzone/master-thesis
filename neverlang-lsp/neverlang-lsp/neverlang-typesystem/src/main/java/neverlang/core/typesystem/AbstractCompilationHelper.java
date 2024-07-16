package neverlang.core.typesystem;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import neverlang.core.typelang.TypeMapperModule;
import neverlang.core.typesystem.compiler.Compiler;
import neverlang.core.typesystem.defaults.DexecutorCompilationUnitExecutor;
import neverlang.core.typesystem.defaults.FindFirstInferencingStrategy;
import neverlang.core.typesystem.graph.LSPGraph;
import neverlang.core.typesystem.symbols.Location;
import neverlang.runtime.ASTNode;
import neverlang.runtime.Context;
import neverlang.runtime.Language;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractCompilationHelper<ID, PRIORITY extends Comparable<PRIORITY>>
    implements Flow.Publisher<Object>, AutoCloseable, Submitter<Object> {
  @Inject
  @Named(TypeMapperModule.compilationUnit)
  @NotNull
  public Class<? extends AbstractCompilationUnit<?>> compilationUnitClass;

  @Inject
  @Named(TypeMapperModule.lspGraph)
  @Nullable
  private Class<? extends LSPGraph> lspGraphClass;

  @Inject
  @Named(TypeMapperModule.symbolTableEntryFactory)
  public Class<? extends SymbolTableEntryFactory<?, ?>> symbolTableEntryFactoryClass;

  @Inject
  @Named(TypeMapperModule.baseTypes)
  private Map<String, String> baseTypes;

  private SubmissionPublisher<Object> publisher = new SubmissionPublisher<>();

  /** By default, the inferencing strategy is to find the first match. */
  private InferencingStrategy inferencingStrategy = new FindFirstInferencingStrategy();

  private Scope<ID> root;
  private AbstractCompilationUnit<ID> rootCompilationUnit;
  private CompilationUnitExecutor compilationUnitExecutor;
  private CompilationContext context;
  private SymbolTableEntryFactory<?, ?> symbolTableEntryFactory;

  private AtomicBoolean rootIsInitialized = new AtomicBoolean(false);
  private AtomicInteger incrementalRuns = new AtomicInteger(0);
  private AtomicReference<CompilationUnitToken> lastToken = new AtomicReference<>();

  private AtomicReference<LSPGraph> graph = new AtomicReference<>(null);

  public void cleanGraph() {
    graph = new AtomicReference<>(null);
  }

  private void bindBaseTypes() {
    if (symbolTableEntryFactoryClass == null) {
      throw new RuntimeException("Symbol table entry factory class not set");
    }
    if (baseTypes == null) {
      throw new RuntimeException("Base types not set");
    }

    baseTypes.forEach((k, v) -> {
      try {
        Class<? extends BaseType> cls = Class.forName(v).asSubclass(BaseType.class);
        BaseType baseType = cls.getConstructor().newInstance();
        baseType
            .withScope(getRoot())
            .withCompilationHelper(this)
            .withSymbolTableEntryFactory(
                symbolTableEntryFactoryClass.getConstructor().newInstance())
            .bind();
      } catch (ClassNotFoundException
          | NoSuchMethodException
          | InstantiationException
          | IllegalAccessException
          | InvocationTargetException e) {
        throw new RuntimeException(e);
      }
    });
  }

  /** This method is called when the root is initialized for the first time. */
  public void beforeAll() {}

  /**
   * This method is called when for each root instantiaton that mean all the times an AST is
   * executed.
   */
  public void beforeEach() {}

  protected abstract Scope<ID> generateRootType();

  public void setInferencingStrategy(InferencingStrategy inferencingStrategy) {
    this.inferencingStrategy = inferencingStrategy;
  }

  public static <T extends AbstractCompilationHelper<?, ?>> Optional<T> getFromASTNode(
      Language language, Class<T> compilationUnitHolder) {
    language.loadLanguage();
    return language.getEndemicSlices().values().stream()
        .flatMap(e -> e.getDeclaredStaticInstances().values().stream())
        .filter(e -> e.getClass().isAssignableFrom(compilationUnitHolder))
        .map(e -> (T) e)
        .findFirst();
  }

  public void subscribe(Flow.Subscriber<Object> subscriber) {
    publisher.subscribe(subscriber);
  }

  public static <T> T getFromASTNode(ASTNode astNode, Class<T> tClass) {
    return astNode.getValue(getClassKey(tClass));
  }

  public static String getClassKey(Class<?> tClass) {
    return "$" + tClass.getSimpleName();
  }

  public void setLspGraph(LSPGraph lspGraph) {
    this.graph.set(lspGraph);
  }

  private void createLspGraph(@NotNull Context $ctx) {
    if (this.graph.get() == null) {
      Optional.ofNullable(lspGraphClass).ifPresent(cls -> {
        // this.graph.set();
        setLspGraph($ctx.root().<LSPGraph>getValue(getClassKey(cls)));
        subscribe(this.graph.get().getSubscriber());
      });
    }
  }

  public @NotNull CompilationUnitExecutor getExecutor() {
    if (compilationUnitExecutor == null) {
      compilationUnitExecutor = initExecutor();
    }
    return compilationUnitExecutor;
  }

  /**
   * Override this method to provide a custom executor. Default value is {@link
   * DexecutorCompilationUnitExecutor}
   *
   * @return
   */
  public CompilationUnitExecutor initExecutor() {
    return new DexecutorCompilationUnitExecutor(this);
  }

  public void eval() {
    if (context == null) {
      throw new RuntimeException("Compilation context not set");
    }
    getExecutor().execute(context);
  }

  public Scope<ID> getRoot() {
    return root;
  }

  @Nullable
  public LSPGraph getGraph() {
    return graph.get();
  }

  public <T extends AbstractCompilationUnit<ID>> T getRootCompilationUnit() {
    return (T) rootCompilationUnit;
  }

  public void initRoot(@NotNull Context $ctx, PRIORITY priority) {
    var $n = $ctx.node();
    var symbolIdentifier = $n.getSymbol().getSymbolIdentifier();
    if (!symbolIdentifier.equals("Program")) {
      throw new RuntimeException("Init root should be attached to Program non terminal symbol");
    }
    if (!rootIsInitialized.getAndSet(true)) {
      if (incrementalRuns.getAndIncrement() == 0) {
        // SHOULD REINIT ROOT
        setRoot(generateRootType(), Location.of($ctx, $n).orElse(null));
        $ctx.root().setValue(getClassKey(compilationUnitClass), getRootCompilationUnit());
        this.createLspGraph($ctx);
        var taskBuilder = getTaskBuilder()
            .withContext($ctx)
            .withPriority(priority)
            .withAstNodes($n.getChildren())
            .withCompilationUnit(getRootCompilationUnit());
        // TODO:CALLABCK
        //            Arrays.stream(getRoot().getClass().getMethods())
        //                    .filter(m -> m.isAnnotationPresent(Callback.class))
        //                    .findFirst()
        //                    .ifPresent(m -> {
        //                        var root = getRoot();
        //                        m.invoke()
        //                    });
        taskBuilder.createAndRegisterTask();

        bindBaseTypes();
        beforeAll();
      } else {
        getTaskBuilder()
            .withContext($ctx)
            .withPriority(priority)
            .withAstNodes($n.getChildren())
            .withCompilationUnit(getRootCompilationUnit())
            .createAndRegisterTask();
      }

    } else {
      $ctx.root().setValue(getClassKey(compilationUnitClass), getRootCompilationUnit());
      getRootCompilationUnit().updateTaskIfPresent(e -> e.compose(ctx -> $ctx.evalChildren($n)));
      beforeEach();
    }
  }

  protected void setRoot(Scope<ID> root, @Nullable Location location) {
    this.root = root;
    this.rootCompilationUnit = generateCompilationUnit(root, location);
  }

  public AbstractCompilationUnit<ID> generateCompilationUnit(Scope<ID> scope) {
    return generateCompilationUnit(scope, inferencingStrategy, null);
  }

  public AbstractCompilationUnit<ID> generateCompilationUnit(
      Scope<ID> scope, @Nullable Location location) {
    return generateCompilationUnit(scope, inferencingStrategy, location);
  }

  // TODO: this is a duplicate in CompilationUnitTask
  private AbstractCompilationUnit<ID> generateCompilationUnit(
      Scope<ID> scope, InferencingStrategy inferencingStrategy, @Nullable Location location) {
    try {
      return (AbstractCompilationUnit<ID>) compilationUnitClass
          .getConstructor(
              Scope.class,
              InferencingStrategy.class,
              AbstractCompilationUnit.class,
              Location.class,
              String.class)
          .newInstance(scope, inferencingStrategy, null, location, null);
    } catch (InstantiationException
        | IllegalAccessException
        | InvocationTargetException
        | NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
  }

  public CompilationUnitTask.Builder<ID, PRIORITY> getTaskBuilder() {
    return new CompilationUnitTask.Builder<>(
            (Class<? extends AbstractCompilationUnit<ID>>) compilationUnitClass,
            (Class<? extends AbstractCompilationHelper<ID, PRIORITY>>) getClass())
        .withExecutor(this.getExecutor())
        .withInferencingStrategy(inferencingStrategy);
  }

  public CompilationContext getContext() {
    return context;
  }

  public <T> T getCompilationUnit(ASTNode astNode) {
    return (T) getFromASTNode(astNode, compilationUnitClass);
  }

  public void submit(Object item) {
    publisher.submit(item);
  }

  @Override
  public void close() {
    publisher.close();
  }

  /**
   * This method should be called before each compilation
   *
   * @param ctx
   */
  public void setup(CompilationContext ctx) {
    this.context = ctx;
    rootIsInitialized.set(false);
    if (getRootCompilationUnit() == null) {
      // This mean this is the first time that the compiling has been done
    } else if (ctx.incremental()) {
      getRootCompilationUnit().updateTaskIfPresent(CompilationUnitTask::emptyTask);
      Compiler.logger.info("Update task for incremental compilation");
    } else {
      Compiler.logger.info("Resetting root");
    }
  }

  public void clear(@NotNull ASTNode astNode, @NotNull CompilationUnitToken token) {
    var oldToken = lastToken.getAndSet(token);
    if (oldToken == null || !oldToken.equals(token)) {
      AbstractCompilationUnit<ID> cu = getCompilationUnit(astNode);
      cu.currentScope().removeEntriesWithOwnerHashcode(cu.hashCode());
      Compiler.logger.info("Clearing typing environment");
    }
  }
}
