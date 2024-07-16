module neverlang.compiler.lsp.endslc.EndemicSlice {

	reference syntax from neverlang.compiler.endslc.EndemicSlice

	role (type-checker) <typecheck> {
		EndemicSlice: .{
	    	eval $3
	    	define scope endemic $3 from #0 to #1 [
	    	    run $4 priority slice
	    	]
		}.
	}
}
