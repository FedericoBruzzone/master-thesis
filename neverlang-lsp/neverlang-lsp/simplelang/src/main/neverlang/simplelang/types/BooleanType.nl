module simplelang.types.BooleanType {
    imports {
        //simplelang.symboltable.CompilationUnit;
        //neverlang.core.typesystem.defaults.CompilationUnit;
//        simplelang.typesystem.signatures.IdentifierSignature;
        neverlang.core.typesystem.symbols.Token;
    }

    reference syntax {
        Boolean <-- "true";
        Boolean <-- "false";

        categories:
            constant = {"true", "false"};

    }

    role(type-checker) {
        0 .{
            $0.type = $$CompilationUnit.typeInference(Token.of("boolean"), new IdentifierSignature());
        }.

        1 .{
            $1.type = $$CompilationUnit.typeInference(Token.of("boolean"), new IdentifierSignature());
        }.
    }
}