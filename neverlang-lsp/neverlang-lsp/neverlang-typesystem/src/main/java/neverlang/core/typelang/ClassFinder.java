package neverlang.core.typelang;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.jar.JarFile;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import java.util.zip.ZipEntry;

public class ClassFinder {

  private static final String fileSeparator = System.getProperty("file.separator");
  private static final String classExt = ".class";

  public static Stream<Class<?>> findAll(String... packageNames) {
    return Arrays.stream(packageNames).flatMap(ClassFinder::findAll);
  }

  public static Stream<Class<?>> findAll(String packageName) {
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    String path = packageName.replace(".", fileSeparator);
    return getClassNamesInPackage(path, classLoader)
        .map(e -> e.replace(fileSeparator, "."))
        .<Class<?>>map(e -> {
          try {
            return Class.forName(e);
          } catch (ClassNotFoundException ex) {
            return null;
          }
        })
        .filter(Objects::nonNull);
  }

  private static Stream<String> getClassNamesInPackage(
      String packagePath, ClassLoader classLoader) {
    return getResourcesStream(packagePath, classLoader).flatMap(url -> switch (url.getProtocol()) {
      case "file" -> getClassNameInFileSystem(url.getFile(), packagePath);
      case "jar" -> getClassNameInJar(url.getFile(), packagePath);
      default -> Stream.empty();
    });
  }

  private static <T> Stream<T> convertIteratorToStream(Enumeration<T> enumeration) {
    return StreamSupport.stream(
        Spliterators.spliteratorUnknownSize(enumeration.asIterator(), Spliterator.ORDERED), false);
  }

  private static Stream<URL> getResourcesStream(String packagePath, ClassLoader classLoader) {
    try {
      return convertIteratorToStream(classLoader.getResources(packagePath));
    } catch (IOException e) {
      return Stream.empty();
    }
  }

  private static Stream<String> getClassNameInFileSystem(String fileName, String packagePath) {
    try (var stream = Files.walk(Paths.get(fileName), 16)) {
      return stream
          .map(Path::toString)
          .filter(path -> path.endsWith(classExt))
          .map(path -> path.replace(fileName, ""))
          .map(path -> path.replace(classExt, ""))
          .map(path -> Path.of(packagePath, path).toString())
          .toList()
          .stream();
    } catch (IOException e) {
      e.printStackTrace();
      return Stream.empty();
    }
  }

  private static Stream<String> getClassNameInJar(String jarPath, String packagePath) {
    String jarFile = jarPath.split("!")[0].substring("file:".length());
    try {
      JarFile jar = new JarFile(jarFile);
      var res = convertIteratorToStream(jar.entries())
          .map(ZipEntry::getName)
          .filter(entry -> entry.startsWith(packagePath) && entry.endsWith(classExt))
          .map(entry -> entry.replace(fileSeparator, ".").replace(classExt, ""))
          .toList()
          .stream();
      jar.close();
      return res;
    } catch (IOException e) {
      e.printStackTrace();
      return Stream.empty();
    }
  }
}
