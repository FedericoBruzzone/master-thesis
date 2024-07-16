package neverlang.core.typesystem;

import java.util.List;
import java.util.function.Supplier;

public interface Type {
  String id();

  /**
   * Determines if the type represented by this object:
   *
   * <ul>
   *   <li>INVARIANT is exactly the same as the other represented by {@code Type}
   *   <li>COVARIANT is a subtype or the same type of the other represented by {@code Type}
   *   <li>CONTRAVARIANT is a supertype or the same the of the other represented by {@code Type}
   * </ul>
   *
   * @param other the other type
   * @param variance the variance
   * @return true if the type represented by this object is assignable from the other type with
   *     the given variance
   */
  default boolean isAssignableFrom(Type other, Variance variance) {
    return false;
  }

  default boolean matchSignature(Signature signature) {
    return false;
  }

  default Type withCompilationHelper(AbstractCompilationHelper helper) {
    return this;
  }

  default Type withSymbolTableEntryFactory(
      Class<? extends neverlang.core.typesystem.SymbolTableEntryFactory<?, ?>>
          symbolTableEntryFactory) {
    return this;
  }

  default Type withSymbolTableEntryFactory(
      Supplier<SymbolTableEntryFactory<?, ?>> symbolTableEntryFactorySupplier) {
    return this;
  }

  default Type bind(List<Type> neededTypes) {
    return this;
  }
}
