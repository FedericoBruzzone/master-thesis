module neverlang.compiler.lsp.mdl.Module {

	reference syntax from neverlang.compiler.mdl.Module

	role(type-checker) <typecheck> {

        2 .{
            try {
                eval $4
                define scope module $4 from #0 to #1 [
                     run $5 priority module
                ]
            }
        }.

        6 .{
            try {
                current scope SYNTAX [
                    run $7 $8 priority syntax
                ]
            }
            try {
                current scope ROLE [
                    run $9 priority role
                ]
            }
        }.
	}
}
