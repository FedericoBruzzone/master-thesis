package neverlang.core.typesystem;

import neverlang.core.typesystem.symbols.Location;

public class UnbindableEntryException extends NeverlangTypesystemException {
  private final SymbolTableEntry symbolTableEntry;

  public UnbindableEntryException(String message, SymbolTableEntry symbolTableEntry) {
    super(message);
    this.symbolTableEntry = symbolTableEntry;
  }

  @Override
  public Location location() {
    return symbolTableEntry.location();
  }
}
