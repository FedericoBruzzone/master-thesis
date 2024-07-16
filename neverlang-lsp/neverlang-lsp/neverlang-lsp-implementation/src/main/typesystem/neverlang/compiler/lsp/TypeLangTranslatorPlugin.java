package neverlang.compiler.lsp;

// import neverlang.compiler.lsp.typesystem.signature.ModuleSignature;
// import neverlang.compiler.lsp.typesystem.types.TypeModule;
// import neverlang.core.typesystem.defaults.Constants;

import java.util.List;

public class TypeLangTranslatorPlugin extends neverlang.core.typelang.TypeLangTranslatorPlugin {
  public TypeLangTranslatorPlugin() {
    super(new TypeLangModule(), new NeverlangLangLSPModule());
    //        importAllPackages(Constants.class, TypeModule.class, CompilationHelper.class,
    // ModuleSignature.class);
  }

  @Override
  protected String formatImports(List<String> importList) {
    importList.addAll(List.of(
        "neverlang.core.typesystem.symboltable.EntryKind",
        "neverlang.core.typesystem.symbols.*",
        //                "simplelang.symboltable.*", //TODO

        "neverlang.core.typesystem.defaults.CompilationUnit",
        "neverlang.core.typesystem.Priority",
        "neverlang.core.lsp.defaults.types.*",
        "neverlang.core.lsp.defaults.signatures.*",
        "neverlang.core.lsp.defaults.priorities.*",
        "neverlang.core.lsp.defaults.symboltable.*"));
    return super.formatImports(importList);
  }
}
