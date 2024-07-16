package neverlanglsp.astvisualizer.cytoscape;

import java.util.List;

public record Elements(List<CytoscapeElement> nodes, List<CytoscapeElement> edges) {
  public static Elements fromTreeElements(List<Node> nodes, List<Edge> edges) {
    return new Elements(
        nodes.stream().map(CytoscapeElement::new).toList(),
        edges.stream().map(CytoscapeElement::new).toList());
  }
}
