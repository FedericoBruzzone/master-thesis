language neverlang.compiler.lsp.NeverlangLangLSP {
    slices
    	bundle (neverlang.compiler.lsp.lang.LanguageBundle)
        bundle (neverlang.compiler.lsp.mdl.ModuleBundle)
		bundle (neverlang.compiler.lsp.mdl.syn.Syntax)
        bundle (neverlang.compiler.lsp.slc.SliceBundle)
		bundle (neverlang.compiler.lsp.mdl.labels.Labels)
        bundle (neverlang.compiler.lpl.Tags)
        neverlang.compiler.lsp.unit.Unit
        neverlang.compiler.unit.UnitName
		neverlang.compiler.lsp.endslc.EndemicSlice
        neverlang.compiler.lsp.lxms.Lexemes
        neverlang.compiler.lsp.bndl.Bundle
        neverlang.compiler.lsp.cat.Categories
        neverlang.compiler.buckets.Buckets

        bundle (neverlang.compiler.legacy.Legacy)
		bundle (neverlang.compiler.mdl.syn.ebnf.NonterminalBundleEBNF)

    endemic slices
    	neverlang.compiler.unit.UnitHolderEndemicSlice
    	neverlang.compiler.mdl.syn.ebnf.AnonymousProductions
//    	neverlang.compiler.lsp.CompilationEndemicSlices

//	roles syntax <+ before-each : type-checker
    roles syntax < useless
}
