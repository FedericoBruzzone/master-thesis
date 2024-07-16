module neverlang.compiler.lsp.bndl.Bundle {

	reference syntax from neverlang.compiler.bndl.Bundle

	role(type-checker) <typecheck> {

		Bundle: .{
			eval $3
			define scope bundle $3 from #0 to #1 [
			    run $4 $5 $6 priority bundle
			]
		}.
	}
}
