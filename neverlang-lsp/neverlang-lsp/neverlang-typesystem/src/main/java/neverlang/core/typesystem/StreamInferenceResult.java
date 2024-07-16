package neverlang.core.typesystem;

import java.util.function.Supplier;
import java.util.stream.Stream;
import neverlang.core.typesystem.symbols.Token;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record StreamInferenceResult(
    Token token, Signature signature, Stream<SymbolTableEntry> stream) implements InferenceResult {

  public StreamInferenceResult or(@NotNull Stream<SymbolTableEntry> typeStream) {
    return new StreamInferenceResult(token, signature, Stream.concat(stream, typeStream));
  }

  @Override
  public InferenceResult or(@Nullable Supplier<InferenceResult> supplier) {
    if (supplier == null) return this;
    var inferenceResult = supplier.get();
    if (inferenceResult == null) return this;
    else if (inferenceResult.token() != token() || inferenceResult.signature() != signature())
      throw new IllegalArgumentException("InferenceResult must have the same token and signature");
    else
      return new StreamInferenceResult(
          token, signature, Stream.concat(stream, inferenceResult.stream()));
  }
}
