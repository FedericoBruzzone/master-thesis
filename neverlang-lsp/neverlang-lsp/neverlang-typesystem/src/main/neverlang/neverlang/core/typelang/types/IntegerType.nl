module neverlang.core.typelang.types.IntegerTemplate {
    reference syntax from neverlang.commons.types.IntegerSyntax

    role(translate) {
        0 <template> .{{{#0.text}}}.
    }
}

slice neverlang.core.typelang.types.IntegerType {
    concrete syntax from neverlang.commons.types.IntegerSyntax
    module neverlang.core.typelang.types.IntegerTemplate with role translate
}