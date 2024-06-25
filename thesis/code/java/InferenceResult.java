public interface InferenceResult {
  Stream<SymbolTableEntry> stream();

  InferenceResult or(Supplier<InferenceResult> supplier);

  Token token();

  Signature signature();
}
