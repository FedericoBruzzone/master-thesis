package neverlang.core.typesystem;

import java.util.HashMap;

/*
 * This class is used to collect all the types that are needed by all subclasses of BaseType
 *
 * It allows to create a package called "neededtypes" that contains all the types that are needed by the BaseType.
 * In order to make this work, each NeededType should be annotated with the @TypeLangAnnotation annotation.
 *
 * For example:
 * ```
 * @TypeLangAnnotation(
 *     keyword = "int",
 *     kind = TypeSystemKind.NEEDED_TYPE
 * )
 * public class IntNeededType extends TypePrimitive {
 *     public IntNeededType() {
 *         super("int");
 *     }
 * }
 * ```
 */
@Deprecated
public class NeededTypesCollector {

  // The key of the map should be the canonical name of the class that needs the type
  private static final HashMap<String, Type> neededTypes = new HashMap<>();

  public static void collect(Type type) {
    neededTypes.putIfAbsent(type.id(), type);
  }

  public void collect(String name, Type type) {
    neededTypes.putIfAbsent(name, type);
  }

  public HashMap<String, Type> getNeededTypes() {
    return neededTypes;
  }

  public static Type get(String name) {
    if (!neededTypes.containsKey(name)) {
      throw new RuntimeException("Type " + name + " not found");
    }

    return neededTypes.get(name);
  }

  private static void clear() {
    neededTypes.clear();
  }
}
