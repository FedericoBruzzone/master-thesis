module neverlang.compiler.lsp.mdl.syn.SyntaxRole {

    imports {
        neverlang.compiler.lsp.typesystem.CompilationHelper;
        neverlang.compiler.lsp.typesystem.signature.SyntaxSignature;
        neverlang.compiler.lsp.typesystem.types.TypeModule;
        neverlang.core.typesystem.SymbolTableEntry;
        neverlang.core.typesystem.defaults.DefaultCompilationUnit;
        neverlang.core.typesystem.defaults.DefaultSymbolTableEntryFactory;
        neverlang.core.typesystem.defaults.FindFirstInferencingStrategy;
        neverlang.core.typesystem.symbols.Token;
        neverlang.core.typesystem.symboltable.EntryKind;
   } 

    reference syntax from neverlang.compiler.mdl.syn.SyntaxRole

	role(type-checker) {
	    SyntaxRole: <typecheck> .{
	        try {
                define scope syntax SYNTAX from #0 to #1 [
                    run $1 priority syntax
                ]
	        }
	    }.

        SyntaxRoleReferenceFrom: <typecheck> .{
            try {
                eval $3
                infer module $3 => $3.moduleType
            }
        }.
	}

    role(after-each){
        SyntaxRoleReferenceFrom: .{
            try {
                SymbolTableEntry symbolTableEntry = $n.ntchild(0).getValue("moduleType");
                TypeModule typeModule = symbolTableEntry.type();
                var result = typeModule.inferFromSignature(Token.of("SYNTAX"), new SyntaxSignature());
                var strategy = new FindFirstInferencingStrategy();
                var syntax = strategy.infer(result);
                var unit = $ctx.root().<DefaultCompilationUnit>getValue("$DefaultCompilationUnit");
                var helper = $ctx.root().<CompilationHelper>getValue("$CompilationHelper");
                unit.importInScope("SYNTAX", syntax);
                new DefaultSymbolTableEntryFactory()
                        .withCompilationHelper(helper)
                        .withCompilationUnit(unit)
                        .withIdentifier("SYNTAX")
                        .withEntryType(syntax)
                        .withEntryKind(EntryKind.USE)
                        .bind();
                } catch(Exception e) {
                    // do nothing
                    e.printStackTrace();
                }
        }.
    }



}
