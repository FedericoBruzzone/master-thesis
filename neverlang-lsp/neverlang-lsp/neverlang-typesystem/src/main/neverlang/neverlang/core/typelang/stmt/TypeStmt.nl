module neverlang.core.typelang.stmt.TypeStmt {

    imports {
        neverlang.core.typelang.Formatting;
    }

    reference syntax {
        Define: TypeStmt <-- "define" Type ScopeIdentifier Inside AttributeList Then;
        Use: TypeStmt <-- "use" ScopeIdentifier "as" Type;
        TypeScope: Type <-- NonTerminal;
    }
    role(translate) {

        Define[1]: .{
            $Define[1].Text = $$Formatting.withType($Define[1]);
        }.

        Define: .{
            $$Formatting.getTypeDefinition($n,"define",0,1,2,3,4);
        }.

        Use[2]: .{
            $Use[2].Text = $$Formatting.withType($Use[2]);
        }.

        Use: .{
            $$Formatting.getTypeDefinition($n,"use",1,0,null,null,null);
        }.

        TypeScope: .{
            $$Formatting.readAttribute($n, $TypeScope[1], "type", $$Formatting.symbolTableEntry());
        }.
    }
}