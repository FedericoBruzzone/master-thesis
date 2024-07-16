package neverlang.core.typesystem.graph;

import java.nio.file.Path;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import neverlang.core.typesystem.symbols.Location;

public class IndexStructure {
  private final ConcurrentHashMap<String, IndexNode> map = new ConcurrentHashMap<>();

  public void addIndex(Indexable index) {
    var uri =
        index.getUri().map(Path::of).map(Path::normalize).map(Path::toString).orElse("$NOFILE");
    map.putIfAbsent(uri, new IndexNode());
    map.computeIfPresent(uri, (_anyKey, value) -> value.add(new IndexNode(index)));
  }

  public Optional<IndexNode> getIndexTree(@Nullable Path path) {
    var key = Optional.ofNullable(path).map(Path::normalize).map(Path::toString).orElse("$NOFILE");
    return Optional.ofNullable(map.get(key));
  }

  public Stream<Indexable> streamTopLevel() {
    return map.values().stream().flatMap(IndexNode::streamTopLevel);
  }

  public void removeAll() {
    map.clear();
  }

  public void removeByPath(Path path) {
    map.remove(path.normalize().toString());
  }

  public void removeByLocation(Location location) {
    location.normalizedUri().ifPresent(e -> {
      if (location.range() == null) {
        map.remove(e);
      } else {
        Optional.ofNullable(map.get(e))
            .ifPresent(indexNode -> indexNode.removeByLocation(location));
      }
    });
  }
}
