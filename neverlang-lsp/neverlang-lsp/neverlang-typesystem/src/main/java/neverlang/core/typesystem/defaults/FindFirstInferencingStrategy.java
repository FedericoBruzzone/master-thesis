package neverlang.core.typesystem.defaults;

import neverlang.core.typesystem.InferenceException;
import neverlang.core.typesystem.InferenceResult;
import neverlang.core.typesystem.InferencingStrategy;
import neverlang.core.typesystem.SymbolTableEntry;

public class FindFirstInferencingStrategy implements InferencingStrategy {

  @Override
  public SymbolTableEntry infer(InferenceResult inferenceResult) {
    return inferenceResult.stream()
        .findFirst()
        .orElseThrow(() -> new InferenceException(
            "No type found for " + inferenceResult.token().text(), inferenceResult));
  }
}
