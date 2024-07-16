package neverlang.core.typesystem;

import neverlang.core.typesystem.symbols.Location;

public class InferenceException extends NeverlangTypesystemException {

  private final InferenceResult inferenceResult;

  public InferenceException(String message, InferenceResult inferenceResult) {
    super(message);
    this.inferenceResult = inferenceResult;
  }

  public InferenceResult getInferenceResult() {
    return inferenceResult;
  }

  @Override
  public Location location() {
    return inferenceResult.token().location();
  }
}
