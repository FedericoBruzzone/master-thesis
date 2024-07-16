module simplelang.expr.Expression {

    imports {
        neverlang.core.typesystem.Type;
        neverlang.core.typesystem.typenv.EntryType;
        neverlang.core.typesystem.defaults.DefaultSymbolTableEntry;
        neverlang.core.typesystem.symbols.Token;
        //simplelang.symboltable.CompilationUnit;
        //neverlang.core.typesystem.defaults.CompilationUnit;
//        simplelang.typesystem.signatures.IdentifierSignature;
//        simplelang.typesystem.signatures.FunctionSignature;
        simplelang.symboltable.SymbolTableEntryFactory;
    }

    reference syntax {
        Expression <-- Literal;
        Expression <-- "(" Expression ")";
        BE: Expression <-- Expression ConcreteBinaryOperator Expression;
        CBO: ConcreteBinaryOperator <-- BinaryOperator;
        BinaryOperator <-- "+";
        BinaryOperator <-- "-";
        BinaryOperator <-- "*";
        BinaryOperator <-- "/";
        BinaryOperator <-- "==";

        categories:
            operator = { "+", "-", "*", "/" },
            brackets = { "(", ")" };
    }

    role(type-checker) {
        CBO: @{
            $CBO[0].token = Token.fromNTASTNode($n, 0);
        }.

        2 @{
            $2.type = $3.type;
        }.

        4 <typecheck> @{
            infer function $6 with [$5 $7] => $4
        }.

    }
}
