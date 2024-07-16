bundle simplelang.types.Literals {
    slices
        simplelang.types.IntegerType
        simplelang.types.BooleanType
        simplelang.types.StringType

    rename {
        Integer --> Literal;
        Boolean --> Literal;
        String --> Literal;
    }
}