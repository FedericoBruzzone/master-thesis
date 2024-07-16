package neverlang.core.typesystem.graph;

import java.util.function.Predicate;
import java.util.stream.Stream;

public interface DependencyGraph<VERTEX, EDGE> {
  boolean addVertex(VERTEX vertex);

  boolean addRelation(VERTEX source, VERTEX target, EDGE relation);

  Stream<VERTEX> outgoingEdgesOf(VERTEX source, Predicate<EDGE> relation);

  Stream<VERTEX> incomingEdgesOf(VERTEX target, Predicate<EDGE> relation);

  void removeAll();

  void removeAll(Stream<VERTEX> stream);
}
