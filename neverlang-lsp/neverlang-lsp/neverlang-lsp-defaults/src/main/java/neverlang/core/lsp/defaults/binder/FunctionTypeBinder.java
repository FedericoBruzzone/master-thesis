package neverlang.core.lsp.defaults.binder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;
import neverlang.core.lsp.defaults.typenv.ReturnEntry;
import neverlang.core.typesystem.SymbolTableEntry;
import neverlang.core.typesystem.Type;
import neverlang.core.typesystem.UnbindableEntryException;
import neverlang.core.typesystem.Variance;
import neverlang.core.typesystem.defaults.SingleTypeTypeBinder;
import neverlang.core.typesystem.typenv.EntryTypeBinder;

public class FunctionTypeBinder extends SingleTypeTypeBinder {
  public List<SymbolTableEntry> returnEntries = new ArrayList<>();
  public AtomicReference<Type> currentType = new AtomicReference<>(null);

  @Override
  public EntryTypeBinder bindEntry(SymbolTableEntry entry) {
    if (entry.details() instanceof ReturnEntry) {
      var type = currentType.get();
      // We check that return types are compatible
      if (type != null && !entry.type().isAssignableFrom(type, Variance.INVARIANT)) {
        throw new UnbindableEntryException(
            "%s and %s are incompatible".formatted(type.id(), entry.type().id()), entry);
      } else {
        currentType.set(entry.type());
      }
      returnEntries.add(entry);
      return this;
    } else {
      // Otherwise, use the default behavior that allows to bind
      // the entry to an identifier only once!
      return super.bindEntry(entry);
    }
  }

  @Override
  public Stream<SymbolTableEntry> stream() {
    if (returnEntries.size() > 0) {
      return returnEntries.stream();
    }
    return super.stream();
  }
}
