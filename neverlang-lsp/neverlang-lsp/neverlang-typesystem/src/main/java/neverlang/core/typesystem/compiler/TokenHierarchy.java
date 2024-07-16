package neverlang.core.typesystem.compiler;

import java.util.Optional;
import java.util.stream.Stream;
import neverlang.core.typesystem.symbols.Location;

public interface TokenHierarchy<T> {
  Optional<T> lookup(Location location);

  Stream<T> lookOnFile(String uri);

  void add(T t);
}
