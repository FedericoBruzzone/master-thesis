@TypeAnnotation(TypeEnum.CUSTOM_TYPE)
@TypeLangAnnotation(
    //This is exactly the keyword used in typelang to define this type
    keyword = "customtype",
    //This is the name of the type in typelang
    kind = TypeSystemKind.TYPE
)
public class CustomType implements Type {
    // Other code
}
