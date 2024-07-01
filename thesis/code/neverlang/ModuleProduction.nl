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
}

