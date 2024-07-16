package neverlang.core.typesystem;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import neverlang.core.typelang.annotations.TypeDetail;
import neverlang.core.typesystem.defaults.DefaultEntryType;
import neverlang.core.typesystem.defaults.DefaultSymbolTableEntry;
import neverlang.core.typesystem.symbols.Location;
import neverlang.core.typesystem.symbols.Range;
import neverlang.core.typesystem.symbols.Token;
import neverlang.core.typesystem.symboltable.EntryKind;
import neverlang.core.typesystem.typenv.EntryType;
import neverlang.runtime.Context;

public abstract class SymbolTableEntryFactory<ID, FACTORY extends SymbolTableEntryFactory<ID, ?>> {
  private EntryKind entryKind;
  private Type type;
  private Location location;
  private Range range;
  private Token token;
  private Range foldingRange;
  private ID identifier;

  private AbstractCompilationUnit<ID> compilationUnit;

  private AtomicBoolean notified = new AtomicBoolean(false);

  private Scope<ID> scope;
  private AbstractCompilationHelper<ID, ?> compilationHelper;
  protected SymbolTableEntry refSymbolTableEntry;
  protected EntryDetails entryDetails = null;
  private SymbolTableEntry symbolTableEntry;

  protected Integer position = null;

  @TypeDetail(id = "position")
  public SymbolTableEntryFactory withPosition(Integer position) {
    this.position = position;
    return this;
  }

  public FACTORY withEntryDetails(EntryDetails entryDetails) {
    this.entryDetails = entryDetails;
    return (FACTORY) this;
  }

  public FACTORY withEntryKind(EntryKind entryKind) {
    this.entryKind = entryKind;
    return (FACTORY) this;
  }

  public Scope<ID> scope() {
    if (scope != null) {
      return scope;
    } else {
      return compilationUnit.currentScope();
    }
  }

  public FACTORY withEntryType(Type type) {
    this.type = type;
    return (FACTORY) this;
  }

  public FACTORY withEntryType(SymbolTableEntry entryType) {
    this.refSymbolTableEntry = entryType;
    return (FACTORY) this;
  }

  public FACTORY withLocation(Location location) {
    this.location = location;
    return (FACTORY) this;
  }

  public FACTORY withRange(Range range) {
    this.range = range;
    return (FACTORY) this;
  }

  public FACTORY withToken(Token token) {
    this.token = token;
    return (FACTORY) this;
  }

  public FACTORY inScope(Scope<ID> scope) {
    this.scope = scope;
    return (FACTORY) this;
  }

  public FACTORY withFoldingRange(Range foldingRange) {
    this.foldingRange = foldingRange;
    return (FACTORY) this;
  }

  public FACTORY withIdentifier(ID identifier) {
    this.identifier = identifier;
    return (FACTORY) this;
  }

  public <T extends AbstractCompilationUnit<ID>> FACTORY withCompilationUnit(T compilationUnit) {
    this.compilationUnit = compilationUnit;
    return (FACTORY) this;
  }

  public <T extends AbstractCompilationHelper<ID, ?>> FACTORY withCompilationHelper(
      T compilationHelper) {
    this.compilationHelper = compilationHelper;
    return (FACTORY) this;
  }

  public FACTORY fromSource(Context context, String src) {
    token = Optional.ofNullable(context.file()).map(e -> Token.of(e.toURI())).orElse(Token.of(src));
    Range.of(context).ifPresent(fl -> this.foldingRange = fl);
    Optional.ofNullable(token.location())
        .map(Location::range)
        .ifPresent(e -> this.foldingRange = e);
    return (FACTORY) this;
  }

  public void bind() {
    switch (entryKind()) {
      case DEFINE -> executeBinding();
      case IMPORT -> getBindingScope().importInScope(getIdentifier(), symbolTableEntry());
      case USE -> {}
    }
    notifyBinding();
  }

  public void notifyBinding() {
    if (!notified.getAndSet(true)) {
      Optional.ofNullable(getCompilationHelper()).ifPresent(e -> e.submit(this));
    }
  }

  public Scope<ID> getBindingScope() {
    return Optional.ofNullable(scope).orElse(getCompilationUnit().currentScope());
  }

  private void executeBinding() {
    if (scope != null) {
      scope.bindTypeToIdentifier(getIdentifier(), symbolTableEntry());
    } else {
      getCompilationUnit().bindTypeToIdentifier(getIdentifier(), symbolTableEntry());
    }
  }

  public <T extends SymbolTableEntry> T bindScopeOrReuse() {
    var res =
        switch (entryKind()) {
          case DEFINE -> getCompilationUnit().bindScopeOrReuse(getIdentifier(), symbolTableEntry());
          case USE, IMPORT -> null;
        };
    notifyBinding();
    return (T) res;
  }

  protected void notifyBindinbindScopeOrReuse() {
    if (!notified.getAndSet(true)) {
      Optional.ofNullable(getCompilationHelper()).ifPresent(e -> e.submit(this));
    }
  }

  public AbstractCompilationUnit<ID> getCompilationUnit() {
    return compilationUnit;
  }

  public AbstractCompilationHelper<ID, ?> getCompilationHelper() {
    return compilationHelper;
  }

  public ID getIdentifier() {
    return Optional.ofNullable(identifier).orElseGet(this::getIdentifierFromToken);
  }

  public abstract ID getIdentifierFromToken(Token token);

  public ID getIdentifierFromToken() {
    return getIdentifierFromToken(token());
  }

  public EntryType entryType() {
    Token currentToken;
    if (token() != null) {
      currentToken = token();
    } else if (getIdentifier() != null) {
      currentToken = Token.of(getIdentifier().toString());
    } else {
      throw new RuntimeException("Cannot create entry type without token or identifier");
    }

    return switch (entryKind()) {
      case USE -> new DefaultEntryType(currentToken, refSymbolTableEntry.refType());
      case DEFINE, IMPORT -> type() == null
          ? new DefaultEntryType(currentToken, refSymbolTableEntry.refType())
          : new DefaultEntryType(currentToken, type());
    };
  }

  public SymbolTableEntry refSymbolTableEntry() {
    return refSymbolTableEntry;
  }

  public SymbolTableEntry getSymbolTableEntry() {
    return new DefaultSymbolTableEntry(
        entryType(),
        entryDetails(),
        foldingRange(),
        entryKind(),
        Optional.ofNullable(getCompilationUnit())
            .map(AbstractCompilationUnit::hashCode)
            .orElse(-1));
  }

  public final SymbolTableEntry symbolTableEntry() {
    if (this.symbolTableEntry == null) {
      this.symbolTableEntry = getSymbolTableEntry();
    }
    return symbolTableEntry;
  }

  public Type type() {
    return type;
  }

  public EntryKind entryKind() {
    return entryKind;
  }

  public EntryDetails entryDetails() {
    return this.entryDetails;
  }

  public Range range() {
    var location = location();
    if (range == null && foldingRange != null && location != null) {
      return location.range().merge(foldingRange);
    } else {
      return range;
    }
  }

  public Location location() {
    if (token != null) {
      return token.location();
    } else {
      return location;
    }
  }

  public Range foldingRange() {
    return foldingRange;
  }

  public Token token() {
    if (token == null && identifier != null) {
      return new Token(identifier.toString(), location());
    } else {
      return token;
    }
  }
}
