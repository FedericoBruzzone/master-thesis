module neverlang.core.typelang.stmt.ScopeStmt {

    imports {
        neverlang.core.typelang.Formatting;
    }

    reference syntax {
        Scope: ScopeStmt <-- "define" "scope" Type ScopeIdentifier Inside AttributeList Block;
        CurrentScope: ScopeStmt <-- "current" "scope" Identifier Block;
        EnterScope: EnterScopeStmt <-- "enter" "scope";
        ExitScope: ExitScopeStmt <-- "exit" "scope";
    }

    role(translate) {
        Scope[1]: .{
            $Scope[1].Text = $$Formatting.withType($Scope[1]);
        }.

        CurrentScope: .{
            $$Formatting.currentScopeTask($n,0,1);
        }.

        Scope: .{
            $$Formatting.getScopeDefinition($n,"define",0,1,2,3,4);
        }.

        EnterScope: .{
            $$Formatting.enterScope($n);
        }.

        ExitScope: .{
            $$Formatting.exitScope($n);
        }.
    }
}