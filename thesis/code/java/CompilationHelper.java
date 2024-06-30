public abstract class AbstractCompilationHelper<ID, PRIORITY extends Comparable<PRIORITY>>
    implements Flow.Publisher<Object>, AutoCloseable, Submitter<Object> {
  @Inject
  @Named(TypeMapperModule.compilationUnit)
  public Class<? extends AbstractCompilationUnit<?>> compilationUnitClass;

  @Inject
  @Named(TypeMapperModule.lspGraph)
  private Class<? extends LSPGraph> lspGraphClass;

  @Inject
  @Named(TypeMapperModule.symbolTableEntryFactory)
  public Class<? extends SymbolTableEntryFactory<?, ?>> symbolTableEntryFactoryClass;

  @Inject
  @Named(TypeMapperModule.baseTypes)
  private Map<String, String> baseTypes;

  private SubmissionPublisher<Object> publisher = new SubmissionPublisher<>();

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

  /* Other methods */
}

