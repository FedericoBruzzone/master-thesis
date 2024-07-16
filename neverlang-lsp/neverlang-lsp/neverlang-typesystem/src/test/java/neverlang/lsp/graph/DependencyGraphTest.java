package neverlang.lsp.graph;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import neverlang.core.typesystem.defaults.DefaultDependencyGraph;
import neverlang.core.typesystem.defaults.Usage;
import neverlang.core.typesystem.symbols.Token;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled
public class DependencyGraphTest {
  private final DefaultDependencyGraph<Token> dependencyGraphTest =
      new DefaultDependencyGraph<Token>();

  @Test
  public void test() {
    var m1 = mock(Token.class);
    var m2 = mock(Token.class);
    var m3 = mock(Token.class);
    dependencyGraphTest.addVertex(m1);
    dependencyGraphTest.addVertex(m2);
    dependencyGraphTest.addVertex(m3);
    dependencyGraphTest.addRelation(m2, m1, new Usage());
    dependencyGraphTest.addRelation(m3, m1, new Usage());
    assertThat(dependencyGraphTest.incomingEdgesOf(m1, e -> e instanceof Usage).toList())
        .containsExactlyInAnyOrder(m2, m3);
  }
}
