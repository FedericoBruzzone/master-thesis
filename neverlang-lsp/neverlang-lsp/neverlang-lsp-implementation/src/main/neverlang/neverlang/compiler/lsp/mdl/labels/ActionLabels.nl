module neverlang.compiler.lsp.mdl.labels.ActionLabels {

    imports {
        neverlang.core.typesystem.defaults.CompilationUnit;
        neverlang.core.typesystem.defaults.DefaultSymbolTableEntryFactory;
        neverlang.core.typesystem.symbols.Token;
        neverlang.core.typesystem.symboltable.EntryKind;
        neverlang.compiler.syn.*;
    }

    reference syntax from neverlang.compiler.mdl.labels.ActionLabels

    role(type-checker)  {
        0 <typecheck>  @{
            try {
                infer label $1
            }
        }.

//        0 @{
//          var helper = $ctx.root().<CompilationHelper>getValue("$CompilationHelper");
//          try {
//            var unit = $ctx.root().<CompilationUnit>getValue("$CompilationUnit");
//            Token token0 = $ctx.nt(1).getValue($ctx.attr(1, "token"));
//            token0 = Token.of("$" + "0", token0.location()); // TODO change to actual token
////            ProductionSignature productionSignature;
////            try {
////                productionSignature = new ProductionSignature($ctx.nt(1).<Integer>getValue($ctx.attr(1, "integer")));
////            } catch (UndefinedAttributeException ex){
////                productionSignature = new ProductionSignature();
////            } catch (neverlang.core.typesystem.InferenceException ex){
////                productionSignature = new ProductionSignature($ctx.nt(2).getValue($ctx.attr(2, "text")));
////            }
////            var entryType0 = unit.typeInference(token0,productionSignature);
////            new DefaultSymbolTableEntryFactory()
////                .withCompilationUnit(unit)
////                .withCompilationHelper(helper)
////                .withEntryType(productionSignature.typeResolution(entryType0))
////                .withEntryKind(EntryKind.USE)
////                .withToken(token0)
////                .bind();
//            var signature0 = new NonTerminalSignature();
//            var entryType0 = unit.typeInference(token0,signature0);
//            new neverlang.core.typesystem.defaults.DefaultSymbolTableEntryFactory()
//                .withCompilationUnit(unit)
//                .withCompilationHelper(helper)
//                .withEntryType(entryType0)
//                .withEntryKind(EntryKind.USE)
//                .withToken(token0)
//                .bind();
//           } catch (neverlang.core.typesystem.NeverlangTypesystemException e){
//            e.submit(helper);
//           }
//        }.

    }

}
