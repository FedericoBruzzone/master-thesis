module neverlang.compiler.lsp.mdl.syn.Nonterminal {

    imports {
        neverlang.core.typesystem.symboltable.EntryKind;
    }

	reference syntax from neverlang.compiler.mdl.syn.Nonterminal

	role(type-checker) {

		2 <typecheck> @{
		    define nonterminal $3
		}.

//        2 @{
//            var helper = $ctx.root().<CompilationHelper>getValue("$CompilationHelper");
//            var unit = $ctx.root().<CompilationUnit>getValue("$CompilationUnit");
//            var type0 = new TypeNonTerminal();
//            var a = new neverlang.core.typesystem.defaults.DefaultSymbolTableEntryFactory()
//                .withCompilationHelper(helper)
//                .withCompilationUnit(unit)
//                .withToken($ctx.nt(3).getValue($ctx.attr(3, "token")))
//                .withEntryType(type0)
//                .withEntryKind(EntryKind.DEFINE);
//            a
//                .bind();
//            $ctx.nt(2).setValue("entry", a.getSymbolTableEntry());
//            $ctx.nt(2).setValue("factory", a);
//        }.
	}

}
