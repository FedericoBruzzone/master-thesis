module neverlang.compiler.lang.ModuleLanguage {
	imports {
		neverlang.compiler.UnitHolder;
		neverlang.utils.*;
		java.util.List;
		java.util.ArrayList;
	}
	
	reference syntax {
    	UnitLang:
    		Unit <-- Language;
	    Language:
	    	Language <-- "language" UnitName "{" LangSlices LangEndemic LangRoles LangRenames "}";
	    
    	categories :
			Brackets = { "(", ")", "{", "}" },
			Keyword = { "language" };
 	}

	role(translate) {
		UnitLang[1]: .{
			$UnitLang[1].SourceCode = $$UnitHolder.getLanguage();
		}.

	    Language: .{
        	$$UnitHolder.getLanguage().setCanonicalName($Language[1].LongId.toString());
	    }.
	}
}
