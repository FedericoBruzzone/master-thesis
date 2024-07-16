package neverlang.core.typelang;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import neverlang.core.typelang.annotations.*;
import neverlang.core.typesystem.*;
import neverlang.core.typesystem.defaults.CompilationUnit;
import neverlang.core.typesystem.defaults.DefaultSymbolTableEntry;
import neverlang.core.typesystem.defaults.DefaultSymbolTableEntryFactory;
import neverlang.core.typesystem.graph.LSPGraph;
import org.jetbrains.annotations.NotNull;

public class TypeLangGenerator extends TypeMapperModule {

  private final Map<String, String> typesMap = new HashMap<>();
  private final Map<String, String> prioritiesMap = new HashMap<>();
  private final Map<String, String> signatureMap = new HashMap<>();
  private final Map<String, String> details = new HashMap<>();

  private final Map<String, Method> callbacks = new HashMap<>();

  private final Map<String, String> baseTypes = new HashMap<>();

  @NotNull
  private Class<? extends AbstractCompilationUnit<?>> compilationUnitClass = CompilationUnit.class;

  @NotNull
  private Class<? extends SymbolTableEntry> symbolTableEntryClass = DefaultSymbolTableEntry.class;

  @NotNull
  private Class<? extends SymbolTableEntryFactory<?, ?>> symbolTableEntryFactoryClass =
      DefaultSymbolTableEntryFactory.class;

  @NotNull
  private Class<? extends AbstractCompilationHelper<?, ?>> compilationHelperClass =
      CompilationHelper.class;

  @NotNull
  private Class<? extends LSPGraph> dependencyGraphClass = LSPGraph.class;

  // If true the generator will use the TypeLangAnnotation to extract the type name
  // If false the generator will use the NEW annotations that implement the
  // TypeSystemKindAnnotation
  public Boolean useTypeLangAnnotation() {
    return true;
  }

  public TypeLangGenerator initPackage(String packageName, String... labels) {
    if (useTypeLangAnnotation()) {
      findAnnotationsInPackage(packageName, labels);
    }
    findTypeSystemKindAnnotationInPackage(packageName);
    return this;
  }

  public TypeLangGenerator initPackage(Stream<String> packageNames) {
    var packageNamesArray = packageNames.toArray(String[]::new);
    if (useTypeLangAnnotation()) {
      findAnnotationsInPackage(packageNamesArray);
    }
    findTypeSystemKindAnnotationsInPackage(packageNamesArray);

    return this;
  }

  public static Optional<Keyword> extractFrom(Class<?> from, TypeSystemKind kind) {
    return Arrays.stream(from.getAnnotations())
        .filter(a -> a.annotationType().isAnnotationPresent(TypeSystemKindAnnotation.class))
        .filter(a ->
            a.annotationType().getAnnotation(TypeSystemKindAnnotation.class).value() == kind)
        .map(a -> {
          try {
            return (Keyword) a.annotationType().getDeclaredMethod("value").invoke(a);
          } catch (Exception e) {
            return null;
          }
        })
        .filter(Objects::nonNull)
        .findFirst();
  }

  private static TypeSystemKind getKind(Class<?> cls) {
    return Arrays.stream(cls.getAnnotations())
        .filter(a -> a.annotationType().isAnnotationPresent(TypeSystemKindAnnotation.class))
        .map(a ->
            a.annotationType().getAnnotation(TypeSystemKindAnnotation.class).value())
        .findFirst()
        .orElseThrow();
  }

  private void findTypeSystemKindAnnotationsInPackage(String[] packageName) {
    Arrays.stream(packageName).forEach(this::findTypeSystemKindAnnotationInPackage);
  }

  private void findTypeSystemKindAnnotationInPackage(String packageName) {
    ClassFinder.findAll(packageName)
        .filter(cls -> Arrays.stream(cls.getAnnotations())
            .anyMatch(a -> a.annotationType().isAnnotationPresent(TypeSystemKindAnnotation.class)))
        .map(e -> Map.entry(e, getKind(e)))
        .forEach(e -> {
          switch (e.getValue()) {
            case PRIORITY -> extractFromEnumOrClassPriority(e.getKey(), prioritiesMap);
            case TYPE -> {
              var id = extractFrom(e.getKey(), TypeSystemKind.TYPE);
              id.ifPresent(
                  keyword -> typesMap.put(keyword.keyword(), e.getKey().getCanonicalName()));
              Arrays.stream(e.getKey().getDeclaredMethods())
                  .filter(f -> f.isAnnotationPresent(Callback.class))
                  .forEach(f -> {
                    Callback callback = f.getAnnotation(Callback.class);
                    callbacks.put(callback.keyword(), f);
                  });
            }
            case SIGNATURE -> {
              var id = extractFrom(e.getKey(), TypeSystemKind.SIGNATURE);
              id.ifPresent(
                  keyword -> signatureMap.put(keyword.keyword(), e.getKey().getCanonicalName()));
            }
            case COMPILATION_HELPER -> compilationHelperClass =
                (Class<? extends AbstractCompilationHelper<?, ?>>) e.getKey();
          }
        });
  }

  // Use the TypeLangAnnotation
  private void findAnnotationsInPackage(String[] packageNames) {
    Arrays.stream(packageNames).forEach(e -> findAnnotationsInPackage(e, new String[] {}));
  }

