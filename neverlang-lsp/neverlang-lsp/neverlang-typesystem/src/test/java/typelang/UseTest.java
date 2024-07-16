package typelang;

import neverlang.core.typelang.test.TypeLang;
import neverlang.junit.NeverlangExt;
import neverlang.junit.NeverlangUnit;
import neverlang.junit.NeverlangUnitParam;
import neverlang.runtime.ASTNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import typelang.utils.MapperTestModule;

@ExtendWith(NeverlangExt.class)
@NeverlangUnit(language = TypeLang.class, injectors = MapperTestModule.class)
public class UseTest {

  @Test
  public void testUse(@NeverlangUnitParam(source = "use $1 as $2") ASTNode astNode) {
    String txt = astNode.getValue("Text");
    //        assertThat(txt).isEqualToNormalizingWhitespace(typeFunctionDefinition);
  }
}
