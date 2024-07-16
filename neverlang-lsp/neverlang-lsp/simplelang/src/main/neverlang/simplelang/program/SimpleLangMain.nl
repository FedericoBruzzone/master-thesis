module simplelang.program.SimpleLangMain {

	reference syntax {
		Program: Program <-- Root;
		Root <-- FirstClassStatement*;

        _ <-- /\s/;
        _ <-- /\/\/.*/;

        categories:
            linecomment = { "//" },
            multilineComment = { "/*", "*/" };
	}

	role(type-checker) <typecheck> {
	   0  .{
	        initRoot sources
	   }.

	   2 .{
            define scope file ($file ?? global) [
                run root $3 priority file
            ]
	   }.
	}
}
