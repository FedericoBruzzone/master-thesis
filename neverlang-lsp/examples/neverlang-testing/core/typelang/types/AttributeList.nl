module neverlang.core.typelang.types.AttributeType {

    imports {
        neverlang.core.typelang.Formatting;
    }
    reference syntax {
        Attributes <-- "{" AttributeList "}";
        VOIDATTR: Attributes <-- "";
        MULTIPLE: AttributeList <-- Attribute AttributeList;
        SINGLE: AttributeList <-- Attribute;
        VOID: AttributeList <-- "";
        ATTR: Attribute <-- Detail NonTerminal;
    }


    role(translate) {
        MULTIPLE: <template> .{{{$MULTIPLE[1].Text}}{{$MULTIPLE[2].Text}}}.

        SINGLE: <template> .{{{$SINGLE[1].Text}}}.

        VOID: <template> .{}.

        ATTR: .{
            $$Formatting.extractAttribute($ATTR[1]);
            $$Formatting.withAttribute($n, 0, 1);
        }.
    }
}
