module neverlang.compiler.lsp.cat.Categories {
    imports {
        neverlang.core.typesystem.SymbolTableEntry;
        neverlang.core.typesystem.symboltable.EntryKind;
        neverlang.core.typesystem.defaults.DefaultSymbolTableEntryFactory;
        neverlang.core.typesystem.symbols.Token;
    }


	reference syntax from neverlang.compiler.cat.Categories

	role(type-checker) {

	    3 <typecheck> .{
	        try {
                eval $4
                define scope category $4 from #1 to #2
                enter scope
                eval $5
                eval $6
                exit scope
	        }
	    }.

	    22 @{
	        var unit = $$CompilationUnit;
	        unit.<TypeCategory>currentScope().addString($23.String);
        }.

        24 .{
            var helper = $ctx.root().<CompilationHelper>getValue("$CompilationHelper");
            var unit = $ctx.root().<CompilationUnit>getValue("$CompilationUnit");
            try {
                $ctx.eval($ctx.nt(25));
                Token token0 = $ctx.nt(25).getValue($ctx.attr(25, "token"));
                var signature0 = new RegexSignature();
                var entryType0 = unit.typeInference(token0,signature0);
                new DefaultSymbolTableEntryFactory()
                    .withCompilationUnit(unit)
                    .withCompilationHelper(helper)
                    .withEntryType(entryType0)
                    .withEntryKind(EntryKind.USE)
                    .withToken(token0)
                    .bind();
               unit.<TypeCategory>currentScope().addRegex(entryType0);
            } catch (neverlang.core.typesystem.NeverlangTypesystemException e) {
                e.submit($ctx.root().<CompilationHelper>getValue("$CompilationHelper"));
            }
        }.
	}

}
