package neverlang.core.typesystem;

import java.util.function.Supplier;
import java.util.stream.Stream;
import neverlang.core.typesystem.symbols.Token;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface InferenceResult {
  Stream<SymbolTableEntry> stream();

  InferenceResult or(@Nullable Supplier<InferenceResult> supplier);

  @NotNull
  Token token();

  @NotNull
  Signature signature();
}
