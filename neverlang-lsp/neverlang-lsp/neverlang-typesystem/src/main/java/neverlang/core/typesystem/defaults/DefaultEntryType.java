package neverlang.core.typesystem.defaults;

import java.util.concurrent.atomic.AtomicReference;
import neverlang.core.typesystem.Type;
import neverlang.core.typesystem.symbols.Token;
import neverlang.core.typesystem.typenv.EntryType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record DefaultEntryType(@Nullable Token token, @NotNull AtomicReference<Type> refType)
    implements EntryType {
  public DefaultEntryType(@Nullable Token token, @NotNull Type type) {
    this(token, new AtomicReference<>(type));
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof DefaultEntryType other) {
      return token.equals(other.token);
    } else {
      return false;
    }
  }

  @Override
  public int hashCode() {
    return token.hashCode();
  }
}
