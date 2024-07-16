package neverlang.core.typesystem.utils;

import java.util.concurrent.Flow;
import java.util.stream.Stream;
import neverlang.core.typesystem.AbstractCompilationHelper;
import neverlang.core.typesystem.CompilationContext;
import neverlang.core.typesystem.CompilationUnitToken;
import neverlang.runtime.ASTNode;
import org.jetbrains.annotations.Nullable;

public class TestUtils {

  public static <T extends AbstractCompilationHelper> T evalASTNode(
      Class<T> compilationHelperClass, ASTNode astNode) {
    return evalASTNode(compilationHelperClass, astNode, null);
  }

  public static <T extends AbstractCompilationHelper> T evalASTNode(
      Class<T> compilationHelperClass,
      ASTNode astNode,
      @Nullable Flow.Subscriber<Object> subscriber) {
    var compilationHelper =
        AbstractCompilationHelper.getFromASTNode(astNode, compilationHelperClass);
    if (subscriber != null) {
      compilationHelper.subscribe(subscriber);
    }
    compilationHelper.setup(
        new CompilationContext(new CompilationUnitToken(), Stream.empty(), null));
    compilationHelper.eval();
    return compilationHelper;
  }
}
