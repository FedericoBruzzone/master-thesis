package neverlang.compiler.lsp;

import java.util.Map;
import neverlang.compiler.plugins.translator.TranslatorPlugin;
import neverlang.runtime.DexterNeverlangModule;

public class TypeLangModule extends DexterNeverlangModule {

  @Override
  public Map<String, Class<? extends TranslatorPlugin>> getTranslators() {
    return Map.of(
        "typecheck", TypeLangTranslatorPlugin.class,
        "java", JavaTypeLangTranslatorPlugin.class);
  }
}
