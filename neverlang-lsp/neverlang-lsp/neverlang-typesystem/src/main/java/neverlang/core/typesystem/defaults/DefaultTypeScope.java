package neverlang.core.typesystem.defaults;

import java.util.Optional;
import neverlang.core.typesystem.Scope;
import neverlang.core.typesystem.symbols.Token;
import neverlang.core.typesystem.typenv.EntryTypeBinder;
import neverlang.core.typesystem.typenv.TypingEnvironment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class DefaultTypeScope implements Scope<String> {

  @Nullable
  private TypingEnvironment<String> typingEnvironment;

  private Scope<String> parent;

  @Override
  public Optional<Scope<String>> getParent() {
    return Optional.ofNullable(parent);
  }

  @Override
  public void setParent(@NotNull Scope<String> parent) {
    if (this.parent != null && !this.parent.equals(parent))
      throw new IllegalArgumentException("Parent scope cannot be set");
    this.parent = parent;
  }

  @Override
  public TypingEnvironment<String> getTypingEnvironment() {
    if (typingEnvironment == null) {
      this.typingEnvironment =
          new TypingEnvironment.Builder<String>().setTypeBinder(getTypeBinder()).build();
    }
    return typingEnvironment;
  }

  public abstract Class<? extends EntryTypeBinder> getTypeBinder();

  @Override
  public String identifierFromToken(Token token) {
    return token.text();
  }
}
