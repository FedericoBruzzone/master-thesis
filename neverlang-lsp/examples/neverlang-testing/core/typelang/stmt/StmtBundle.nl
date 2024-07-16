bundle neverlang.core.typelang.stmt.StmtBundle {
    slices
        neverlang.core.typelang.stmt.TypeStmt
        neverlang.core.typelang.stmt.EvalStmt
        neverlang.core.typelang.stmt.ScopeStmt
        neverlang.core.typelang.stmt.InitRootStmt
        neverlang.core.typelang.stmt.InferStmt
        neverlang.core.typelang.stmt.CatchStmt
        neverlang.core.typelang.stmt.TypeCheckStmt
        neverlang.core.typelang.stmt.ResolveStmt
        neverlang.core.typelang.stmt.ImportStmt

    rename {
       Eval --> Statement;
       ScopeStmt --> Statement;
       EnterScopeStmt --> Statement;
       ExitScopeStmt --> Statement;
       TypeStmt --> Statement;
       InitRoot --> Statement;
       InferStmt --> Statement;
       CatchStmt --> Statement;
       TypeCheckStmt --> Statement;
       ResolveStmt --> Statement;
       ImportStmt --> Statement;
    }
}
