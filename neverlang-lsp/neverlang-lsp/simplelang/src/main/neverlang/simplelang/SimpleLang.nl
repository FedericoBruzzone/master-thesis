language simplelang.SimpleLang {

    slices
        simplelang.program.SimpleLangMain
        bundle(simplelang.types.Literals)
        bundle(simplelang.expr.FunBundle)
        bundle(simplelang.expr.ExpressionBundle)
        bundle(simplelang.stmt.StatementBundle)

    /* TODO: Endemic slices
    endemic slices
        simplelang.CompilationEndemicSlices
    */

    roles syntax < useless //<+ before-each : type-checker

    rename {
        FunDeclaration --> FirstClassStatement;
    }

}
