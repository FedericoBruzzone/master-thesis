package neverlang.core.typesystem.defaults;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;
import neverlang.core.typesystem.SymbolTableEntry;
import neverlang.core.typesystem.typenv.EntryTypeBinder;

public class MultipleTypeTypeBinder implements EntryTypeBinder {
  private SymbolTableEntry singleEntry;
  private List<SymbolTableEntry> multipleEntry;

  @Override
  public MultipleTypeTypeBinder bindEntry(SymbolTableEntry type) {
    if (multipleEntry != null) {
      multipleEntry.add(type);
    } else if (singleEntry != null) {
      // THIS WILL ADD ANY TYPE
      multipleEntry = new ArrayList<>() {
        {
          add(singleEntry);
          add(type);
        }
      };
      // Set single entry to null
      singleEntry = null;
    } else {
      singleEntry = type;
    }
    return this;
  }

  @Override
  public boolean isBound() {
    return !(singleEntry == null || multipleEntry == null || multipleEntry.size() == 0);
  }

  @Override
  public Stream<SymbolTableEntry> stream() {
    return multipleEntry == null ? Stream.of(singleEntry) : multipleEntry.stream();
  }

  @Override
  public Optional<SymbolTableEntry> getBoundEntry() {
    return Optional.of(singleEntry);
  }

  @Override
  public void removeIf(Predicate<SymbolTableEntry> predicate) {
    if (multipleEntry != null) {
      multipleEntry =
          multipleEntry.stream().filter(e -> predicate.negate().test(e)).toList();
    } else if (singleEntry != null && predicate.test(singleEntry)) {
      singleEntry = null;
    }
  }
}
