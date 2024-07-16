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
public class CatchTest {
  @Test
  public void testCatchEvalSugarTypeLang(
      @NeverlangUnitParam(source = "tryEval $1") ASTNode astNode) {
    String txt = astNode.getValue("Text");
    assertThat(txt).isEqualToNormalizingWhitespace(Templates.catchEvalString);
  }

  @Test
  public void testCatchEvalTypeLang(@NeverlangUnitParam(source = "try {eval $1}") ASTNode astNode) {
    String txt = astNode.getValue("Text");
    assertThat(txt).isEqualToNormalizingWhitespace(Templates.catchEvalString);
  }

  @Test
  public void testCatchWithOrBranch(
      @NeverlangUnitParam(source = "try {eval $1} on InferenceException {eval $2}")
          ASTNode astNode) {
    String txt = astNode.getValue("Text");
    assertThat(txt).isEqualToNormalizingWhitespace(Templates.catchWithOrBranchString);
  }

  @Test
  public void testCatchWithDefine(
      @NeverlangUnitParam(
              source = "try {\n"
                  + "                eval $1\n"
                  + "                define scope module $1 from #3 to #4 [\n"
                  + "                    run $2 $3 priority module\n"
                  + "                ]\n"
                  + "            }")
          ASTNode astNode) {
    String txt = astNode.getValue("Text");
  }
}
