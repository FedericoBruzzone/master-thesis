package neverlang.core.typesystem.defaults;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import neverlang.core.typesystem.compiler.Compiler;
import neverlang.core.typesystem.compiler.Source;
import neverlang.core.typesystem.compiler.SourceEventCase;
import neverlang.core.typesystem.compiler.SourceSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DefaultSourceSet implements SourceSet {
  ConcurrentHashMap<Path, Source> map = new ConcurrentHashMap<>();

  public DefaultSourceSet(@NotNull List<Source> sourceList) {
    sourceList.forEach(e -> map.put(e.path(), e));
  }

  public void update(@NotNull Path p, @Nullable SourceEventCase sourceEventCase) {
    Compiler.logger.info("Updating source set with " + p + " and " + sourceEventCase);
    if (sourceEventCase == null) return;
    switch (sourceEventCase) {
      case MODIFIED -> {
        map.computeIfAbsent(p, DefaultSource::new);
        map.computeIfPresent(p, (k, v) -> new DefaultSource(p, null, true));
      }
      case CREATED -> map.put(p, new DefaultSource(p));
      case REMOVED -> map.remove(p);
    }
  }

  @Override
  public Stream<Source> getClonedStream() {
    var stream = map.values().stream();
    // TODO: Find a better way to do this
    // Update a new map with the same values, but with the dirty flag set to false
    map = map.entrySet().stream()
        .map(e -> Map.entry(
            e.getKey(), new DefaultSource(e.getValue().path(), e.getValue().source(), false)))
        .collect(Collectors.toMap(
            Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a, ConcurrentHashMap::new));
    return stream;
  }

  public static class Builder {
    private final Predicate<Path> predicate;

    public Builder(Predicate<Path> predicate) {
      this.predicate = predicate;
    }

    public Builder(String extension) {
      this.predicate = e -> e.toString().endsWith(extension);
    }

    public DefaultSourceSet buildFromRootDir(Path root) {
      return new DefaultSourceSet(sourceStreamFromDir(root).toList());
    }

    public DefaultSourceSet buildFromMultipleRoots(Path... multipleRoots) {
      return new DefaultSourceSet(Arrays.stream(multipleRoots)
          .sequential()
          .filter(Objects::nonNull)
          .flatMap(this::sourceStreamFromDir)
          .toList());
    }

    private Stream<Source> sourceStreamFromDir(Path root) {
      try (var stream = Files.walk(root)) {
        return sourceStream(stream).toList().stream();
      } catch (IOException e) {
        return Stream.empty();
      }
    }

    private Stream<Source> sourceStream(Stream<Path> filePaths) {
      return filePaths
          .filter(Files::isRegularFile)
          .filter(predicate)
          .map(DefaultSource::new)
          .map(e -> (Source) e);
    }

    public DefaultSourceSet buildFromPathStream(Stream<Path> pathList) {
      return new DefaultSourceSet(sourceStream(pathList).toList());
    }
  }
}
