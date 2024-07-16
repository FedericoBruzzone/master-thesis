package neverlang.core.typesystem.graph;

import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Flow;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import neverlang.core.typesystem.AbstractCompilationUnit;
import neverlang.core.typesystem.SymbolTableEntry;
import neverlang.core.typesystem.SymbolTableEntryFactory;
import neverlang.core.typesystem.defaults.DefaultDependencyGraph;
import neverlang.core.typesystem.defaults.Relation;
import neverlang.core.typesystem.defaults.Usage;
import neverlang.core.typesystem.symbols.Location;
import neverlang.core.typesystem.symbols.Token;
import neverlang.core.typesystem.typenv.EntryType;

public class LSPGraph {

  private final DependencyGraph<EntryType, Relation> dependencyGraph;
  private final IndexStructure indexStructure = new IndexStructure();
  // maybe is not useful
  private final Map<EntryType, EntryTypeExtra> map = new ConcurrentHashMap<>();

  public LSPGraph() {
    this(new DefaultDependencyGraph<>());
  }

  public LSPGraph(DependencyGraph<EntryType, Relation> dependencyGraph) {
    this.dependencyGraph = dependencyGraph;
  }

  public DependencyGraph<EntryType, Relation> getDependencyGraph() {
    return dependencyGraph;
  }

  public IndexStructure getIndexStructure() {
    return indexStructure;
  }

  private synchronized void addVertex(EntryTypeExtra typeExtra) {
    if (dependencyGraph.addVertex(typeExtra.entry().entryType())) {
      map.put(typeExtra.entry().entryType(), typeExtra);
    }
    indexStructure.addIndex(typeExtra.entry());
  }

  public synchronized void addDefinition(EntryTypeExtra typeExtra) {
    addVertex(typeExtra);
  }

  public synchronized void addUsage(EntryTypeExtra typeExtra, EntryType to) {
    addVertex(typeExtra);
    dependencyGraph.addRelation(typeExtra.entry().entryType(), to, new Usage());
  }

  public void addImplementation(Token source, SymbolTableEntry target) {
    //        if (map.containsKey(target)) {
    //            dependencyGraph.addRelation(source, map.get(target), new Implementation());
    //        } else {
    //            throw new RuntimeException("Entry type non defined");
    //        }
  }

  public Stream<EntryType> getUsages(SymbolTableEntry source) {
    return dependencyGraph.incomingEdgesOf(source.entryType(), e -> e instanceof Usage);
  }

  public Stream<EntryType> getReferences(SymbolTableEntry source) {
    return Optional.ofNullable(getDefinition(source))
        .map(e -> Stream.concat(Stream.of(e), getUsages(map.get(e).entry())))
        .orElseGet(() -> Stream.concat(Stream.of(source.entryType()), getUsages(source)));
  }

  public EntryType getDefinition(SymbolTableEntry source) {
    return dependencyGraph
        .outgoingEdgesOf(source.entryType(), e -> e instanceof Usage)
        .findFirst()
        .orElse(null);
  }

  private synchronized void removeAll() {
    this.dependencyGraph.removeAll();
    this.indexStructure.removeAll();
    this.map.clear();
  }

  public void removeAll(Collection<EntryType> symbolTableEntries) {
    this.dependencyGraph.removeAll(symbolTableEntries.stream());
    symbolTableEntries.forEach(map::remove);
  }

  public List<AbstractCompilationUnit> clearByPathAndGetReferredCompilationUnit(Set<Path> pathSet) {
    var allSymbolTableEntriesToRemove = map.entrySet().parallelStream()
        .filter(e -> e.getValue().getPath().map(pathSet::contains).orElse(false))
        .collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue));

    var indexStructure = new IndexStructure();

    allSymbolTableEntriesToRemove.values().parallelStream()
        .map(EntryTypeExtra::entry)
        .flatMap(e -> {
          try {
            return getUsages(e);
          } catch (IllegalArgumentException ex) {
            return Stream.empty();
          }
        })
        .filter(e -> !allSymbolTableEntriesToRemove.containsKey(e))
        .map(map::get)
        .filter(Objects::nonNull)
        .map(EntryTypeExtra::compilationUnit)
        .map(AbstractCompilationUnit::recursivelyGoBack)
        .distinct()
        .forEach(indexStructure::addIndex);
    // In this way we pick only top level Compilation Unit
    var list =
        indexStructure.streamTopLevel().map(e -> (AbstractCompilationUnit) e).toList();

    allSymbolTableEntriesToRemove.keySet().forEach(map::remove);
    this.dependencyGraph.removeAll(allSymbolTableEntriesToRemove.keySet().stream());
    pathSet.forEach(this.indexStructure::removeByPath);
    return list;
  }

  public Flow.Subscriber<Object> getSubscriber() {
    return new LSPGraph.Subscriber(this);
  }

  public record EntryTypeExtra(SymbolTableEntry entry, AbstractCompilationUnit<?> compilationUnit) {
    public EntryTypeExtra(SymbolTableEntryFactory<?, ?> factory) {
      this(factory.symbolTableEntry(), factory.getCompilationUnit());
    }

    Optional<Path> getPath() {
      return Optional.ofNullable(entry.location()).map(Location::uri).map(Path::of);
    }
  }

  public static class Subscriber implements Flow.Subscriber<Object> {

    private Flow.Subscription subscription;
    private final LSPGraph graph;

    public Subscriber(LSPGraph graph) {
      this.graph = graph;
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
      this.subscription = subscription;
      subscription.request(1);
    }

    @Override
    public void onNext(Object item) {
      if (item instanceof SymbolTableEntryFactory factory) {
        Optional.ofNullable(factory.token()).ifPresent(token -> {
          switch (factory.entryKind()) {
            case DEFINE -> graph.addDefinition(new EntryTypeExtra(factory));
            case USE -> graph.addUsage(
                new EntryTypeExtra(factory), factory.refSymbolTableEntry().entryType());
          }
        });
      }

      subscription.request(1);
    }

    @Override
    public void onError(Throwable throwable) {
      // TODO: CHECK THIS
      throwable.printStackTrace();
      subscription.request(1);
    }

    @Override
    public void onComplete() {}
  }
}
