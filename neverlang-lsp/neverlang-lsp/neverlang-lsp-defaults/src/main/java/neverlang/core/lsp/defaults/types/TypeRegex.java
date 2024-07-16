package neverlang.core.lsp.defaults.types;

import java.util.regex.Pattern;
import neverlang.core.lsp.defaults.signatures.RegexSignature;
import neverlang.core.typelang.annotations.*;
import neverlang.core.typesystem.Signature;
import neverlang.core.typesystem.SymbolTableEntry;
import org.eclipse.lsp4j.InlayHint;
import org.eclipse.lsp4j.SemanticTokenTypes;

@TypeAnnotation(TypeEnum.REGEX)
@TypeLangAnnotation(keyword = "regex", kind = TypeSystemKind.TYPE)
public class TypeRegex extends TypeTerminal {

  public static final Pattern REGEX_PATTERN =
      Pattern.compile("((?:\\.|[^/\\\\]+)+)/(?:\\{(.+?)\\})?(?:\\[(.+?)\\])?");
  public static final Pattern EXTRACT_REGEX = Pattern.compile("((?:\\\\.|[^/\\\\]+)+)");
  private String regex;

  @Override
  public String id() {
    return "regex";
  }

  @Override
  public boolean matchSignature(Signature signature) {
    return signature instanceof RegexSignature;
  }

  @Override
  @neverlang.core.typelang.annotations.InlayHint
  public InlayHint inlayHint(SymbolTableEntry entry) {
    return super.inlayHint(entry);
  }

  @SemanticToken(SemanticTokenTypes.Regexp)
  public String semanticToken(SymbolTableEntry entry) {
    return SemanticTokenTypes.Regexp;
  }

  public void setRegex(String regex) {
    this.regex = regex;
  }

  public void setDescription(String group) {}

  public void setLabel(String label) {}

  public String getRegex() {
    return regex;
  }
}
