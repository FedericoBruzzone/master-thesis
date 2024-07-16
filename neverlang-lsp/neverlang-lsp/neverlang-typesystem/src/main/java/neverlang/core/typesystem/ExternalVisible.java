package neverlang.core.typesystem;

import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;

public interface ExternalVisible<IDENTIFIER> extends Scope<IDENTIFIER> {

  @NotNull
  default Stream<SymbolTableEntry> streamExportedTypes(IDENTIFIER id, Signature signature) {
    return Stream.concat(streamInternalTypes(id, signature), streamExternalVisible(id, signature));
  }
}
