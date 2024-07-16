package simplelang;

import java.util.List;

public class TypeLangTranslatorPlugin extends neverlang.core.typelang.TypeLangTranslatorPlugin {
  public TypeLangTranslatorPlugin() {
    super(new TypeLangModule(), new SimpleLangModule());
  }

  @Override
  protected String formatImports(List<String> importList) {
    importList.addAll(List.of(
        "neverlang.core.typesystem.symboltable.EntryKind",
        "neverlang.core.typesystem.symbols.*",
        "simplelang.symboltable.*",
        //                "simplelang.typesystem.types.*",
        //                "simplelang.typesystem.signatures.*",

        //                "simplelang.symboltable.CompilationUnit",
        //                "simplelang.symboltable.CompilationHelper",
        //                "simplelang.graph.*" // TODO: GRAPH
        "neverlang.core.typesystem.defaults.CompilationUnit",
        "neverlang.core.typesystem.Priority",
        "neverlang.core.lsp.defaults.types.*",
        "neverlang.core.lsp.defaults.signatures.*",
        "neverlang.core.lsp.defaults.priorities.*",
        "neverlang.core.lsp.defaults.symboltable.*"));
    return super.formatImports(importList);
  }
}
