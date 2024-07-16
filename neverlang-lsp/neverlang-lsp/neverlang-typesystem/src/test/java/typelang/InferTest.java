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
public class InferTest {
  @Test
  public void testSimpleInfer(
      @NeverlangUnitParam(source = "infer identifier $1 ") ASTNode astNode) {
    String txt = astNode.getValue("Text");
    //        assertThat(txt).isEqualToNormalizingWhitespace(typeFunctionDefinition);
  }

  @Test
  public void testSimpleInferWithAssignment(
      @NeverlangUnitParam(source = "infer identifier $1 => $1.customType") ASTNode astNode) {
    String txt = astNode.getValue("Text");
    //        assertThat(txt).isEqualToNormalizingWhitespace(typeFunctionDefinition);
  }

  @Test
  public void testSimpleInferWithAssignmentWithoutAttribute(
      @NeverlangUnitParam(source = "infer identifier $1 => $1") ASTNode astNode) {
    String txt = astNode.getValue("Text");
    //        assertThat(txt).isEqualToNormalizingWhitespace(typeFunctionDefinition);
  }

  @Test
  public void testInferFunctionNoArray(
      @NeverlangUnitParam(source = "infer function $1 with $1") ASTNode astNode) {
    String txt = astNode.getValue("Text");
    //        assertThat(txt).isEqualToNormalizingWhitespace(typeFunctionDefinition);
  }

  @Test
  public void testInferFunction(
      @NeverlangUnitParam(source = "infer function $1 with [$1 $2]") ASTNode astNode) {
    String txt = astNode.getValue("Text");
    //        assertThat(txt).isEqualToNormalizingWhitespace(typeFunctionDefinition);
  }

  @Test
  public void testInferFunctionMultipleParams(
      @NeverlangUnitParam(source = "infer function $1 with [$1 $2], $3") ASTNode astNode) {
    String txt = astNode.getValue("Text");
    //        assertThat(txt).isEqualToNormalizingWhitespace(typeFunctionDefinition);
  }

  @Test
  public void testInferFromIdentifier(
      @NeverlangUnitParam(source = "infer function SYNTAX") ASTNode astNode) {
    String txt = astNode.getValue("Text");
    //        assertThat(txt).isEqualToNormalizingWhitespace(typeFunctionDefinition);
  }

  @Test
  public void testInferFromIdentifierInsideOtherTypes(
      @NeverlangUnitParam(source = "infer function SYNTAX from $1") ASTNode astNode) {
    String txt = astNode.getValue("Text");
    //        assertThat(txt).isEqualToNormalizingWhitespace(typeFunctionDefinition);
  }
}
