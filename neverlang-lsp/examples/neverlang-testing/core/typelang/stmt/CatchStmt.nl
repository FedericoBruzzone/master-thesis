module neverlang.core.typelang.stmt.CatchStmt {

    imports {
        neverlang.core.typelang.Formatting;
        java.util.stream.Stream;
        java.util.List;
    }

    reference syntax {
        CatchStmt <-- "try" "{" StatementList "}" OrStmt;
        OrStmt <-- "on" Exception "{" StatementList "}";
        OrStmt <-- "";
        Exception <-- Identifier;
        Exception <-- "";
   } 

    role(translate) {
        0 .{
            $$Formatting.catchStmt($n, 1, $$Formatting.collectAll($1.list, false), $$Formatting.collectAll($2.list, false));
        }.

        3 .{
            $n.ntchild(0)
                .<String>getOptValue("Text")
                .ifPresent(e -> $n.setValue("Text", e));
            $3.list = $5.list;
        }.

        6 .{
            $6.list = List.of();
        }.

        7 .{
            $7.Text = $8.Text;
        }.
    }
}
