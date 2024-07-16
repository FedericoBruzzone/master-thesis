module simplelang.stmt.Statement {
    imports {
//        simplelang.symboltable.CompilationHelper;
        neverlang.core.typesystem.symboltable.EntryKind;
        simplelang.symboltable.SymbolTableEntryFactory;
        //simplelang.symboltable.CompilationUnit;
        //neverlang.core.typesystem.defaults.CompilationUnit;
        neverlang.core.typesystem.symbols.Token;
    }

	reference syntax {
	    BlockStatement <-- StatementList ReturnStatement;
	    BlockStatement <-- StatementList;
	    RT: ReturnStatement <-- "return" Expression ";";
		MST: StatementList <-- Statement StatementList;
		SST: StatementList <-- Statement;
		StatementList <-- "";

		categories:
            keyword = { "return" };
	}


    role(before-each) {
        RT: .{
            $RT[0].token = Token.fromASTNode($n, 0);
        }.
    }

	role(type-checker) <typecheck> {

        RT: .{
            try {
                eval $6
                define $6 $5
            }
        }.

        7 <typecheck> .{
            tryEval $8
            eval $9
        }.

        10 <typecheck> .{
            tryEval $11
        }.
	}
}

