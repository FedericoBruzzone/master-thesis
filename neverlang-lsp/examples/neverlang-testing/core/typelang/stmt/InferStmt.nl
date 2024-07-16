module neverlang.core.typelang.stmt.InferStmt {

    imports {
        neverlang.core.typelang.Formatting;
    }

    reference syntax {
        Infer: InferStmt <-- "infer" Signature TokenIdentifier From? With AssignTo;
        From: From <-- "from" Type;
        AssignTo: AssignTo <-- "=>" NonTerminal;
        AssignTo <-- ""; 
        With <-- "";
        WITH: With <-- "with" NonTerminalParamList;
        MP: NonTerminalParamList <-- WrappedNonTerminalList "," NonTerminalParamList;
        SP: NonTerminalParamList <-- WrappedNonTerminalList;
        Wrapped: WrappedNonTerminalList <-- "[" NonTerminalList "]" ;
        NotWrapped: WrappedNonTerminalList <-- NonTerminal ;
    }

    role(translate) {

        Infer[1]: .{
            $Infer[1].Text = $$Formatting.withSignature($Infer[1]);
        }.

        Infer: .{
            $$Formatting.getInferType($n,"use",0,1,2,3,4);
        }.

        From: .{
            $$Formatting.getTypeFromSymbolTableEntry($n,0);
        }.

        AssignTo: .{
            $$Formatting.childAttributeWriteFormat($n, 0, "type");
        }.

        WITH: .{
            $$Formatting.buildSignatureParamType($n, 0);
        }.

        Wrapped: .{
            $$Formatting.buildEntryType($n, 0, false);
        }.

        NotWrapped: .{
            $$Formatting.buildEntryType($n, 0, true);
        }.
    }
}
