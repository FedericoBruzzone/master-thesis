module neverlang.compiler.lsp.mdl.SemanticRole {
    /*
	reference syntax {
		Role:
			Role <-- "role" "(" Id ")" LangSpec "{" ActionList "}";
		EmptyRole:
			Role <-- "role" "(" Id ")" LangSpec "{" "}";
		LangSpecId:
			LangSpec <-- "<" Id ">";
		LangSpecEmpty:
			LangSpec <-- "";
        LangSpecModule:
        	LangSpecModule <-- "module";
	    LangSpecModuleId:
	    	LangSpecModule <-- "<" Id ">" "module";
        	ActionList <-- AnnotatedAction ActionList ;
        	ActionList <-- AnnotatedAction ;
        	ActionList <-- "";
        	RoleList <-- Role RoleList ;
        	RoleList <-- Role ;
        	RoleList <-- "";


        categories :
            Brackets = { "{", "}", "[", "]", "(", ")" },
            Operator = { "*", ":", "<", ">", "!", ";" },
            Keyword = { "module", "imports", "role" };
	}
	*/

	reference syntax from neverlang.compiler.mdl.SemanticRole

	role(type-checker) <typecheck> {
	    0 .{
	        eval $1
	        define scope role $1 from #3 to #4 [
	            run $3 priority role
	        ]
	    }.
	}
}
