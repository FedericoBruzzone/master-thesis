package neverlang.core.typesystem.graph;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Stream;
import neverlang.core.typesystem.symbols.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record IndexNode(
    @Nullable Indexable index, @NotNull AtomicReference<TreeSet<IndexNode>> _children)
    implements Comparable<IndexNode> {

  public IndexNode() {
    this(null);
  }

  public IndexNode(@Nullable Indexable index) {
    this(index, new AtomicReference<>(null));
  }

  public <T extends Indexable> Optional<T> lookup(int row, int col) {
    if (index == null) {
      return lookupChildren(row, col);
    } else if (index.isExactlyThis(row, col)) {
      return Optional.of((T) index);
    } else if (index.isInsideThis(row, col)) {
      return lookupChildren(row, col);
    } else {
      return Optional.empty();
    }
  }

  // TODO: this method should be tested
  public Stream<Indexable> streamTopLevel() {
    return children().stream().map(IndexNode::index);
  }

  @NotNull
  private <T extends Indexable> Optional<T> lookupChildren(int row, int col) {
    return children().stream()
        .map(e -> e.lookup(row, col))
        .filter(Optional::isPresent)
        .findFirst()
        .flatMap(Function.identity())
        .map(e -> (T) e);
  }

  public Stream<IndexNode> streamAll() {
    return Stream.concat(Stream.of(this), children().stream().flatMap(IndexNode::streamAll));
  }

  public TreeSet<IndexNode> children() {
    _children.compareAndSet(null, new TreeSet<>());
    return _children.get();
  }

  public IndexNode add(IndexNode node) {
    AtomicInteger counter = new AtomicInteger(0);
    AtomicBoolean isAdd = new AtomicBoolean(false);
    children().stream()
        .peek(_e -> counter.incrementAndGet())
        .filter(e -> !isAdd.get())
        .filter(node::isContainedIn)
        .forEachOrdered(e -> {
          isAdd.set(true);
          e.add(node);
        });
    if (!isAdd.get()) {
      children().stream().filter(e -> e.isContainedIn(node)).forEach(node::add);
      children().removeIf(e -> e.isContainedIn(node));
      children().add(node);
    }
    return this;
  }

  public boolean isContainedIn(IndexNode indexNode) {
    return indexNode.index != null && indexNode.index.isInsideThis(index);
  }

  @Override
  public String toString() {
    return index == null ? "root" : index.toString();
  }

  @Override
  public int compareTo(@NotNull IndexNode o) {
    if (index == null && o.index == null) {
      return 0;
    } else if (index == null) {
      return -1;
    } else if (o.index == null) {
      return 1;
    } else {
      return index.compareTo(o.index);
    }
  }

  public boolean isContainedIn(Location location) {
    return index != null && location.range() != null && location.range().contains(index.range());
  }

  public void removeByLocation(Location location) {
    children().removeIf(e -> e.isContainedIn(location));
    children().forEach(e -> e.removeByLocation(location));
  }
}
