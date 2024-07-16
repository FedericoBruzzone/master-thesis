package neverlang.core.typelang;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import neverlang.compiler.plugins.translator.JavaTranslatorPlugin;
import neverlang.core.typesystem.symbols.Token;
import neverlang.core.typesystem.symboltable.EntryKind;
import neverlang.runtime.ASTNode;
import neverlang.runtime.DexterNeverlangModule;
import org.jetbrains.annotations.Nullable;

public abstract class TypeLangTranslatorPlugin extends JavaTranslatorPlugin {
  private final TypeMapperModule abstractLangModule;
  private final DexterNeverlangModule dexterNeverlangModule;
  private final List<String> imports = new ArrayList<>();

  public TypeLangTranslatorPlugin(
      DexterNeverlangModule dexterNeverlangModule, TypeMapperModule abstractLangModule) {
    this(dexterNeverlangModule, abstractLangModule, null);
  }

  public TypeLangTranslatorPlugin(
      DexterNeverlangModule dexterNeverlangModule,
      TypeMapperModule abstractLangModule,
      @Nullable List<String> packageImports) {
    this.abstractLangModule = abstractLangModule;
    this.dexterNeverlangModule = dexterNeverlangModule;
    Optional.ofNullable(packageImports).ifPresent(imports::addAll);
    importAllPackages(EntryKind.class, Token.class);
  }

  public void importAllPackages(Class<?>... classes) {
    Arrays.stream(classes)
        .map(Class::getPackageName)
        .distinct()
        .forEach(e -> imports.add(e + ".*"));
  }

  @Override
  protected String formatImports(List<String> importList) {
    return super.formatImports(
        Stream.concat(imports.stream(), importList.stream()).toList());
  }

  @Override
  public String formatSource(String src) {
    StringBuilder sb = new StringBuilder();
    if (shouldEvalChildren()) {
      sb.append(MessageFormat.format(evalChildren, getPos()));
      sb.append("\n");
    }
    ASTNode astNode = abstractLangModule.getLanguage(dexterNeverlangModule).exec(src);
    sb.append(astNode.<String>getValue("Text"));
    return sb.toString();
  }
}
