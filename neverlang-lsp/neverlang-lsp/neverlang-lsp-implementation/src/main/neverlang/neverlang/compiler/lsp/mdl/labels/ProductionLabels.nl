module neverlang.compiler.lsp.mdl.labels.ProductionLabels {

	reference syntax from neverlang.compiler.mdl.labels.ProductionLabels

	/*
    Production <-- Id ":" Production;
    */

	role(type-checker) {
        0 .{
            eval $1;
            $2.token = $1.token;
            eval $2;
        }.
	}
}
