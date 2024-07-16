module neverlang.core.typelang.types.NonTerminalType {

    imports {
        neverlang.core.typelang.Formatting;
    }
    reference syntax {
        NonTerminal <-- "$" Integer OptionalIdentifier;
        OptionalIdentifier <-- "." Identifier;
        OptionalIdentifier <-- "";
        NonTerminalList <-- NonTerminal NonTerminalList;
        NonTerminalList <-- NonTerminal;
    }

    role(translate) {
        0 .{
            $0.Text = $1.Text;
            $ctx.nt(2).<String>getOptValue("Text").ifPresent(e -> $0.Attribute = e);
        }.

        3 .{
            $3.Text = $4.Text;
        }.
    }
}