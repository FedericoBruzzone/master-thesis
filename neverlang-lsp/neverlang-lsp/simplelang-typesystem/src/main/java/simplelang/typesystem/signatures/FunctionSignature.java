// package simplelang.typesystem.signatures;
//
// import neverlang.core.typesystem.Signature;
// import neverlang.core.typesystem.SymbolTableEntry;
// import neverlang.core.typesystem.typenv.EntryType;
// import neverlang.core.lsp.defaults.exceptions.UndefinedReturnTypeException;
// import neverlang.core.lsp.defaults.typenv.ReturnEntry;
//
//// import neverlang.core.lsp.defaults.types.TypeFunction;
//
// import neverlang.core.typelang.annotations.TypeLangAnnotation;
// import neverlang.core.typelang.annotations.TypeSystemKind;
//
// import java.util.Arrays;
// import java.util.concurrent.atomic.AtomicInteger;
//
//// import simplelang.typesystem.typenv.ParamTypeEntry;
//// import simplelang.typesystem.types.TypeUnresolved;
// import neverlang.core.lsp.defaults.typenv.ParamTypeEntry;
// import neverlang.core.lsp.defaults.types.TypeUnresolved;
// import simplelang.typesystem.types.AbstractTypeFunction;
//
//
// @TypeLangAnnotation(
//        keyword = "function",
//        kind = TypeSystemKind.SIGNATURE
// )
// public record FunctionSignature(EntryType[] params) implements Signature {
//
//    public FunctionSignature(SymbolTableEntry[] params){
//        this(Arrays.stream(params).map(SymbolTableEntry::entryType).toArray(EntryType[]::new));
//    }
//
//    public EntryType getPositionalParameter(int index) {
//        return params[index];
//    }
//
//    //WARNING: This should be called after an in
//    public SymbolTableEntry typeResolution(SymbolTableEntry symbolTableEntry) {
//        if (symbolTableEntry.type() instanceof AbstractTypeFunction typeFunction) {
//            //resolve all params that are unresolved
//            var counter = new AtomicInteger(0);
//            typeFunction.streamSymbolTableEntries()
//                    .filter(e -> e.details() instanceof ParamTypeEntry)
//                    .peek(e -> counter.incrementAndGet())
//                    .forEach(e -> {
//                        ParamTypeEntry paramTypeEntry = (ParamTypeEntry) e.details();
//                        var signatureParamType =
// getPositionalParameter(paramTypeEntry.position());
//                        if(signatureParamType.type() instanceof TypeUnresolved) {
//                            //TODO: check how to make this resolution more robust
//                            signatureParamType.refType().set(e.type());
//                        }
//                    });
//            var res =  typeFunction.streamSymbolTableEntries()
//                    .filter(e -> e.details() instanceof ReturnEntry)
//                    .findFirst();
//            if(res.isEmpty()) {
//                throw new UndefinedReturnTypeException("No return type found for function " +
// symbolTableEntry.entryType().token(), symbolTableEntry.location());
//            }
//            return res.get();
//        } else {
//            throw new RuntimeException("This should not happen");
//        }
//    }
// }
