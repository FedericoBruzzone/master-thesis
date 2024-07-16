module neverlang.core.typelang.stmt.TypeCheckStmt {

    imports {
        neverlang.core.typelang.Formatting;
    }

    reference syntax {
        TypeCheckStmt <-- "check" NonTerminal ":" Type "is" TypeVariance Type;
    }

    role(translate) {
        0 .{
            $$Formatting.check($n,0,1,2,3);
        }.
        1 .{
            $$Formatting.readAttribute($1, $1, "token");
        }.
        3 .{
            $3.Text = $$Formatting.withVariance($3);
        }.
    }
}
