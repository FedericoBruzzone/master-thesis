package neverlang.core.typesystem;

import neverlang.core.typesystem.symbols.Location;
import neverlang.core.typesystem.symbols.Token;

public class TypeMismatchException extends NeverlangTypesystemException {

  private final Token token;

  public TypeMismatchException(SymbolTableEntry assignee, SymbolTableEntry assigned, Token token) {
    super("Cannot assign " + assignee.type().id() + " to " + assigned.type().id());
    this.token = token;
  }

  @Override
  public Location location() {
    return token.location();
  }
}
