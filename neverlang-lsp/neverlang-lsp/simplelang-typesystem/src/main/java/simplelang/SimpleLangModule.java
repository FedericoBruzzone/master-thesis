package simplelang;

import java.util.stream.Stream;
import neverlang.core.lsp.defaults.Defaults;
import neverlang.core.typelang.TypeLangGenerator;

public class SimpleLangModule extends TypeLangGenerator {

  public static final String LANGUAGE = "simplelang";

  public SimpleLangModule() {
    initPackage(Stream.of("simplelang"));
    initPackage(Stream.of(Defaults.DEFAULT_TYPES_PACKAGE));
  }
}
