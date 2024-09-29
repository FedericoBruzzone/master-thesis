module simplelang.stmt.IfStatement {
    imports {
//        simplelang.symboltable.CompilationUnit;
//        neverlang.core.typesystem.defaults.CompilationUnit;
//        simplelang.typesystem.signatures.IdentifierSignature;
        neverlang.core.typesystem.symbols.Token;
        neverlang.core.typesystem.Variance;
    }

	reference syntax {
	    IfStatement <-- "if" Expression "{" BlockStatement "}" ElseBranch;
	    ElseBranch <-- "";
	    ElseBranch <-- "else" "{" BlockStatement "}";

	    categories:
	        keyword = { "if", "else" };
	}

	role(before-each){
	    0 .{
	        $0.type = $$CompilationUnit.typeInference(Token.of("boolean"), new IdentifierSignature());
	        $0.token = Token.fromASTNode($n, 0);
	    }.
	}

	role(type-checker) {
        0 <typecheck> .{
            try {
                eval $1
                check $0.token : $0.type is controvariant $1.type
            }
            tryEval $2
            tryEval $3
        }.
	}

}

