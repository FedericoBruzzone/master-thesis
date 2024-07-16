module neverlang.core.typelang.blck.InsideBlock {
    imports {
        neverlang.core.typelang.Formatting;
    }

    reference syntax {
        Inside: Inside <-- "from" "#" Integer "to" "#" Integer;
        VoidInside: Inside <-- "";
    }

    role(translate) {

        Inside: .{
            $$Formatting.withFoldingRange($n, 0, 1);
        }.
    }
}