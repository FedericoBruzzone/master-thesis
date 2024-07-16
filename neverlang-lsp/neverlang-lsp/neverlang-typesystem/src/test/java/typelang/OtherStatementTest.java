package typelang;

import static org.assertj.core.api.Assertions.assertThat;

import neverlang.core.typelang.test.TypeLang;
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
public class OtherStatementTest {

  @Test
  public void testTypeLang(@NeverlangUnitParam(source = "eval $1\neval $2") ASTNode astNode) {
    String txt = astNode.getValue("Text");
    assertThat(txt).isEqualTo("$ctx.eval($ctx.nt(1));\n$ctx.eval($ctx.nt(2));");
  }

  @Test
  public void testInitRoot(@NeverlangUnitParam(source = "initRoot module") ASTNode astNode) {
    String txt = astNode.getValue("Text");
    assertThat(txt)
        .isEqualToNormalizingWhitespace(
            "var helper = $ctx.root().<TestCompilationHelper>getValue(\"$TestCompilationHelper\");\n"
                + "helper.initRoot($ctx, Priority.MODULE);");
  }

  @Test
  public void testResolve(@NeverlangUnitParam(source = "resolve $1 as $2") ASTNode astNode) {
    String txt = astNode.getValue("Text");
    assertThat(txt).isEqualToNormalizingWhitespace(Templates.resolveString);
  }
}
