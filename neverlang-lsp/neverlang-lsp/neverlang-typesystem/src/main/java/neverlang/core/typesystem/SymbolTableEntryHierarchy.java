package neverlang.core.typesystem;

import java.util.Optional;
import java.util.stream.Stream;
import neverlang.core.typesystem.compiler.TokenHierarchy;
import neverlang.core.typesystem.symbols.Location;

public class SymbolTableEntryHierarchy implements TokenHierarchy<SymbolTableEntry> {

  @Override
  public Optional<SymbolTableEntry> lookup(Location location) {
    return Optional.empty();
  }

  @Override
  public Stream<SymbolTableEntry> lookOnFile(String uri) {
    return null;
  }

  @Override
  public void add(SymbolTableEntry symbolTableEntry) {}
}
