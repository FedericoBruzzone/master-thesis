package neverlang.core.lsp.defaults.symboltable;

import neverlang.core.lsp.defaults.types.TypeSourceSet;
import neverlang.core.typelang.annotations.CompilationHelperAnnotation;
import neverlang.core.typelang.annotations.CompilationHelperEnum;
import neverlang.core.typelang.annotations.TypeLangAnnotation;
import neverlang.core.typelang.annotations.TypeSystemKind;
import neverlang.core.typesystem.AbstractCompilationHelper;
import neverlang.core.typesystem.Priority;
import neverlang.core.typesystem.Scope;

@CompilationHelperAnnotation(CompilationHelperEnum.COMPILATION_HELPER)
@TypeLangAnnotation(kind = TypeSystemKind.COMPILATION_HELPER)
public class CompilationHelper extends AbstractCompilationHelper<String, Priority> {

  @Override
  protected Scope<String> generateRootType() {
    return new TypeSourceSet();
  }
}
