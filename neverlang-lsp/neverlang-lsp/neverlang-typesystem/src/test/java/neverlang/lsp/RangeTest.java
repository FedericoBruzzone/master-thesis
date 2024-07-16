package neverlang.lsp;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import neverlang.core.typesystem.symbols.Range;
import neverlang.parser.NeverlangToken;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

@Disabled
public class RangeTest {

  @Test
  public void singleLineRangeConversionNT() {
    var nt = Mockito.mock(NeverlangToken.class, Mockito.CALLS_REAL_METHODS);
    nt.row = 1;
    nt.col = 1;
    nt.text = "Single Line";

    var expectedRange = new Range(0, 1, 0, 12);
    var obtainedRange = Range.fromNeverlangToken(nt);
    assertThat(obtainedRange).isEqualTo(expectedRange);
  }

  @Test
  public void singleLineRangeConversionWithOffsetNT() {
    var nt = Mockito.mock(NeverlangToken.class, Mockito.CALLS_REAL_METHODS);
    nt.row = 1;
    nt.col = 5;
    nt.text = "test";

    var expectedRange = new Range(0, 5, 0, 9);
    var obtainedRange = Range.fromNeverlangToken(nt);
    assertThat(obtainedRange).isEqualTo(expectedRange);
  }

  @Test
  public void multiLineRangeConversionNT() {
    var nt = Mockito.mock(NeverlangToken.class, Mockito.CALLS_REAL_METHODS);
    nt.row = 1;
    nt.col = 1;
    nt.text = "Multiline\nhello world!";

    var expectedRange = new Range(0, 1, 1, 12);
    var obtainedRange = Range.fromNeverlangToken(nt);
    assertThat(obtainedRange).isEqualTo(expectedRange);
  }

  @Test
  public void singleLineRangeConversion() {
    var row = 0;
    var col = 0;
    var text = "Single Line";

    var expectedRange = new Range(0, 0, 0, 11);
    var obtainedRange = Range.fromText(text, row, col);
    assertThat(obtainedRange).isEqualTo(expectedRange);
  }

  @Test
  public void singleLineRangeConversionWithOffset() {
    var row = 0;
    var col = 4;
    var text = "test";

    var expectedRange = new Range(0, 4, 0, 8);
    var obtainedRange = Range.fromText(text, row, col);
    assertThat(obtainedRange).isEqualTo(expectedRange);
  }

  @Test
  public void multiLineRangeConversion() {
    var row = 0;
    var col = 0;
    var text = "Multiline\nhello world!";

    var expectedRange = new Range(0, 0, 1, 12);
    var obtainedRange = Range.fromText(text, row, col);
    assertThat(obtainedRange).isEqualTo(expectedRange);
  }

  @Test
  public void rangeMerge() {
    var r1 = new Range(1, 10, 2, 10);
    var r2 = new Range(1, 15, 2, 15);
    var expectedRange = new Range(1, 10, 2, 15);
    assertThat(r1.merge(r2)).isEqualTo(expectedRange);
  }
}
