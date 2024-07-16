package neverlang.core.lsp.defaults.types;

import neverlang.core.lsp.defaults.typenv.ParamTypeEntry;
import neverlang.core.lsp.utils.Conversions;
import neverlang.core.typelang.annotations.*;
import neverlang.core.typesystem.Signature;
import neverlang.core.typesystem.SymbolTableEntry;
import neverlang.core.typesystem.Type;
import org.eclipse.lsp4j.InlayHintKind;

@TypeAnnotation(TypeEnum.UNRESOLVED)
@TypeLangAnnotation(keyword = "unresolved", kind = TypeSystemKind.TYPE)
public record TypeUnresolved() implements Type {

  @Override
  public boolean matchSignature(Signature signature) {
    return true;
  }

  @Override
  public String id() {
    return "unknown";
  }

  @InlayHint
  public org.eclipse.lsp4j.InlayHint inlayHint(SymbolTableEntry entry) {
    if (entry.details() instanceof ParamTypeEntry) {
      var inlayHint = new org.eclipse.lsp4j.InlayHint();
      inlayHint.setLabel(entry.type().id());
      inlayHint.setPosition(Conversions.toPositionEnd(entry.selectionRange()));
      inlayHint.setKind(InlayHintKind.Parameter);
      inlayHint.setPaddingLeft(true);
      return inlayHint;
    } else {
      return null;
    }
  }
}
