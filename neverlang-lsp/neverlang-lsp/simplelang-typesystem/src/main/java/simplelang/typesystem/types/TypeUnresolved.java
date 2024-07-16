package simplelang.typesystem.types;

// @TypeLangAnnotation(
//        keyword = "unresolved",
//        kind = TypeSystemKind.TYPE
// )
// public record TypeUnresolved() implements Type {
//
//    @Override
//    public boolean matchSignature(Signature signature) {
//       return true;
//    }
//
//    @Override
//    public String id() {
//        return "unknown";
//    }
//
//    @InlayHint
//    public org.eclipse.lsp4j.InlayHint inlayHint(SymbolTableEntry entry) {
//        if(entry.details() instanceof ParamTypeEntry){
//            var inlayHint = new org.eclipse.lsp4j.InlayHint();
//            inlayHint.setLabel(entry.type().id());
//            inlayHint.setPosition(Conversions.toPositionEnd(entry.selectionRange()));
//            inlayHint.setKind(InlayHintKind.Parameter);
//            inlayHint.setPaddingLeft(true);
//            return inlayHint;
//        } else {
//            return null;
//        }
//    }
// }
