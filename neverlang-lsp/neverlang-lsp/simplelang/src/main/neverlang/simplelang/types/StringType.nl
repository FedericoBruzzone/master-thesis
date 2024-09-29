module simplelang.types.StringType {
    imports {
        neverlang.core.typesystem.symbols.Token;
    }

    reference syntax from neverlang.commons.StringType


    role(type-checker) {
        0 .{
        // infer string $0.token
            $0.type = $$CompilationUnit.typeInference(Token.of("string"), new IdentifierSignature());
        }.

    }
}