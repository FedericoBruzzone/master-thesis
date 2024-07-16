package typelang;

import static org.assertj.core.api.Assertions.assertThat;

import neverlang.core.typelang.test.TypeLang;
import neverlang.core.typesystem.SymbolTableEntry;
import neverlang.core.typesystem.defaults.DefaultSymbolTableEntry;
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
public class DefineTest {
  /**
   * $1 must contain an attribute "type" that should be an {@link SymbolTableEntry} $2 must
   * contain an attribute "token" that should be a {@link neverlang.core.typesystem.symbols.Token}
   * This code define a new type with the name of the token and the type of the symbol table entry
   */
  @Test
  public void testInferredTypeAssignment(
      @NeverlangUnitParam(source = "define $1 $2") ASTNode astNode) {
    String txt = astNode.getValue("Text");
    assertThat(txt)
        .isEqualToNormalizingWhitespace(Templates.typeDefinitionFromChild.formatted(
            Templates.symbolTableEntry, "type", "token"));
  }

  @Test
  public void testInferredTypeAssignmentWithThen(
      @NeverlangUnitParam(source = "define $1 $2 then validate") ASTNode astNode) {
    String txt = astNode.getValue("Text");
    var head =
        Templates.typeDefinitionFromChild.formatted(Templates.symbolTableEntry, "type", "token");
    var tail = Templates.validationDefinition.formatted(
        "type0", "<typelang.utils.TypeTest>type().customMethod", "type0");
    assertThat(txt).isEqualToNormalizingWhitespace(head + tail);
  }

  @Test
  public void testInferredTypeAssignmentWithAttribute(
      @NeverlangUnitParam(source = "define $1.myType $2.myToken") ASTNode astNode) {
    String txt = astNode.getValue("Text");
    assertThat(txt)
        .isEqualToNormalizingWhitespace(Templates.typeDefinitionFromChild.formatted(
            DefaultSymbolTableEntry.class.getCanonicalName(), "myType", "myToken"));
  }

  @Test
  public void testTypeDefinition(
      @NeverlangUnitParam(source = "define scope module $1 from #0 to #1") ASTNode astNode) {
    String txt = astNode.getValue("Text");
    assertThat(txt).isEqualToNormalizingWhitespace(Templates.typeModuleDefinition);
  }

  @Test
  public void testTypeFileDefinition(
      @NeverlangUnitParam(source = "define scope file ($file ?? global)") ASTNode astNode) {
    String txt = astNode.getValue("Text");
    assertThat(txt).isEqualToNormalizingWhitespace(Templates.typeFileDefinition);
  }

  @Test
  public void testTypeCustomModuleDefinition(
      @NeverlangUnitParam(source = "define scope module customModule") ASTNode astNode) {
    String txt = astNode.getValue("Text");
    assertThat(txt).isEqualToNormalizingWhitespace(Templates.typeCustomNameDefinition);
  }

  @Test
  public void testTaskDefinition(
      @NeverlangUnitParam(
              source = "define scope module $1 from #0 to #1 [ run $1 $2 priority module ]")
          ASTNode astNode) {
    String txt = astNode.getValue("Text");
    assertThat(txt)
        .isEqualToNormalizingWhitespace(Templates.typeModuleDefinition + Templates.taskDefinition);
  }

  @Test
  public void testCurrentScopeTaskDefinition(
      @NeverlangUnitParam(source = "current scope syntax [ run $1 $2 priority module ]")
          ASTNode astNode) {
    String txt = astNode.getValue("Text");
    assertThat(txt)
        .isEqualToNormalizingWhitespace(
            Templates.currentScopeTaskDefinition.formatted("\"syntax\""));
  }

  @Test
  public void testTaskDefinitionWithThen(
      @NeverlangUnitParam(
              source =
                  "define scope module $1 from #0 to #1 [ run $1 $2 priority module then validate ]")
          ASTNode astNode) {
    String txt = astNode.getValue("Text");
  }

  @Test
  public void testScopeWithAttributes(
      @NeverlangUnitParam(source = "define scope file $1 modifier $1.value") ASTNode astNode) {
    String txt = astNode.getValue("Text");
    assertThat(txt).isEqualToNormalizingWhitespace(Templates.typeModuleDefinitionWithModifier);
  }

  @Test
  public void testTaskRootDefinition(
      @NeverlangUnitParam(
              source = "define scope module $1 from #0 to #1 [ run root $1 $2 priority module ]")
          ASTNode astNode) {
    String txt = astNode.getValue("Text");
    assertThat(txt)
        .isEqualToNormalizingWhitespace(
            Templates.typeModuleDefinition + Templates.taskRootDefinition);
  }

  @Test
  public void testSimpleType(
      @NeverlangUnitParam(source = "define function $1 from #0 to #1") ASTNode astNode) {
    String txt = astNode.getValue("Text");
    assertThat(txt).isEqualToNormalizingWhitespace(Templates.typeFunctionDefinition);
  }

  @Test
  public void testEnterScope(
      @NeverlangUnitParam(source = "define scope module customModule\nenter scope")
          ASTNode astNode) {
    String txt = astNode.getValue("Text");
    //        assertThat(txt).isEqualToNormalizingWhitespace(typeFunctionDefinition);
    assertThat(txt)
        .isEqualToNormalizingWhitespace(
            Templates.typeCustomNameDefinition + "\n" + "unit.enterScope(scope0);");
  }
}
