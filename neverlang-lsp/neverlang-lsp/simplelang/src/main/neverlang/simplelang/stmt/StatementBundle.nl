bundle simplelang.stmt.StatementBundle {

	slices
	    simplelang.stmt.Statement
	    simplelang.stmt.IfStatement
	    simplelang.stmt.AssignStatement

	rename {
	    IfStatement --> Statement;
	    AssignStatement --> Statement;
	}

}

