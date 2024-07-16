module neverlang.compiler.lsp.unit.Unit {

	reference syntax from neverlang.compiler.unit.Unit

	role(type-checker) {
    	0 <typecheck> .{
    	    initRoot
    	    define scope file ($file ?? global)  [
    	        run root $1 priority unit
    	    ]
    	}.
	}
}