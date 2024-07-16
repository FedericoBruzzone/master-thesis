package neverlang.core.typesystem.defaults;

import java.util.function.Predicate;
import java.util.stream.Stream;
import neverlang.core.typesystem.graph.DependencyGraph;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;

public class DefaultDependencyGraph<T> implements DependencyGraph<T, Relation> {

  private Graph<T, Relation> graph = new DefaultDirectedGraph<>(Relation.class);

  public Graph<T, Relation> getInnerGraph() {
    return graph;
  }

  @Override
  public boolean addVertex(T vertex) {
    return graph.addVertex(vertex);
  }

  @Override
  public boolean addRelation(T source, T target, Relation relation) {
    return graph.addEdge(source, target, relation);
  }

  @Override
  public Stream<T> outgoingEdgesOf(T source, Predicate<Relation> predicate) {
    return graph.outgoingEdgesOf(source).stream().filter(predicate).map(graph::getEdgeTarget);
  }

  @Override
  public Stream<T> incomingEdgesOf(T target, Predicate<Relation> predicate) {
    return graph.incomingEdgesOf(target).stream().filter(predicate).map(graph::getEdgeSource);
  }

  @Override
  public void removeAll() {
    graph = new DefaultDirectedGraph<>(Relation.class);
  }

  @Override
  public void removeAll(Stream<T> stream) {
    graph.removeAllVertices(stream.toList());
  }
}
