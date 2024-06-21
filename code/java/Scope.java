public interface Scope<IDENTIFIER> extends Type {

  TypingEnvironment<IDENTIFIER> getTypingEnvironment();

  IDENTIFIER identifierFromToken(Token token);

  void setParent(Scope<IDENTIFIER> parent);

  Optional<Scope<IDENTIFIER>> getParent();

  default void applyBinding(IDENTIFIER variable, SymbolTableEntry entry) {
    getTypingEnvironment().bindTypeToIdentifier(variable, entry);
  }

  default Stream<SymbolTableEntry> streamSymbolTableEntries() {
    return getTypingEnvironment().stream()
        .map(Map.Entry::getValue)
        .flatMap(EntryTypeBinder::stream);
  }

  default InferenceResult inferFromSignature(Token token, Signature signature) {
    return new StreamInferenceResult(
      token, signature, streamInternalTypes(identifierFromToken(token), signature)
    ).or(streamExternalVisible(identifierFromToken(token), signature));
  }

  @Override
  default boolean isAssignableFrom(Type other, Variance variance) {
    return switch (variance) {
      case INVARIANT -> this.equals(other);
      case COVARIANT, CONTROVARIANT -> false;
    };
  }

}
