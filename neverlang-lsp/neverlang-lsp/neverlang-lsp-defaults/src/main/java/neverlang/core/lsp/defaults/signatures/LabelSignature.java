package neverlang.core.lsp.defaults.signatures;

import neverlang.core.typelang.annotations.SignatureAnnotation;
import neverlang.core.typelang.annotations.SignatureEnum;
import neverlang.core.typelang.annotations.TypeLangAnnotation;
import neverlang.core.typelang.annotations.TypeSystemKind;
import neverlang.core.typesystem.Signature;

@SignatureAnnotation(SignatureEnum.LABEL)
@TypeLangAnnotation(keyword = "label", kind = TypeSystemKind.SIGNATURE)
public class LabelSignature implements Signature {}
