module neverlang.compiler.lsp.mdl.Action {
    imports {
        neverlang.core.typesystem.symbols.Token;
    }

    /*
	reference syntax {
		Action:
			Action <-- Integer LangSpec VerbatimCode;
        ActionWithLabel:
        	Action <-- ActionLabel LangSpec VerbatimCode;
	}
	*/
	reference syntax from neverlang.compiler.mdl.Action

	role(before-each) {
	    0 @{
	        Token token = $1.token;
	        $1.token = token.withPrefix("$");
	    }.
	}

	role(type-checker) <typecheck> {
	    0 .{
            try {
                infer nonterminal $1
            }
	    }.
//	    4 .{
//	        try {
//                infer nonterminal $5
//            }
//	    }.
    }
}
