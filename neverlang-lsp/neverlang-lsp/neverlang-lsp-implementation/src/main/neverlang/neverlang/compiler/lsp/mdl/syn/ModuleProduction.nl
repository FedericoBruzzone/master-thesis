module neverlang.compiler.lsp.mdl.syn.ModuleProduction {

    imports {
            neverlang.core.typesystem.symboltable.EntryKind;
            neverlang.core.typesystem.SymbolTableEntryFactory;
    }

	reference syntax from neverlang.compiler.mdl.syn.ModuleProduction

	role(type-checker) {
		0 <typecheck> .{
            try {
                define scope production $0
                enter scope
                eval $1
                define label $1
                eval $2
                eval $3
                exit scope
            } on UndefinedAttributeException {
                define scope production PRODUCTION
                enter scope
                eval $1
                define label $1
                eval $2
                eval $3
                exit scope
            }
		}.

		4 <typecheck> .{
		    try {
                define scope production $4
                enter scope
                eval $5
                eval $6
                exit scope
            } on UndefinedAttributeException {
                define scope production PRODUCTION
                enter scope
                eval $5
                eval $6
                exit scope
            }
		}.

//        0 .{
//        var helper = $ctx.root().<CompilationHelper>getValue("$CompilationHelper");
//        var unit = $ctx.root().<CompilationUnit>getValue("$CompilationUnit");
//            try {
//            var type0 = new TypeProduction();
//            neverlang.core.typesystem.defaults.DefaultSymbolTableEntry scope0 = new neverlang.core.typesystem.defaults.DefaultSymbolTableEntryFactory()
//            	.withCompilationHelper(helper)
//            	.withCompilationUnit(unit)
//            	.withToken($ctx.nt(0).getValue($ctx.attr(0, "token")))
//            	.withEntryType(type0)
//            	.withEntryKind(EntryKind.DEFINE)
//            	.bindScopeOrReuse();
//
//
//            unit.enterScope(scope0);
//            $ctx.eval($ctx.nt(1));
//            $ctx.eval($ctx.nt(2));
//            $ctx.eval($ctx.nt(3));
//            unit.exitScope();
//            } catch (UndefinedAttributeException e) {
//            var type1 = new TypeProduction().withName($ctx.evalReturn($ctx.nt(1), "text"));
//            neverlang.core.typesystem.defaults.DefaultSymbolTableEntry scope1 = new neverlang.core.typesystem.defaults.DefaultSymbolTableEntryFactory()
//            	.withCompilationHelper(helper)
//            	.withCompilationUnit(unit)
//            	.withIdentifier("PRODUCTION")
//            	.withEntryType(type1)
//            	.withEntryKind(EntryKind.DEFINE)
//            	.bindScopeOrReuse();
//
//            unit.enterScope(scope1);
//            $ctx.eval($ctx.nt(1));
//            $ctx.eval($ctx.nt(2));
//            $ctx.eval($ctx.nt(3));
//
////            SymbolTableEntryFactory<?,?> factory = $ctx.nt(2).getValue($ctx.attr(2, "factory"));
////            factory.withIdentifier($ctx.nt(1).getValue($ctx.attr(1, "text"))).bind();
//
//            unit.exitScope();
//            }
//        }.
	}
}
