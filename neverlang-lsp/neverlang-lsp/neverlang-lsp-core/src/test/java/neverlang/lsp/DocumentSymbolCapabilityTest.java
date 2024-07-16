package neverlang.lsp;

import static org.assertj.core.api.Assertions.assertThat;

import neverlang.core.lsp.defaults.DefaultDocumentSymbol;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class DocumentSymbolCapabilityTest {
  @Disabled
  @Test
  public void testDocumentSymbolsFound() {
    var docs = new DefaultDocumentSymbol("simplelang.typesystem.types");
    assertThat(docs.documentSymbolMap).hasSize(2);
  }
}
