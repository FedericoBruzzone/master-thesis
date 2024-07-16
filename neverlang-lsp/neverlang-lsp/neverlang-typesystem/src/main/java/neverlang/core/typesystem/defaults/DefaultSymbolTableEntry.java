package neverlang.core.typesystem.defaults;

import neverlang.core.typesystem.*;
import neverlang.core.typesystem.symbols.Range;
import neverlang.core.typesystem.symbols.Token;
import neverlang.core.typesystem.symboltable.EntryKind;
import neverlang.core.typesystem.typenv.EntryType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record DefaultSymbolTableEntry(
    @NotNull EntryType entryType,
    @Nullable EntryDetails details,
    @Nullable Range foldingRange,
    @NotNull EntryKind entryKind,
    int ownerHashcode)
    implements SymbolTableEntry {

  public DefaultSymbolTableEntry(Type type, Token token) {
    this(new DefaultEntryType(token, type), null, null, EntryKind.DEFINE, -1);
  }

  @Override
  public String toString() {
    return String.format(
        "%s in %s", entryType().token().text(), entryType().token().range());
  }
}
