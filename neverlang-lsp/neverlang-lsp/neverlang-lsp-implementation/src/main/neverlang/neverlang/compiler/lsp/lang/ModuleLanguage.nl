module neverlang.compiler.lsp.lang.ModuleLanguage {

	reference syntax from neverlang.compiler.lang.ModuleLanguage

	role(type-checker) <typecheck> {

	    Language: .{
            eval $3
            define scope language $3 from #0 to #1 [
                run $4 $5 $6 $7 priority language
            ]
        }.
	}
}