module simplelang.stmt.AssignStatement {

    imports {
        neverlang.core.typesystem.defaults.DefaultSymbolTableEntry;
        neverlang.core.typesystem.TypeMismatchException;
        neverlang.core.typesystem.Variance;
        neverlang.core.typesystem.symbols.*;
        neverlang.core.typesystem.symboltable.EntryKind;
        simplelang.symboltable.*;
//        simplelang.typesystem.types.*;
//        simplelang.typesystem.signatures.*;
        //simplelang.symboltable.CompilationUnit;
        //neverlang.core.typesystem.defaults.CompilationUnit;
        neverlang.core.typesystem.InferenceException;
    }

	reference syntax {
	    AssignStatement <-- Identifier "=" Expression ";";
	}

	role(type-checker) {
        0 <typecheck> @{
            try {
                infer identifier $1.token with [$2.type] => $1.type
                check $1.token : $1.type is invariant $2.type
                use $1.token as $1.type
            } on InferenceException {
                define $2 $1
            }
        }.
	}

}

