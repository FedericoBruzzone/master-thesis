package typelang;

import static org.assertj.core.api.Assertions.assertThat;

import neverlang.core.typelang.test.TypeLang;
import neverlang.core.typesystem.Variance;
import neverlang.junit.NeverlangExt;
import neverlang.junit.NeverlangUnit;
import neverlang.junit.NeverlangUnitParam;
import neverlang.runtime.ASTNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import typelang.utils.MapperTestModule;
import typelang.utils.Templates;

@ExtendWith(NeverlangExt.class)
@NeverlangUnit(language = TypeLang.class, injectors = MapperTestModule.class)
public class TypeCheckTest {

  /**
   * This will check that the type $1 is invariant with respect to $2. The first $1 represent the
   * token that will be checked. All the variance possibility are listed {@link Variance}
   */
  @Test
  public void testTypeCheck(
      @NeverlangUnitParam(source = "check $1 : $1 is invariant $2") ASTNode astNode) {
    String txt = astNode.getValue("Text");
    assertThat(txt)
        .isEqualToNormalizingWhitespace(
            Templates.checkString.formatted("$ctx.nt(1).getValue($ctx.attr(1, \"token\"))"));
  }
}
