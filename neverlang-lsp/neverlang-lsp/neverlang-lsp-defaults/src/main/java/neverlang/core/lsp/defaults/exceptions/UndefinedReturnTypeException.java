package neverlang.core.lsp.defaults.exceptions;

import neverlang.core.typesystem.NeverlangTypesystemException;
import neverlang.core.typesystem.symbols.Location;

public class UndefinedReturnTypeException extends NeverlangTypesystemException {
  private final Location location;

  public UndefinedReturnTypeException(String s, Location location) {
    super(s);
    this.location = location;
  }

  @Override
  public Location location() {
    return location;
  }
}
