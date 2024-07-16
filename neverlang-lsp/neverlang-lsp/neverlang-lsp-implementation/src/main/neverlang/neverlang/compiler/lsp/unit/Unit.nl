module neverlang.compiler.lsp.unit.Unit {

    reference syntax {
			Program <-- Unit+;
  	}

	role(type-checker) <typecheck> {
    	0 .{
    	    initRoot sources
    	}.
	}
}

