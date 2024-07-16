module neverlang.core.typelang.stmt.ResolveStmt {

    imports {
        neverlang.core.typelang.Formatting;
    }

    reference syntax {
        ResolveStmt <-- "resolve" Type "as" Type;
    }

    role(translate) {
        0 .{
            $$Formatting.resolve($n,0,1);
        }.
    }
}