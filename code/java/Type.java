public interface Type {

  String id();

  default boolean isAssignableFrom(Type other, Variance variance) {
    return false;
  }

  default boolean matchSignature(Signature signature) {
    return false;
  }

  default Type bind(List<Type> neededTypes) {
    return this;
  }

}
