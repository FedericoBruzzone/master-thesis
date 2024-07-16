module simplelang.fun.FunCallExpression {

    imports {
        neverlang.core.typesystem.defaults.DefaultSymbolTableEntry;
        neverlang.core.typesystem.SymbolTableEntry;
    }
    reference syntax {
       FunCall: Expression <-- Identifier ParamList;
       ParamList <-- "(" CommaSeparatedExpressionList ")";
       ParamList <-- "";
       CommaSeparatedExpressionList <-- Expression "," CommaSeparatedExpressionList;
       CommaSeparatedExpressionList <-- Expression;
       CommaSeparatedExpressionList <-- "";
    }

    role(type-checker) {

        when(% $ctx.evalReturn($n.ntchild(1), "type") != null %)
        0 <typecheck> .{
            eval $1
            infer function $1 with $2 => $0
        }.

        0 <typecheck> .{
            eval $1
            infer identifier $1 => $0
        }.

        3 @{
            $3.type = $n.ntchild(0).streamSymbolList("CommaSeparatedExpressionList", "Expression")
                .map(e -> e.<SymbolTableEntry>getValue("type"))
                .toArray(SymbolTableEntry[]::new);
        }.
    }
}