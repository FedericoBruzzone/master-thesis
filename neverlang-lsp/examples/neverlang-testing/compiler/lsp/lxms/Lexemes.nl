module neverlang.compiler.lsp.lxms.Lexemes {

    imports {
        neverlang.core.typesystem.symbols.Token;
    }

	reference syntax from neverlang.compiler.lxms.Lexemes

	role(type-checker) {
		0.{
		    $0.token = Token.fromASTNode($n, 0);
		}.

		1 .{
		    $1.sourceCode = Token.fromASTNode($n, 0, 1);
		    $1.preorder = false;
		}.

		2.{
		    $2.sourceCode = Token.fromASTNode($n, 0, 1);
		    $2.preorder = true;
		}.

		3 .{
            $3.integer = Integer.parseInt(#0.text);
            $3.token = Token.fromASTNode($n, 0);
        }.

        /*
		5 .{
		    var type = new TypeTerminalRegex($n, 0);
		    var token = Token.fromASTNode($n, 0);
            new NeverlangSymbolTableEntry.Builder()
                .withContext($ctx)
                .withEntryKind(NeverlangEntryKind.DEFINITION)
                .withType(type)
                .withToken(token)
                .bind();
		}.
        */

		6 @{
			var astNode = $7;
            $6.token = Token.join(astNode
                .streamSymbolList("DottedIdList","Id")
                .map(e -> e.<Token>getValue("token"))
                .toList(),".");
		}.
	}
}