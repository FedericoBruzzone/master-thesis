package simplelang;

import java.io.IOException;
import java.io.Writer;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import neverlang.core.typesystem.defaults.DefaultDependencyGraph;
import neverlang.core.typesystem.defaults.Relation;
import neverlang.core.typesystem.graph.LSPGraph;
import neverlang.core.typesystem.typenv.EntryType;
import org.jgrapht.nio.Attribute;
import org.jgrapht.nio.AttributeType;
import org.jgrapht.nio.DefaultAttribute;
import org.jgrapht.nio.dot.DOTExporter;

public class Utils {

  public static void updateVersion(Path tempDir, String base, int i) {
    updateVersion(tempDir, base, i, path -> false);
  }

  public static void updateVersion(Path tempDir, String base, int i, Predicate<Path> filter) {
    var sep = System.getProperty("file.separator");
    var url = Utils.class.getResource(sep + Path.of(base, "v" + i));
    try {
      var source = Path.of(Objects.requireNonNull(url).toURI());
      // delete directory even is not empty
      try (var stream = Files.walk(tempDir)) {
        stream.sorted(Comparator.reverseOrder()).filter(filter).forEach(path -> {
          try {
            Files.delete(path);
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        });
      }
      // copy all the file in source to the tempDir
      try (var stream = Files.walk(source)) {
        stream.filter(e -> e.toFile().isFile()).forEach(path -> {
          try {
            Files.copy(
                path,
                tempDir.resolve(source.relativize(path)),
                StandardCopyOption.REPLACE_EXISTING);
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        });
      }
    } catch (URISyntaxException | IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static void dotExporter(LSPGraph graph, Writer writer) {
    var depGraph = graph.getDependencyGraph();
    if (depGraph instanceof DefaultDependencyGraph dependencyGraph) {
      var innerGraph = dependencyGraph.getInnerGraph();
      var exporter = new DOTExporter<EntryType, Relation>(t -> String.valueOf(t.hashCode()));
      exporter.setVertexAttributeProvider(entry -> {
        Map<String, Attribute> map = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append(entry.token().text());
        if (entry.token().location() != null) {
          sb.append("\n").append(entry.token().location().range());
        }
        map.put("label", new DefaultAttribute<>(sb.toString(), AttributeType.STRING));
        return map;
      });
      exporter.setEdgeAttributeProvider(relation -> {
        Map<String, Attribute> map = new HashMap<>();
        map.put("label", new DefaultAttribute<>(relation, AttributeType.STRING));
        return map;
      });
      exporter.exportGraph(innerGraph, writer);
    }

    //        DOTExporter<String, DefaultEdge> exporter2=new DOTExporter<>(v -> v.toString());
    //        writer = new StringWriter();
    //        exporter2.exportGraph(graph, writer);
  }
}
