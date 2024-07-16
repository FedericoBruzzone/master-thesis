module neverlang.compiler.lsp.mdl.syn.SyntaxRole {

    imports {
        neverlang.core.typesystem.symbols.Token;
    }

    reference syntax from neverlang.compiler.mdl.syn.SyntaxRole


    role(before-each) {
        SyntaxRole: .{
            $SyntaxRole[0].token = Token.join($n, 0, 1).withText("SYNTAX");
        }.
    }

	role(type-checker) {
	    SyntaxRole: <typecheck> .{
	        try {
                define scope syntax $0 from #2 to #3 [
                    run $1 priority syntax
                ]
	        }
	    }.

        SyntaxRoleReferenceFrom: <typecheck> .{
            try {
                eval $3
                infer module $3 => $3.moduleType
                infer syntax SYNTAX from $3.moduleType => $3.syntaxType
                import $3.syntaxType as SYNTAX
            }
        }.
	}
}
