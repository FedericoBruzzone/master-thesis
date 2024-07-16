package neverlang.lsp.defaults;

import neverlang.core.typesystem.Signature;
import neverlang.core.typesystem.Type;
import neverlang.core.typesystem.Variance;

public record TypePrimitive(String id) implements Type {

  @Override
  public boolean isAssignableFrom(Type other, Variance variance) {
    switch (variance) {
      case INVARIANT:
        return this.equals(other);
      default:
        return false;
    }
  }

  @Override
  public boolean matchSignature(Signature signature) {
    return signature instanceof VariableSignature;
  }
}
