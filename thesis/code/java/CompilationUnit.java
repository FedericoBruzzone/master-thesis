public abstract class AbstractCompilationUnit<IDENTIFIER> implements Indexable {
  private final Scope<IDENTIFIER> scope;
  private final AbstractCompilationUnit<IDENTIFIER> compilationUnitParent;
  private final Stack<Scope<IDENTIFIER>> stack = new Stack<>();
  private final InferencingStrategy inferencingStrategy;
  private final Location location;
  private CompilationUnitTask task;
  private final String id;

  public AbstractCompilationUnit(
      Scope<IDENTIFIER> scope,
      InferencingStrategy inferencingStrategy,
      AbstractCompilationUnit<IDENTIFIER> compilationUnitParent,
      Location location,
      String id) {
    this.scope = scope;
    this.compilationUnitParent = compilationUnitParent;
    this.inferencingStrategy = inferencingStrategy;
    this.location = location;
    this.id = id == null ? "" : id;
  }

  @Override
  public Range foldingRange() {
    return Optional.ofNullable(location).map(Location::range).orElse(null);
  }

  public void bindTypeToIdentifier(IDENTIFIER typeId,
                                   SymbolTableEntry type)
      throws UnbindableEntryException {
    currentScope().bindTypeToIdentifier(typeId, type);
  }

  public SymbolTableEntry
  bindScopeOrReuse(IDENTIFIER identifier,
                   SymbolTableEntry symbolTableEntry) {
    if (!(symbolTableEntry.type() instanceof Scope<?>)) {
      throw new UnbindableEntryException("Type is not a scope",
                                         symbolTableEntry);
    }
    bindTypeToIdentifier(identifier, symbolTableEntry);
    return symbolTableEntry;
  }

  private InferenceResult typeInference(Token token, Signature signature,
                                        Scope<IDENTIFIER> scope) {
    return scope.inferFromSignature(token, signature)
        .or(()
                -> scope.getParent()
                       .map(parent -> typeInference(token, signature, parent))
                       .orElse(null));
  }

  public void
  updateTaskIfPresent(Function<CompilationUnitTask, CompilationUnitTask> fun) {
    getTask().ifPresentOrElse(e -> setTask(fun.apply(e)), () -> {
      Compiler.logger.warning(
"Something wrong could happen, your root compilation task should not be empty!");
    });
  }

  /* Other methods */
}
