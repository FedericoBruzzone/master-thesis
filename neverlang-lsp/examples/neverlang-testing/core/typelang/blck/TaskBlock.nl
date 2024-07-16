module neverlang.core.typelang.blck.TaskBlock {

    imports {
        neverlang.core.typelang.Formatting;
        neverlang.core.typelang.NamingHelper;
    }

    reference syntax {
        Block: Block <-- "[" Task "]";
        VoidBlock: Block <-- "";
        Task: Task <-- "run" Root NonTerminalList "priority" Priority Then;
        IsRoot: Root <-- "root";
        NotRoot: Root <-- "";
    }

    role(translate) {
        Block: .{
            $Block[0].Text = $Block[1].<String>getOptValue("Text").orElse("");
        }.

        Task: .{
            $$Formatting.getTaskBuilder($n, $Task[1].isRoot, 0, 1, 2, 3);
        }.

        Task[3]: .{
            $Task[3].Text = $$Formatting.getPriority($Task[3]);
        }.

        IsRoot: .{
            $IsRoot[0].isRoot = true;
        }.

        NotRoot: .{
            $NotRoot[0].isRoot = false;
        }.
    }
}
