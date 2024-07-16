package simplelang;

// Create a custom JavaTranslatorPlugin allow us to import something in each role
public class JavaTypeLangTranslatorPlugin
    extends neverlang.compiler.plugins.translator.JavaTranslatorPlugin {
  @Override
  protected String[] addImports() {
    return new String[] {
      "java.util.*",
      "java.util.stream.*",
      "neverlang.core.typesystem.defaults.CompilationUnit",
      "neverlang.core.lsp.defaults.types.*",
      "neverlang.core.lsp.defaults.signatures.*"
    };
  }
}
