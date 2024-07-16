module neverlang.compiler.mdl.Module {
	imports {
		neverlang.compiler.*;
		neverlang.compiler.syn.*;
		neverlang.utils.*;
		java.util.List;
        java.util.ArrayList;
    }

	reference syntax {
		UnitModule:
			Unit <-- Module;
		Module:
			Module <-- LangSpecModule UnitName "{" ModuleContents "}";
		ModuleContents:
			ModuleContents <-- Imports SyntaxRole SyntaxPriorities RoleList;
		Imports:
		    // 11                         12
			Imports <-- "imports" "{" ImportList "}";
			// 13
       		Imports <-- "" ;
		ImportStringSeq:
		    // 14            15         16
			ImportString <-- Id "." ImportString;
		ImportStringId:
		    // 17            18
			ImportString <-- Id ";";
		ImportStringStar:
		    //19
			ImportString <-- "*" ";";
		ImportList:
		    //20                21          22
        	ImportList <-- ImportString ImportList;
        	//23                24
        	ImportList <-- ImportString;

        categories :
            Brackets = { "{", "}" },
            Operator = { "*", ".", "*", ";" },
            Keyword = { "module", "imports"};

        in-buckets :
        	$ImportList[1] <-- { UnitNames };
	}

	role(translate) {
	    UnitModule: .{
	        $UnitModule[0].SourceCode = $UnitModule[1].SourceCode;
	    }.

	    Module: .{
	        ModuleSource msrc = $$UnitHolder.getModule();
	        $Module[0].SourceCode = msrc;
	    }.

	    Module[2]: .{
	        String mname = $Module[2].LongId;
	        String langSpec = $Module[1].LangSpec;
	        ModuleSource msrc = $$UnitHolder.getModule();
	        msrc.setCanonicalName(mname);
	        if(langSpec!=null) msrc.setLanguage(langSpec);
	    }.

	    Imports: .{
	        List<String> importList = AttributeList.collectFrom($Imports[1], "ImportString");
	        $$UnitHolder.getModule().setImports(importList);
        }.

        ImportStringSeq: .{
            StringBuilder sb = $ImportStringSeq[2].ImportString;
            String id = $ImportStringSeq[1].Id;
            sb.insert(0, ".").insert(0,id);
            $ImportStringSeq[0].ImportString = sb;
        }.

        ImportStringId: .{
            String id = $ImportStringId[1].Id;
            StringBuilder sb = new StringBuilder();
            $ImportStringId[0].ImportString = sb.append(id);
        }.

        ImportStringStar: .{
            StringBuilder sb = new StringBuilder();
	        $ImportStringStar[0].ImportString = sb.append("*");
        }.
	}
}