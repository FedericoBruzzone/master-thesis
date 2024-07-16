language neverlang.compiler.lsp.NeverlangLangLSP {
    slices
    	bundle (neverlang.compiler.lang.LanguageBundle)
        bundle (neverlang.compiler.lsp.mdl.ModuleBundle)
		bundle (neverlang.compiler.lsp.mdl.syn.Syntax)
        bundle (neverlang.compiler.slc.SliceBundle)
		bundle (neverlang.compiler.mdl.labels.Labels)
        bundle (neverlang.compiler.lpl.Tags)
        neverlang.compiler.lsp.unit.Unit
        neverlang.compiler.unit.UnitName
		neverlang.compiler.endslc.EndemicSlice
        neverlang.compiler.lsp.lxms.Lexemes
        neverlang.compiler.bndl.Bundle
        neverlang.compiler.cat.Categories
        neverlang.compiler.buckets.Buckets

        bundle (neverlang.compiler.legacy.Legacy)
		bundle (neverlang.compiler.mdl.syn.ebnf.NonterminalBundleEBNF)

    endemic slices
        neverlang.compiler.lsp.CompilationEndemicSlices
    	neverlang.compiler.unit.UnitHolderEndemicSlice
    	neverlang.compiler.mdl.syn.ebnf.AnonymousProductions

	roles syntax <+ before-each : type-checker
}
