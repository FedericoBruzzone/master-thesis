package simplelang.typesystem.signatures;

// import neverlang.core.typesystem.Signature;
// import neverlang.core.typesystem.SymbolTableEntry;
// import simplelang.SimpleLangModule;
// import simplelang.typesystem.types.TypeUnresolved;
// import neverlang.core.typelang.annotations.TypeLangAnnotation;
// import neverlang.core.typelang.annotations.TypeSystemKind;
//
// @TypeLangAnnotation(
//        keyword = "identifier",
//        label = SimpleLangModule.LANGUAGE,
//        kind = TypeSystemKind.SIGNATURE
// )
// public class IdentifierSignature implements Signature {
//
//    private final SymbolTableEntry[] symbolTableEntry;
//
//    public IdentifierSignature(){
//        symbolTableEntry = new SymbolTableEntry[0];
//    }
//
//    public IdentifierSignature(SymbolTableEntry[] symbolTableEntry){
//        this.symbolTableEntry = symbolTableEntry;
//    }
//
//    @Override
//    public SymbolTableEntry typeResolution(SymbolTableEntry entryType) {
//        if (symbolTableEntry.length == 1 && entryType.type() instanceof TypeUnresolved){
//            entryType.refType().set(symbolTableEntry[0].type());
//        }
//        return entryType;
//    }
// }
