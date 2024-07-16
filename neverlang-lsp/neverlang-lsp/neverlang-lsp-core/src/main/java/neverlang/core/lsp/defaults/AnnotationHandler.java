package neverlang.core.lsp.defaults;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import neverlang.core.typelang.ClassFinder;
import neverlang.core.typelang.annotations.TypeLangAnnotation;
import neverlang.core.typelang.annotations.TypeSystemKind;
import neverlang.core.typesystem.SymbolTableEntry;
import neverlang.core.typesystem.graph.IndexNode;

public class AnnotationHandler {

  static Stream<Map.Entry<Class<?>, String>> findAnnotationInMethod(
      String packageName,
      Class<? extends Annotation> annotation,
      Class<?>[] parametersType,
      Class<?> returnType) {
    return ClassFinder.findAll(packageName)
        .filter(e -> e.isAnnotationPresent(TypeLangAnnotation.class)
            && e.getAnnotation(TypeLangAnnotation.class).kind().equals(TypeSystemKind.TYPE))
        .map(Class::getDeclaredMethods)
        .flatMap(Arrays::stream)
        .filter(e -> e.isAnnotationPresent(annotation)
            && e.getReturnType().isAssignableFrom(returnType)
            && e.getParameterCount() == parametersType.length
            && IntStream.range(0, parametersType.length)
                .allMatch(i -> e.getParameterTypes()[i].isAssignableFrom(parametersType[i])))
        .map(e -> Map.entry(e.getDeclaringClass(), e.getName()));
  }

  static <T> Optional<T> invokeMethod(IndexNode indexNode, Map<Class<?>, String> map) {
    if (indexNode.index() instanceof SymbolTableEntry symbolTableEntry) {
      var cls = symbolTableEntry.type().getClass();
      return Optional.ofNullable(map.get(cls)).map(methodName -> {
        try {
          var method = cls.getMethod(methodName, SymbolTableEntry.class);
          return (T) method.invoke(symbolTableEntry.type(), symbolTableEntry);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
          return null;
        }
      });
    } else {
      return Optional.empty();
    }
  }
}
