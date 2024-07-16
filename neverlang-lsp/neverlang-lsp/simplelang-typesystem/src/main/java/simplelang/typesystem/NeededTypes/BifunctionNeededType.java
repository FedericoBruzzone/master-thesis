package simplelang.typesystem.NeededTypes;

// import neverlang.core.typesystem.AbstractCompilationHelper;
// import neverlang.core.typesystem.Type;
// import neverlang.core.typesystem.symboltable.EntryKind;
// import simplelang.symboltable.SymbolTableEntryFactory;
// import simplelang.typesystem.types.AbstractTypeFunction;
//// import neverlang.core.lsp.defaults.types.TypeFunction;
// import simplelang.typesystem.types.TypeFunction;

// @TypeLangAnnotation(
//        kind = TypeSystemKind.NEEDED_TYPE
// )
// public class BifunctionNeededType extends AbstractTypeFunction {
//
//    private AbstractCompilationHelper compilationHelper = null;
//
//    public final Type withCompilationHelper(AbstractCompilationHelper compilationHelper){
//        this.compilationHelper = compilationHelper;
//        return this;
//    }
//
//    public Type bind(List<Type> neededTypes) {
//        assert neededTypes.size() == 3;
//        return bind(
//                neededTypes.get(0),
//                neededTypes.get(1),
//                neededTypes.get(2)
//        );
//    }
//
//    private Type bind(Type a1, Type a2, Type returntype) {
//        var function = new TypeFunction();
//        new SymbolTableEntryFactory()
//                .withEntryKind(EntryKind.DEFINE)
//                .withPosition(0)
//                .withIdentifier("x")
//                .withEntryType(a1)
//                .inScope(function)
//                .withCompilationHelper(compilationHelper)
//                .bind();
//        new SymbolTableEntryFactory()
//                .withEntryKind(EntryKind.DEFINE)
//                .withPosition(1)
//                .withIdentifier("y")
//                .withEntryType(a2)
//                .inScope(function)
//                .withCompilationHelper(compilationHelper)
//                .bind();
//        new SymbolTableEntryFactory()
//                .withEntryKind(EntryKind.DEFINE)
//                .withIdentifier("return")
//                .withEntryType(returntype)
//                .inScope(function)
//                .withCompilationHelper(compilationHelper)
//                .bind();
//        return function;
//    }
// }
