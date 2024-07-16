package neverlang.core.typesystem.typenv;

import java.util.concurrent.atomic.AtomicReference;
import neverlang.core.typesystem.Signature;
import neverlang.core.typesystem.Type;
import neverlang.core.typesystem.symbols.Token;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** This represents a symbol on a document */
public interface EntryType {
  @NotNull
  Token token();

  @NotNull
  AtomicReference<Type> refType();

  @Nullable
  default <T extends Type> T type() {
    return (T) refType().get();
  }

  default boolean matchSignature(Signature signature) {
    return type().matchSignature(signature);
  }

  default String tokenString() {
    return token().text();
  }
}
