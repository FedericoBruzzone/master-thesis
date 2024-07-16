package neverlang.lsp;

import static org.assertj.core.api.Assertions.assertThat;

import neverlang.core.lsp.defaults.DefaultSemanticToken;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class SemanticTokenCapabilityTest {

  @Disabled
  @Test
  public void testSemanticTokens() {
    var docs = new DefaultSemanticToken("simplelang.typesystem.types");
    assertThat(docs.semanticTokenList()).hasSize(4);
  }
}
