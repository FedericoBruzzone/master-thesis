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
public class ImportTest {
  @Test
  public void testImport(@NeverlangUnitParam(source = "import $1.test as SYNTAX") ASTNode astNode) {
    String txt = astNode.getValue("Text");
    //        assertThat(txt).isEqualToNormalizingWhitespace(typeFunctionDefinition);
  }
}
