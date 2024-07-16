module neverlang.core.typelang.stmt.ImportStmt {

    imports {
        neverlang.core.typelang.Formatting;
    }

    reference syntax {
        Import: ImportStmt <-- "import" Type as TokenIdentifier;
    }
    role(translate) {

        Import: .{
            $$Formatting.getImport($n, 0, 1);
        }.
    }
}