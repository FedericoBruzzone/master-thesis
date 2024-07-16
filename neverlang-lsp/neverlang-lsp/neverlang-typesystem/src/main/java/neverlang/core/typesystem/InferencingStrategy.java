package neverlang.core.typesystem;

public interface InferencingStrategy {
  SymbolTableEntry infer(InferenceResult inferenceResult);
}
