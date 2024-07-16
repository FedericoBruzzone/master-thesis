module neverlang.core.typelang.types.StringTemplate {
	reference syntax from neverlang.commons.StringType

	role(translate) {
		0 <template> .{{{#0.matches.group(1)}}}.
	}
}


slice neverlang.core.typelang.types.StringType {
    concrete syntax from neverlang.commons.StringType
    module neverlang.core.typelang.types.StringTemplate with role translate
}