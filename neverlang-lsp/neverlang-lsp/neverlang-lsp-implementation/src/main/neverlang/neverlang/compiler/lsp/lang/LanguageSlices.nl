module neverlang.compiler.lsp.lang.LanguageSlices {

	reference syntax from neverlang.compiler.lang.LanguageSlices

	role(type-checker) <typecheck> {
    	2 .{
    	    try {
                eval $3
                infer module $3 => $2
                import $2 as $3
    	    }
	    }.

	    4 .{
	        try {
                eval $5
                infer bundle $5 => $4
                import $4 as $5
            }
	    }.
	}
}
