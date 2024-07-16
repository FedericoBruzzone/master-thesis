package neverlang.core.typesystem;

import java.util.concurrent.atomic.AtomicReference;
import neverlang.core.typesystem.graph.Indexable;
import neverlang.core.typesystem.symbols.Location;
import neverlang.core.typesystem.symboltable.EntryKind;
import neverlang.core.typesystem.typenv.EntryType;

public interface SymbolTableEntry extends Indexable {
  default <T extends Type> T type() {
    return (T) refType().get();
  }

  default AtomicReference<Type> refType() {
    return entryType().refType();
  }

  EntryKind entryKind();

  EntryType entryType();

  EntryDetails details();

  default Location location() {
    return entryType().token().location();
  }

  default boolean isAssignableFrom(SymbolTableEntry symbolTableEntry, Variance variance) {
    return type().isAssignableFrom(symbolTableEntry.type(), variance);
  }

  default int ownerHashcode() {
    return -1;
  }
}
