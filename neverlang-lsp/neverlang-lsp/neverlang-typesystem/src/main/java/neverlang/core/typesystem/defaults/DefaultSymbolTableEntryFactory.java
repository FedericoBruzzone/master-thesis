package neverlang.core.typesystem.defaults;

import java.util.Optional;
import neverlang.core.typesystem.SymbolTableEntryFactory;
import neverlang.core.typesystem.symbols.Token;

public class DefaultSymbolTableEntryFactory
    extends SymbolTableEntryFactory<String, DefaultSymbolTableEntryFactory> {
  @Override
  public String getIdentifierFromToken(Token token) {
    return Optional.ofNullable(token).map(Token::text).orElse(null);
  }
}
