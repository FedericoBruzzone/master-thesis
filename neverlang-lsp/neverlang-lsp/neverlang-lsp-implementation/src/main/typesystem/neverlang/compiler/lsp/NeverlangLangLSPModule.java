package neverlang.compiler.lsp;

import java.util.stream.Stream;
import neverlang.core.lsp.defaults.Defaults;
import neverlang.core.typelang.TypeLangGenerator;

public class NeverlangLangLSPModule extends TypeLangGenerator {
  public static final String LANGUAGE = "neverlang";

  //    public final static String TYPE_PACKAGE = TypeModule.class.getPackageName();

  public NeverlangLangLSPModule() {
    //        initPackage("neverlang.compiler.lsp.typesystem", LANGUAGE);
    //        initPackage(Constants.DEFAULT_TYPES_PACKAGE, Constants.DEFAULT_TYPES_LABEL);
    initPackage(Stream.of("neverlang.compiler.lsp"));
    initPackage(Stream.of(Defaults.DEFAULT_TYPES_PACKAGE));
  }
}
