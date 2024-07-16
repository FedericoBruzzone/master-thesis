module neverlang.core.typelang.blck.ThenBlock {

    imports {
        neverlang.core.typelang.Formatting;
    }

    reference syntax {
        Then: Then <-- "then" Callback;
        Then <-- "";
    }

    role(translate) {
        0 .{
            $$Formatting.callback($n,0);
        }.

        1 .{
            $1.method = $$Formatting.withCallback($1);
        }.
    }
}