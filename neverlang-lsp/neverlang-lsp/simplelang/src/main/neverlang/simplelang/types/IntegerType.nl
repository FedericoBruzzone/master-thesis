module simplelang.types.IntegerType {
    imports {
//        simplelang.typesystem.BaseLang;
        //simplelang.symboltable.CompilationUnit;
        //neverlang.core.typesystem.defaults.CompilationUnit;
//        simplelang.typesystem.signatures.IdentifierSignature;
        neverlang.core.typesystem.symbols.Token;
    }
    reference syntax {
        Integer <-- /\d+/;
    }

    role(type-checker) {
        0 .{
        // infer int $0.token
            $0.type = $$CompilationUnit.typeInference(Token.of("int"), new IdentifierSignature());
        }.

    }
}