module neverlang.core.typelang.stmt.InitRootStmt {
    imports {
        neverlang.core.typelang.Formatting;
        neverlang.core.typelang.NamingHelper;
    }

    reference syntax {
        InitRoot <-- "initRoot";
    }

    role(translate) {
        0 .{
            $$Formatting.initRoot($n);
        }.
    }
}