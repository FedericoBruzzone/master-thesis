package neverlang.core.typesystem;

import java.util.*;
import java.util.function.Function;
import neverlang.core.typesystem.compiler.Compiler;
import neverlang.core.typesystem.graph.Indexable;
import neverlang.core.typesystem.symbols.Location;
import neverlang.core.typesystem.symbols.Range;
import neverlang.core.typesystem.symbols.Token;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractCompilationUnit<IDENTIFIER> implements Indexable {
  private final Scope<IDENTIFIER> scope;

  @Nullable
  private final AbstractCompilationUnit<IDENTIFIER> compilationUnitParent;

  @NotNull
  private final Stack<Scope<IDENTIFIER>> stack = new Stack<>();

  @NotNull
  private final InferencingStrategy inferencingStrategy;

  @Nullable
  private final Location location;

  @Nullable
  private CompilationUnitTask task;

  @NotNull
  private final String id;

  public AbstractCompilationUnit(
      @NotNull Scope<IDENTIFIER> scope,
      @NotNull InferencingStrategy inferencingStrategy,
      @Nullable AbstractCompilationUnit<IDENTIFIER> compilationUnitParent,
      @Nullable Location location,
      @Nullable String id) {
    this.scope = scope;
    this.compilationUnitParent = compilationUnitParent;
    this.inferencingStrategy = inferencingStrategy;
    this.location = location;
    this.id = id == null ? "" : id;
  }

  public Optional<AbstractCompilationUnit<IDENTIFIER>> getCompilationUnitParent() {
    return Optional.ofNullable(compilationUnitParent);
  }

  private void addScopeRelation(Scope<IDENTIFIER> child, Scope<IDENTIFIER> parent) {
    child.setParent(parent);
  }

  public Location location() {
    return location;
  }

  @Override
  public Range foldingRange() {
    return Optional.ofNullable(location).map(Location::range).orElse(null);
  }

  public void bindTypeToIdentifier(@Nullable IDENTIFIER typeId, @NotNull SymbolTableEntry type)
      throws UnbindableEntryException {
    currentScope().bindTypeToIdentifier(typeId, type);
  }

  public SymbolTableEntry bindScopeOrReuse(
      @Nullable IDENTIFIER identifier, @NotNull SymbolTableEntry symbolTableEntry) {
    if (!(symbolTableEntry.type() instanceof Scope<?>)) {
      throw new UnbindableEntryException("Type is not a scope", symbolTableEntry);
    }
    bindTypeToIdentifier(identifier, symbolTableEntry);
    return symbolTableEntry;
  }

  public void enterScope(@NotNull Scope<IDENTIFIER> scope) {
    addScopeRelation(scope, currentScope());
    stack.push(scope);
  }

  public void enterScope(@NotNull SymbolTableEntry symbolTableEntry) {
    if (symbolTableEntry.type() instanceof Scope<?> scope) {
      enterScope((Scope<IDENTIFIER>) scope);
    } else {
      throw new RuntimeException("Type " + symbolTableEntry.type() + " is not a scope");
    }
  }

  /**
   * @throws java.util.EmptyStackException
   */
  public void exitScope() {
    stack.pop();
    // var res = stack.pop();
    // childToParentMap.remove(res);
  }

  public <T extends Scope<IDENTIFIER>> T currentScope() {
    return (T) (stack.isEmpty() ? scope : stack.peek());
  }

  public SymbolTableEntry typeInference(Token token, Signature signature) {
    return inferencingStrategy.infer(typeInference(token, signature, currentScope()));
  }

  /**
   * Look out in scope otherwise look out inside parent of scope
   *
   * @param token
   * @param signature
   * @param scope
   * @return
   */
  private InferenceResult typeInference(Token token, Signature signature, Scope<IDENTIFIER> scope) {
    return scope.inferFromSignature(token, signature).or(() -> scope
        .getParent()
        .map(parent -> typeInference(token, signature, parent))
        .orElse(null));
  }

  //    @NotNull
  //    private  Function<Scope<IDENTIFIER>, InferenceResult> inferFromScope(Token id, Signature
  // signature) {
  //        return e -> this.typeInference(id, signature, e);
  //    }

  public Stack<Scope<IDENTIFIER>> getStack() {
    return stack;
  }

  public Optional<CompilationUnitTask> getTask() {
    return Optional.ofNullable(task);
  }

  public void setTask(CompilationUnitTask task) {
    this.task = task;
  }

  public void updateTaskIfPresent(Function<CompilationUnitTask, CompilationUnitTask> fun) {
    getTask().ifPresentOrElse(e -> setTask(fun.apply(e)), () -> {
      Compiler.logger.warning(
          "Something wrong could happen, your root compilation task should not be empty!");
    });
  }

  public Scope<IDENTIFIER> getScope() {
    return scope;
  }

  @Override
  public boolean equals(Object obj) {
    if (location == null) {
      return super.equals(obj);
    } else if (obj instanceof AbstractCompilationUnit<?> compilationUnit
        && this.scope.equals(compilationUnit.scope)) {
      return location.equals(compilationUnit.location()) && this.id.equals(compilationUnit.id);
    } else {
      return false;
    }
  }

  @Override
  public int hashCode() {
    if (location == null) {
      return super.hashCode();
    } else {
      return scope.hashCode() * location.hashCode() * (id.hashCode() == 0 ? 1 : id.hashCode());
    }
  }

  public AbstractCompilationUnit<IDENTIFIER> recursivelyGoBack() {
    return getCompilationUnitParent()
        .map(e -> {
          if (e.getScope().equals(this.getScope())) {
            return e.recursivelyGoBack();
          } else {
            return this;
          }
        })
        .orElse(this);
  }
}
