package neverlang.lsp.graph;

import static org.assertj.core.api.Assertions.assertThat;

import neverlang.core.typesystem.graph.IndexStructure;
import neverlang.core.typesystem.graph.Indexable;
import neverlang.core.typesystem.symbols.Location;
import neverlang.core.typesystem.symbols.Range;
import org.junit.jupiter.api.Test;

public class IndexStructureTest {

  /*
  REFERENCE TEXT
  prova {
      a
      b
  }
   */

  @Test
  public void testIndexStructure() {
    String p = "prova";
    var pRange = Range.fromText(p, 0, 0);
    var leftRange = Range.fromText("{", 0, p.length() + 1);
    var rightRange = Range.fromText("}", 3, 0);
    var index = new Index(new Location(null, pRange), leftRange.merge(rightRange));
    var a = new Index(new Location(null, Range.fromText("a", 1, 0)), null);
    var b = new Index(new Location(null, Range.fromText("b", 2, 0)), null);
    IndexStructure indexStructure = new IndexStructure();
    indexStructure.addIndex(index);
    indexStructure.addIndex(a);
    indexStructure.addIndex(b);
    var indexNode = indexStructure.getIndexTree(null);
    var res = indexNode.flatMap(e -> e.lookup(0, 1));
    assertThat(res).isPresent();
    assertThat(res.get()).isEqualTo(index);
    var res2 = indexNode.flatMap(e -> e.lookup(0, 10));
    assertThat(res2).isNotPresent();
  }

  record Index(Location location, Range foldingRange) implements Indexable {}
}
