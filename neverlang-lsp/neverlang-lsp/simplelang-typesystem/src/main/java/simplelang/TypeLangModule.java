package simplelang;

import java.util.Map;
import neverlang.compiler.plugins.translator.TranslatorPlugin;
import neverlang.runtime.DexterNeverlangModule;

public class TypeLangModule extends DexterNeverlangModule {

  @Override
  public Map<String, Class<? extends TranslatorPlugin>> getTranslators() {
    //        return Map.of("typecheck", TypeLangTranslatorPlugin.class);
    return Map.ofEntries(
        Map.entry("typecheck", TypeLangTranslatorPlugin.class),
        Map.entry("java", JavaTypeLangTranslatorPlugin.class));
  }
}
