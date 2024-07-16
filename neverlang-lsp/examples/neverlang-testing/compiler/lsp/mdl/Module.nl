module neverlang.compiler.lsp.mdl.Module {

	reference syntax from neverlang.compiler.mdl.Module

	role(type-checker) {

        2 <typecheck> .{
            try {
                eval $4
                define scope module $4 from #0 to #1 [
                     run $5 priority module
                ]
            }
        }.
	}
}