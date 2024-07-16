package neverlang.compiler.lsp;

import neverlang.core.lsp.defaults.symboltable.CompilationHelper;
import neverlang.core.typesystem.utils.TestUtils;
import neverlang.junit.NeverlangExt;
import neverlang.junit.NeverlangUnit;
import neverlang.junit.NeverlangUnitParam;
import neverlang.runtime.ASTNode;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(NeverlangExt.class)
@NeverlangUnit(language = NeverlangLangLSP.class, injectors = NeverlangLangLSPModule.class)
public class NeverlangLangTest {
  @Disabled
  @Test
  public void moduleTest(@NeverlangUnitParam(files = "Module.nl") ASTNode astNode) {
    var compilationHelper = TestUtils.evalASTNode(CompilationHelper.class, astNode);
  }
}
