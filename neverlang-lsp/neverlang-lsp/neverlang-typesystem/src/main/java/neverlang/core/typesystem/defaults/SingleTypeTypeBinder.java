package neverlang.core.typesystem.defaults;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;
import neverlang.core.typesystem.AlreadyBoundException;
import neverlang.core.typesystem.SymbolTableEntry;
import neverlang.core.typesystem.typenv.EntryTypeBinder;
import org.jetbrains.annotations.Nullable;

public class SingleTypeTypeBinder implements EntryTypeBinder {

  @Nullable
  private SymbolTableEntry type;

  @Override
  public EntryTypeBinder bindEntry(SymbolTableEntry entryType) {
    if (isBound()) {
      throw new AlreadyBoundException(entryType, type);
    }
    this.type = entryType;
    return this;
  }

  @Override
  public boolean isBound() {
    return type != null;
  }

  @Override
  public Stream<SymbolTableEntry> stream() {
    return Stream.of(type).filter(Objects::nonNull);
  }

  @Override
  public Optional<SymbolTableEntry> getBoundEntry() {
    return Optional.ofNullable(type);
  }

  @Override
  public void removeIf(Predicate<SymbolTableEntry> predicate) {
    if (predicate.test(type)) {
      type = null;
    }
  }
}
