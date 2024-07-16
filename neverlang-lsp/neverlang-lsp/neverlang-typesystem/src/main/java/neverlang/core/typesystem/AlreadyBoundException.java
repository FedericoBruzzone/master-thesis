package neverlang.core.typesystem;

import neverlang.core.typesystem.symbols.Location;
import org.jetbrains.annotations.Nullable;

public class AlreadyBoundException extends NeverlangTypesystemException {
  private final SymbolTableEntry currentType;

  public AlreadyBoundException(SymbolTableEntry currentType, SymbolTableEntry alreadyBoundType) {
    super("Type " + currentType + " is already bound to " + alreadyBoundType);
    this.currentType = currentType;
  }

  @Nullable
  @Override
  public Location location() {
    return currentType.location();
  }
}
