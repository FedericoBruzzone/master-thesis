package neverlang.core.typesystem.graph;

import java.net.URI;
import java.util.Optional;
import java.util.function.Function;
import neverlang.core.typesystem.symbols.Location;
import neverlang.core.typesystem.symbols.Range;
import org.jetbrains.annotations.Nullable;

public interface Indexable extends Comparable<Indexable> {
  @Nullable
  Location location();

  /**
   * The range that should be selected and revealed when this symbol is being picked, e.g the name
   * of a function. Must be contained by the {@link #range()}.
   */
  @Nullable
  default Range selectionRange() {
    return Optional.ofNullable(location()).map(Location::range).orElse(null);
  }

  Range foldingRange();

  /**
   * The range enclosing this symbol not including leading/trailing whitespace but everything else
   * like comments. This information is typically used to determine if the client's cursor is
   * inside the symbol to reveal the symbol in the UI.
   */
  @Nullable
  default Range range() {
    return Optional.ofNullable(selectionRange())
        .map(e -> e.merge(foldingRange()))
        .orElse(null);
  }

  private boolean isIN(Range range, Function<Range, Boolean> mapFunction) {
    return Optional.ofNullable(range).map(mapFunction).orElse(false);
  }

  default boolean isExactlyThis(int row, int col) {
    return isIN(selectionRange(), e -> e.contains(row, col));
  }

  default boolean isInsideThis(int row, int col) {
    return isIN(range(), e -> e.contains(row, col));
  }

  default boolean isInsideThis(Range range) {
    return isIN(range(), e -> e.contains(range));
  }

  /**
   * @return -1 if not a single line
   */
  default int length() {
    if (selectionRange() != null
        && selectionRange().startRow() == selectionRange().endRow()) {
      return selectionRange().endCol() - selectionRange().startCol();
    } else {
      return -1;
    }
  }

  /**
   * Check if index is inside this
   *
   * @param index
   * @return
   */
  default boolean isInsideThis(Indexable index) {
    return isIN(range(), e -> e.contains(index.selectionRange()));
  }

  default Optional<URI> getUri() {
    return Optional.ofNullable(location()).map(Location::uri);
  }

  default int compareTo(Indexable o) {
    var otherRange = o.selectionRange();
    var currentRange = selectionRange();
    if (otherRange == null && currentRange == null) {
      return 0;
    } else if (otherRange == null) {
      return 1;
    } else if (currentRange == null) {
      return -1;
    } else {
      return currentRange.compareTo(otherRange);
    }
  }
}
