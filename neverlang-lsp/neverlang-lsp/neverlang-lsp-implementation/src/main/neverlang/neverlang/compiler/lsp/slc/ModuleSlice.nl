module neverlang.compiler.lsp.slc.ModuleSlice {


	reference syntax from neverlang.compiler.slc.ModuleSlice


	role(type-checker) <typecheck> {

	    Slice: .{
	        eval $3
	        define scope slice $3 from #0 to #1 [
	            run $4 priority slice
	        ]
	    }.
	}
}