  // Use the TypeLangAnnotation
  private void findAnnotationsInPackage(String packageName, String[] labels) {
    Set<String> labelSet = Arrays.stream(labels).collect(Collectors.toSet());
    ClassFinder.findAll(packageName)
        .filter(e -> e.isAnnotationPresent(TypeLangAnnotation.class))
        .map(e -> Map.entry(e, e.getAnnotation(TypeLangAnnotation.class)))
        // .filter(e -> labelSet.contains(e.getValue().label()))
        .forEach(e -> {
          switch (e.getValue().kind()) {
              //            case TYPE -> {
              //              var id = e.getValue().keyword();
              //              if (!id.isEmpty() && !id.isBlank()) {
              //                typesMap.put(e.getValue().keyword(),
              // e.getKey().getSimpleName());
              //              }
              //              Arrays.stream(e.getKey().getDeclaredMethods())
              //                  .filter(f ->
              // f.isAnnotationPresent(Callback.class))
              //                  .forEach(f -> {
              //                    Callback callback =
              // f.getAnnotation(Callback.class);
              //                    callbacks.put(callback.keyword(), f);
              //                  });
              //            }
              // case PRIORITY -> extractFromEnumOrClass(e.getKey(),
              // prioritiesMap);
              // case SIGNATURE -> signatureMap.put(
              //    e.getValue().keyword(), e.getKey().getSimpleName());
              // case COMPILATION_HELPER -> compilationHelperClass =
              //    (Class<? extends AbstractCompilationHelper<?, ?>>)
              // e.getKey();
            case COMPILATION_UNIT -> compilationUnitClass =
                (Class<? extends AbstractCompilationUnit<?>>) e.getKey();
            case DEPENDENCY_GRAPH -> dependencyGraphClass = (Class<? extends LSPGraph>) e.getKey();
            case SYMBOL_TABLE_ENTRY -> symbolTableEntryClass =
                (Class<? extends SymbolTableEntry>) e.getKey();
            case SYMBOL_TABLE_ENTRY_FACTORY -> {
              symbolTableEntryFactoryClass =
                  (Class<? extends SymbolTableEntryFactory<?, ?>>) e.getKey();

              Stream.concat(
                      Arrays.stream(e.getKey().getDeclaredMethods()),
                      Arrays.stream(e.getKey().getSuperclass().getDeclaredMethods()))
                  .filter(f -> f.isAnnotationPresent(TypeDetail.class))
                  .forEach(f -> {
                    TypeDetail detail = f.getAnnotation(TypeDetail.class);
                    details.put(detail.id(), f.getName());
                  });
            }
            case NEEDED_TYPE -> {
              try {
                Class<? extends Type> cls = (Class<? extends Type>) e.getKey();
                Type type = cls.getConstructor().newInstance();
                NeededTypesCollector.collect(type);
              } catch (InstantiationException
                  | IllegalAccessException
                  | InvocationTargetException
                  | NoSuchMethodException ex) {
                throw new RuntimeException(ex);
              }
            }
            case BASE_TYPE -> {
              baseTypes.put(e.getValue().keyword(), e.getKey().getCanonicalName());
            }
          }
        });
  }

  public static void extractFromEnumOrClassPriority(Class<?> cls, Map<String, String> m) {
    if (cls.isEnum()) {
      extractFromEnumPriority(cls, m);
    } else {
      extractFromClassPriority(cls, m);
    }
  }

  public static void extractFromClassPriority(Class<?> cls, Map<String, String> m) {
    //        m.put(cls.getSimpleName().toLowerCase(),
    // String.format("Priority.retrive($ctx.getLang(), %s.class)", cls.getSimpleName()));
    m.put(
        cls.getSimpleName().toLowerCase(),
        String.format("Priority.retrive($ctx.getLang(), %s.class)", cls.getCanonicalName()));
  }

  public static void extractFromEnumPriority(Class<?> cls, Map<String, String> m) {
    if (cls.isEnum()) {
      Arrays.stream(cls.getDeclaredFields())
          .filter(Field::isEnumConstant)
          .forEach(e -> m.put(
              e.getName().toLowerCase(), String.format("%s.%s", cls.getSimpleName(), e.getName())));
    }
  }

  @Override
  public Class<? extends AbstractCompilationUnit<?>> compilationUnit() {
    return compilationUnitClass;
  }

  @Override
  public Class<? extends SymbolTableEntry> symbolTableEntry() {
    return symbolTableEntryClass;
  }

  @Override
  public Class<? extends SymbolTableEntryFactory<?, ?>> symbolTableEntryFactory() {
    return symbolTableEntryFactoryClass;
  }

  @Override
  public Class<? extends AbstractCompilationHelper<?, ?>> compilationHelper() {
    return compilationHelperClass;
  }

  @Override
  public Map<String, String> types() {
    return typesMap;
  }

  @Override
  public Map<String, String> priorities() {
    return prioritiesMap;
  }

  @Override
  public Map<String, String> details() {
    return details;
  }

  @Override
  public Map<String, Method> callbacks() {
    return callbacks;
  }

  @Override
  public Class<? extends LSPGraph> lspGraph() {
    return dependencyGraphClass;
  }

  @Override
  public Map<String, String> signatures() {
    return signatureMap;
  }

  @Override
  public Map<String, String> baseTypes() {
    return baseTypes;
  }
}
