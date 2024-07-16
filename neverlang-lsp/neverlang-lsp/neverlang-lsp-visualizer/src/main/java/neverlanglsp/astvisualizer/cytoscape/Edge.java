package neverlanglsp.astvisualizer.cytoscape;

import java.util.UUID;

public record Edge(String id, String source, String target, int nPos) implements TreeElement {

  public Edge(String source, String target, int pos) {
    this(UUID.randomUUID().toString(), source, target, pos);
  }

  public Edge(Node source, Node target, int pos) {
    this(source.id(), target.id(), pos);
  }
}
