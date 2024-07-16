module neverlang.core.typelang.TypeLangMain {

    imports {
        java.util.stream.Collectors;
        java.util.stream.Stream;
        java.util.ArrayList;
    }

    reference syntax {
        P:  Program <-- StatementList;
        MS: StatementList <-- Statement StatementList;
        SS: StatementList <-- Statement;
    }

    role(translate) {
       P: .{
            $P[0].Text = $$Formatting.collectAll($P[1].list,true);
       }.

       MS: .{
            ArrayList<String> list = $MS[2].list;
            String txt = $MS[1].Text;
            list.add(0,txt);
            $MS[0].list = list;
       }.

       SS: .{
            String txt = $SS[1].Text;
            var list = new ArrayList<String>();
            list.add(txt);
            $SS[0].list = list;
       }.

    }

}
