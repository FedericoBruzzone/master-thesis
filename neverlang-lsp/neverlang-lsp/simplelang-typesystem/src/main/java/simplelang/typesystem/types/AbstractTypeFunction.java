// package simplelang.typesystem.types;
//
// import neverlang.core.lsp.defaults.binder.FunctionTypeBinder;
// import neverlang.core.typelang.annotations.*;
// import neverlang.core.typesystem.*;
// import neverlang.core.typesystem.defaults.DefaultTypeScope;
// import neverlang.core.typesystem.symboltable.EntryKind;
// import neverlang.core.typesystem.typenv.EntryTypeBinder;
// import org.eclipse.lsp4j.Position;
// import org.eclipse.lsp4j.SemanticTokenTypes;
// import org.eclipse.lsp4j.SymbolKind;
// import neverlang.core.lsp.defaults.typenv.OperatorEntry;
// import simplelang.typesystem.signatures.FunctionSignature;
//// import import neverlang.core.lsp.defaults.signatures.FunctionSignature;
//
// import neverlang.core.lsp.defaults.typenv.ReturnEntry;
//
// import java.util.concurrent.atomic.AtomicInteger;
// import java.util.stream.Stream;
// import neverlang.core.lsp.defaults.typenv.ParamTypeEntry;
// import neverlang.core.lsp.defaults.types.TypeUnresolved;
//
// public abstract class AbstractTypeFunction extends DefaultTypeScope {
//
//    @Override
//    public String id() {
//        return "function";
//    }
//
//    protected AbstractCompilationHelper compilationHelper = null;
//
//    @Override
//    public Type withCompilationHelper(AbstractCompilationHelper compilationHelper) {
//        this.compilationHelper = compilationHelper;
//        return this;
//    }
//
//    @Override
//    public boolean matchSignature(Signature signature) {
//        if (signature instanceof FunctionSignature functionSignature) {
//            var counter = new AtomicInteger(0);
//            var isAssignable = streamSymbolTableEntries()
//                    .filter(e -> e.details() instanceof ParamTypeEntry)
//                    .peek(e -> counter.incrementAndGet())
//                    .allMatch(e -> {
//                        try {
//                            ParamTypeEntry paramsSymbolTableEntry = (ParamTypeEntry) e.details();
//                            var signatureParamType =
// functionSignature.getPositionalParameter(paramsSymbolTableEntry.position());
//                            return signatureParamType.type() instanceof TypeUnresolved ||
// e.type().isAssignableFrom(signatureParamType.type(), Variance.INVARIANT);
//                        } catch (ArrayIndexOutOfBoundsException ex) {
//                            return false;
//                        }
//                    });
//            return isAssignable && counter.get() == functionSignature.params().length;
//        } else {
//            return false;
//        }
//    }
//
//    @DocumentSymbol
//    public SymbolKind documentSymbol(SymbolTableEntry entry) {
//        return entry.entryKind().equals(EntryKind.DEFINE) ? SymbolKind.Function : null;
//    }
//
//    @SemanticToken({SemanticTokenTypes.Function, SemanticTokenTypes.Operator})
//    public String semanticToken(SymbolTableEntry entry) {
//        if (entry.details() instanceof OperatorEntry) {
//            return SemanticTokenTypes.Operator;
//        } else {
//            return SemanticTokenTypes.Function;
//        }
//    }
//
//    @InlayHint
//    public org.eclipse.lsp4j.InlayHint inlayHint(SymbolTableEntry entry) {
//        try {
//            if (entry.entryKind().equals(EntryKind.DEFINE)) {
//                var inlayHint = new org.eclipse.lsp4j.InlayHint();
//                inlayHint.setLabel(returnTypes().findFirst().map(Type::id).orElse(""));
//                var position = new Position(
//                        entry.selectionRange().startRow(),
//                        entry.selectionRange().startCol());
//                inlayHint.setPosition(position);
//                inlayHint.setPaddingRight(true);
//                return inlayHint;
//            }
//        } catch (Exception ignored) {
//        }
//        return null;
//    }
//
//    public Stream<Type> returnTypes() {
//        return streamSymbolTableEntries()
//                .filter(e -> e.details() instanceof ReturnEntry)
//                .map(SymbolTableEntry::type);
//    }
//
//    @Override
//    public Class<? extends EntryTypeBinder> getTypeBinder() {
////        return SimpleLangTypeBinder.class;
//        return FunctionTypeBinder.class;
//    }
// }
