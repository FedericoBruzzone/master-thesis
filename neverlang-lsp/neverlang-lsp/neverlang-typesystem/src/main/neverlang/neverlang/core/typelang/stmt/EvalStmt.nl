module neverlang.core.typelang.stmt.EvalStmt {

    imports {
        neverlang.core.typelang.Formatting;
    }

    reference syntax {
        Eval <-- "eval" NonTerminal;
        Eval <-- "tryEval" NonTerminal;
    }

    role(translate) {
        0 .{
            $$Formatting.eval($n, $1);
        }.

        3 .{
            $$Formatting.catchEval($n, $1);
        }.
    }
}
