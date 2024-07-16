package neverlang.core.typesystem;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import neverlang.core.typesystem.symbols.Token;
import neverlang.core.typesystem.typenv.EntryTypeBinder;
import neverlang.core.typesystem.typenv.TypingEnvironment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Scope<IDENTIFIER> extends Type {
  @NotNull
  TypingEnvironment<IDENTIFIER> getTypingEnvironment();

  void setParent(Scope<IDENTIFIER> parent);

  Optional<Scope<IDENTIFIER>> getParent();

  @Override
  default boolean isAssignableFrom(Type other, Variance variance) {
    return switch (variance) {
      case INVARIANT -> this.equals(other);
      case COVARIANT, CONTROVARIANT -> false;
    };
  }

  default void bindTypeToIdentifier(@Nullable IDENTIFIER variable, @NotNull SymbolTableEntry entry)
      throws UnbindableEntryException {
    if (isBindable(entry)) {
      applyBinding(variable, entry);
    } else {
      throw new UnbindableEntryException(
          "Unable to bind type " + entry + " to identifier " + variable, entry);
    }
  }

  default Stream<SymbolTableEntry> streamSymbolTableEntries() {
    return getTypingEnvironment().stream()
        .map(Map.Entry::getValue)
        .flatMap(EntryTypeBinder::stream);
  }

  default Stream<Map.Entry<IDENTIFIER, SymbolTableEntry>> streamIDSymbolTableEntries() {
    return getTypingEnvironment().stream().flatMap(e -> e.getValue().stream()
        .map(symbolTableEntry -> Map.entry(e.getKey(), symbolTableEntry)));
  }

  default Stream<Map.Entry<IDENTIFIER, SymbolTableEntry>> streamAllEntries() {
    return Scope.this.streamIDSymbolTableEntries().flatMap(e -> {
      if (e.getValue().type() instanceof Scope scope) {
        return Stream.concat(Stream.of(e), scope.streamAllEntries());
      } else {
        return Stream.of(e);
      }
    });
  }

  default void applyBinding(IDENTIFIER variable, SymbolTableEntry entry) {
    getTypingEnvironment().bindTypeToIdentifier(variable, entry);
  }

  IDENTIFIER identifierFromToken(Token token);

  default boolean isBindable(SymbolTableEntry entry) {
    return true;
  }

  default Stream<SymbolTableEntry> streamEntries(IDENTIFIER id) {
    return getTypingEnvironment().getTypesBoundedWith(id);
  }

  default Stream<SymbolTableEntry> streamInternalTypes(IDENTIFIER id, Signature signature) {
    return streamEntries(id).filter(e -> e.entryType().matchSignature(signature));
  }

  default InferenceResult inferFromSignature(Token token, Signature signature) {
    return new StreamInferenceResult(
            token, signature, streamInternalTypes(identifierFromToken(token), signature))
        .or(streamExternalVisible(identifierFromToken(token), signature));
  }

  @NotNull
  default Stream<SymbolTableEntry> streamExternalVisible(IDENTIFIER id, Signature signature) {
    return getTypingEnvironment().stream()
        .map(Map.Entry::getValue)
        .flatMap(EntryTypeBinder::stream)
        .filter(e -> e.type() instanceof ExternalVisible<?>)
        .flatMap(e -> ((ExternalVisible<IDENTIFIER>) e.type()).streamExportedTypes(id, signature));
  }

  default void removeEntriesWithOwnerHashcode(int hashcode) {
    getTypingEnvironment()
        .removeIf(symbolTableEntry -> symbolTableEntry.ownerHashcode() == hashcode);
  }

  default void importInScope(IDENTIFIER id, SymbolTableEntry entry) {
    if (ExternalVisible.class.isAssignableFrom(entry.type().getClass())) {
      bindTypeToIdentifier(id, entry);
    } else {
      throw new RuntimeException("Cannot import " + entry.type() + " in scope");
    }
  }
}
