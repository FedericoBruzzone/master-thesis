package simplelang.typesystem.types;

// import simplelang.typesystem.signatures.IdentifierSignature;
// import simplelang.typesystem.typenv.ParamTypeEntry;

// @TypeLangAnnotation(
//        kind = TypeSystemKind.TYPE
// )
// public class TypePrimitive implements Type {
//
//    private static final Map<String, TypePrimitive> instances = new HashMap<>();
//
//    private interface Nameable {
//        String name();
//    }
//
//    public enum Names implements Nameable {
//        INT,
//        CHAR,
//        STRING,
//        BOOLEAN
//    }
//
//    public static TypePrimitive of(Nameable id) {
//        instances.putIfAbsent(id.name(), new TypePrimitive(id.name()));
//        return instances.get(id.name());
//    }
//
//    private String id;
//
//    public TypePrimitive(String id) {
//        this.id = id;
//    }
//
//    @Override
//    public String id() {
//        return this.id;
//    }
//
//    @Override
//    public boolean matchSignature(Signature signature) {
//        return signature instanceof IdentifierSignature;
//    }
//
//    @Override
//    public boolean isAssignableFrom(Type other, Variance variance) {
//        return switch (variance) {
//            case INVARIANT -> other.equals(this);
//            //TODO: CHECK CASE WHEN OTHER IS A SUBTYPE OF THIS
//            case CONTROVARIANT -> other.equals(this);
//            default -> false;
//        };
//    }
//
//    @DocumentSymbol
//    public SymbolKind documentSymbol(SymbolTableEntry entry) {
//        if (entry.details() instanceof ParamTypeEntry) {
//            return SymbolKind.TypeParameter;
//        } else {
//            return null;
//        }
//    }
//
//    @SemanticToken({SemanticTokenTypes.Parameter, SemanticTokenTypes.Variable})
//    public String semanticToken(SymbolTableEntry entry) {
//        if (entry.details() instanceof ParamTypeEntry) {
//            return SemanticTokenTypes.Parameter;
//        } else if (entry.details() == null) {
//            return SemanticTokenTypes.Variable;
//        } else {
//            return "";
//        }
//    }
//
//    @InlayHint
//    public org.eclipse.lsp4j.InlayHint inlayHint(SymbolTableEntry entry) {
//        if (entry.details() instanceof ParamTypeEntry) {
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
