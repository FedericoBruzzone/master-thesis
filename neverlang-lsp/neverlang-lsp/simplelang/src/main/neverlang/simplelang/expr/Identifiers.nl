module simplelang.expr.Identifiers {

    imports {
        neverlang.core.typesystem.symbols.Token;
    }

    reference syntax {
        Identifier <-- /[a-zA-Z_][a-zA-Z0-9_]*/;
    }

    role(type-checker) {

        0 .{
            $0.token = Token.fromASTNode($n, 0);
        }.
    }
}