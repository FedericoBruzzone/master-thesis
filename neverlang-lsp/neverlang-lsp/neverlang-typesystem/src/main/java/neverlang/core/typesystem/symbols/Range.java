package neverlang.core.typesystem.symbols;

import java.util.Optional;
import java.util.stream.Stream;
import neverlang.parser.NeverlangToken;
import neverlang.runtime.ASTNode;
import neverlang.runtime.Context;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record Range(int startRow, int startCol, int endRow, int endCol)
    implements Comparable<Range> {
  public static Optional<Range> of(@NotNull Context context) {
    return Optional.ofNullable(context.file()).map(e -> Range.fromText(context.source(), 0, 0));
  }

  public static Range foldingRangeFrom(@NotNull ASTNode node, int start, int end) {
    var startRange = fromNeverlangToken(node.tchild(start).getToken());
    var endRange = fromNeverlangToken(node.tchild(end).getToken());
    return startRange.merge(endRange);
  }

  // Range start from 0,0
  @NotNull
  public static Range fromNeverlangToken(@NotNull NeverlangToken token) {
    var text = token.getText();
    // NeverlangToken start from 1,1
    var startRow = token.row - 1;
    // TODO: try to understand NeverlangToken
    var startCol = startRow == 0 ? token.col : token.col - 1;
    return fromText(text, startRow, startCol);
  }

  public static Range fromText(String text) {
    return fromText(text, 0, 0);
  }

  @NotNull
  public static Range fromText(String text, int startRow, int startCol) {
    var lines = text == null ? 0 : text.lines().count();
    var length = text == null ? 0 : text.length();
    var rows = Long.valueOf(lines).intValue() - 1;
    var col = rows <= 0
        ? length + startCol
        : text.lines().map(String::length).skip(rows).findFirst().orElse(0);
    return new Range(startRow, startCol, startRow + rows, col);
  }

  public Range merge(@Nullable Range range) {
    if (range == null) return this;
    var sr = Math.min(startRow, range.startRow);
    var sc = startRow < range.startRow ? startCol : Math.min(range.startCol, startCol);
    var er = Math.max(endRow, range.endRow());
    var ec = endRow > range.endRow ? endCol : Math.max(range.endCol, endCol);
    return new Range(sr, sc, er, ec);
  }

  public boolean contains(int row, int col) {
    if (startRow() == row && endRow() == row) {
      return col >= startCol() && col <= endCol();
    } else if (startRow() == row) {
      return col >= startCol();
    } else if (endRow() == row) {
      return col <= endCol();
    } else return row > startRow() && row < endRow();
  }

  public boolean contains(@Nullable Range range) {
    return range != null
        && contains(range.startRow(), range.endCol())
        && contains(range.endRow(), range.endCol());
  }

  @Override
  public String toString() {
    return String.format("%d:%d-%d:%d", startRow, startCol, endRow, endCol);
  }

  @Override
  public int compareTo(@NotNull Range o) {
    if (startRow() == o.startRow()) {
      return Integer.compare(startCol(), o.startCol());
    } else {
      return Integer.compare(startRow(), o.startRow());
    }
  }

  public Stream<Integer> stream() {
    return Stream.of(startRow, endRow, startCol, endCol);
  }
}
