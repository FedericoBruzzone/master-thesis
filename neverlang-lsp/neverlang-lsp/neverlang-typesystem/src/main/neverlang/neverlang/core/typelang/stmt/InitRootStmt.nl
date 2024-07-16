module neverlang.core.typelang.stmt.InitRootStmt {
    imports {
        neverlang.core.typelang.Formatting;
        neverlang.core.typelang.NamingHelper;
    }

    reference syntax {
        InitRoot <-- "initRoot" Priority;
    }

    role(translate) {
        0 .{
            $$Formatting.initRoot($n, 0);
        }.

        1 .{
            $1.Text = $$Formatting.getPriority($1);
        }.
    }
}