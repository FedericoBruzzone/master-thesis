package neverlang.core.typesystem;

import java.util.stream.Stream;

public interface Importable<T, IDENTIFIER> extends Scope<IDENTIFIER> {
  void importInScope(T importable);

  Stream<T> streamImported();
}
