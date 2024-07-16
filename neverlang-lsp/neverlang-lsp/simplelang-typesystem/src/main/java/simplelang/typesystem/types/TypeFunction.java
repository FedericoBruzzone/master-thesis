// package simplelang.typesystem.types;
//
// import neverlang.core.lsp.defaults.binder.FunctionTypeBinder;
// import neverlang.core.lsp.defaults.typenv.OperatorEntry;
// import neverlang.core.typelang.annotations.*;
// import neverlang.core.typesystem.*;
// import neverlang.core.typesystem.defaults.DefaultTypeScope;
// import neverlang.core.typesystem.symboltable.EntryKind;
// import neverlang.core.typesystem.typenv.EntryTypeBinder;
// import org.eclipse.lsp4j.Position;
// import org.eclipse.lsp4j.SemanticTokenTypes;
// import org.eclipse.lsp4j.SymbolKind;
//
// import java.lang.reflect.InvocationTargetException;
// import java.util.List;
// import java.util.function.Supplier;
// import java.util.stream.IntStream;
// import neverlang.core.lsp.defaults.types.functions.AbstractTypeFunction;
//
// @TypeLangAnnotation(
//        keyword = "function",
//        kind = TypeSystemKind.TYPE
// )
// public class TypeFunction extends AbstractTypeFunction {
//
//    private Supplier<neverlang.core.typesystem.SymbolTableEntryFactory<?, ?>>
// symbolTableEntryFactorySupplier;
//
//    @Override
//    public Type withSymbolTableEntryFactory(Class<? extends
// neverlang.core.typesystem.SymbolTableEntryFactory<?,?>> symbolTableEntryFactory) {
//        this.symbolTableEntryFactorySupplier = () -> {
//            try {
//                return symbolTableEntryFactory.getConstructor().newInstance();
//            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
//                    NoSuchMethodException e) {
//                throw new RuntimeException(e);
//            }
//        };
//        return this;
//    }
//
//    @Override
//    public Type
// withSymbolTableEntryFactory(Supplier<neverlang.core.typesystem.SymbolTableEntryFactory<?,?>>
// symbolTableEntryFactorySupplier) {
//        this.symbolTableEntryFactorySupplier = symbolTableEntryFactorySupplier;
//        return this;
//    }
//
//    @Override
//    public Type bind(List<Type> args) {
//        bindAsTypes(args.subList(0, args.size() - 1));
//        bindAsReturn(args.get(args.size() - 1));
//        return this;
//    }
//    private void bindAsTypes(List<Type> args) {
//        String identifier = "x";
//        IntStream.range(0, args.size()).forEach(i -> {
//            this.symbolTableEntryFactorySupplier.get()
//                    .withEntryKind(EntryKind.DEFINE)
//                    .withPosition(i)
//                    .withIdentifier(identifier + i)
//                    .withEntryType(args.get(i))
//                    .inScope(this)
//                    .withCompilationHelper(compilationHelper)
//                    .bind();
//        });
//    }
//
//    private void bindAsReturn(Type returnType) {
//        ((SymbolTableEntryFactory<String, ?>)this.symbolTableEntryFactorySupplier.get())
//                .withEntryKind(EntryKind.DEFINE)
//                .withIdentifier("return")
//                .withEntryType(returnType)
//                .inScope(this)
//                .withCompilationHelper(compilationHelper)
//                .bind();
//    }
//
//    @Override
//    @DocumentSymbol
//    public SymbolKind documentSymbol(SymbolTableEntry entry) {
//        return super.documentSymbol(entry);
//    }
//
//    @Override
//    @SemanticToken({SemanticTokenTypes.Function, SemanticTokenTypes.Operator})
//    public String semanticToken(SymbolTableEntry entry) {
//        return super.semanticToken(entry);
//    }
//
//    @Override
//    @InlayHint
//    public org.eclipse.lsp4j.InlayHint inlayHint(SymbolTableEntry entry) {
//        return super.inlayHint(entry);
//    }
// }
