package simplelang;

import static org.assertj.core.api.Assertions.assertThat;
import static simplelang.IncrementalTest.tempDir;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Flow;
import java.util.function.Predicate;
import java.util.logging.LogRecord;
import java.util.stream.Stream;
import lsp.SimpleLangWorkspaceHandler;
import neverlang.core.lsp.compiler.WorkspaceHandler;
import neverlang.core.lsp.defaults.symboltable.CompilationHelper;
import neverlang.core.lsp.services.Workspace;
import neverlang.core.typesystem.*;
import neverlang.core.typesystem.compiler.Compiler;
import neverlang.core.typesystem.defaults.DefaultSymbolTableEntry;
import neverlang.junit.NeverlangExt;
import neverlang.junit.NeverlangUnit;
import neverlang.junit.NeverlangUnitParam;
import neverlang.runtime.ASTNode;
import neverlang.runtime.Language;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import simplelang.typesystem.BaseLang;

// TODO: test void function => `function() {}`
@ExtendWith(NeverlangExt.class)
@NeverlangUnit(language = SimpleLang.class, injectors = SimpleLangModule.class)
public class SimpleLangTest {

  private static final String sum =
      """
function sum1(x) {
    return sum(x,1);
}

function sum(x,y) {
    return x + y;
}""";

  private static WorkspaceHandler wh;
  private static Language language;

  @BeforeAll
  public static void setUP() { // @Lang SimpleLang l
    language = getNewLanguage();
  }

  public void clear(ASTNode astNode) {
    var compilationHelper = CompilationHelper.getFromASTNode(astNode, CompilationHelper.class);
    compilationHelper.cleanGraph();
  }

  private static Language getNewLanguage() {
    return new SimpleLangWorkspaceHandler(new Workspace(tempDir, "simplelang"))
        .buildCompiler()
        .getLanguage();
  }

  private static Compiler getNewCompiler() {
    return new SimpleLangWorkspaceHandler(new Workspace(tempDir, "simplelang")).buildCompiler();
  }

  private static CompilationHelper evalASTNode(ASTNode astNode) {
    return evalASTNode(astNode, null);
  }

  private static CompilationHelper evalASTNode(
      ASTNode astNode, @Nullable Flow.Subscriber<Object> subscriber) {
    var compilationHelper = CompilationHelper.getFromASTNode(astNode, CompilationHelper.class);
    if (subscriber != null) {
      compilationHelper.subscribe(subscriber);
    }
    compilationHelper.setup(
        new CompilationContext(new CompilationUnitToken(), Stream.empty(), null));
    compilationHelper.eval();
    return compilationHelper;
  }

  @Test
  public void testCategories() throws IOException {
    Language l = getNewLanguage();
    l.getSlices().values().stream().map(e -> e.getSyntaxRole().getDeclaredCategories());
    //        StringType t = new StringType();

    //        String pkg = l.getClass().getPackageName();
    //        String name = l.getClass().getSimpleName().toLowerCase();
    //        try {
    //            Class<?> mapper = this.getClass().getClassLoader().loadClass(pkg + "." + name
    // +
    // ".CategoryMapper");
    //            Field f = mapper.getDeclaredField("mapper");
    //            f.setAccessible(true);
    //            Map<Integer, String> map = (Map<Integer, String>) f.get(null);
    //            System.out.println(map);
    //        } catch (ClassNotFoundException | IllegalAccessException | NoSuchFieldException e)
    // {
    //        }
    //        Compiler c = getNewCompiler();
    //
    //        String projectPath = System.getProperty("user.dir");
    //        String buildFolder = projectPath + "/build";
    //        var tmp = new CategoryMapperWriter(new File(buildFolder));
    //        tmp.setLanguage(l);
  }

  @Test
  public void testReturnFunction(
      @NeverlangUnitParam(source = "function ack(m,n) {\n" + "  return n + 1;\n" + "}")
          ASTNode pAstNode) {
    var astNode = language.exec(pAstNode);
    var compilationHelper = evalASTNode(astNode);
    var writer = new StringWriter();
    Utils.dotExporter(compilationHelper.getGraph(), writer);
    astNode.getValues();
  }

  @Test
  public void testSum(@NeverlangUnitParam(source = sum) ASTNode pAstNode) {
    //        var string = new GraphvizAST(astNode).toString();
    //        System.out.println(string);
    var astNode = language.exec(pAstNode);
    var compilationHelper = evalASTNode(astNode);
    var writer = new StringWriter();
    Utils.dotExporter(compilationHelper.getGraph(), writer);
    astNode.getValues();
  }

  @Test
  public void testFailFunction(
      @NeverlangUnitParam(source = "function ack(m,n) {\n" + "  return true + 1;\n" + "}")
          ASTNode pAstNode) {
    var astNode = language.exec(pAstNode);
    var compilationHelper = evalASTNode(astNode);
    var writer = new StringWriter();
    Utils.dotExporter(compilationHelper.getGraph(), writer);
    astNode.getValues();
  }

  @Test
  public void testIfReturnFunction(
      @NeverlangUnitParam(
              source = "function ack(m,n) {\n"
                  + "  if (m == 0) {\n"
                  + "    return n + 1;\n"
                  + "  }\n"
                  + "}")
          ASTNode pAstNode) {
    var astNode = language.exec(pAstNode);
    var compilationHelper = evalASTNode(astNode);
    var writer = new StringWriter();
    Utils.dotExporter(compilationHelper.getGraph(), writer);
    astNode.getValues();
  }

  @Test
  public void testAssignmentFunction(
      @NeverlangUnitParam(
              source = "function ack(m,n) {\n" + "  n2 = m + 1;\n" + "  return n2;\n" + "}")
          ASTNode pAstNode) {
    var astNode = language.exec(pAstNode);
    var compilationHelper = evalASTNode(astNode);

    var writer = new StringWriter();
    Utils.dotExporter(compilationHelper.getGraph(), writer);
    astNode.getValues();
  }

  @Test
  public void testWithBoolean(
      @NeverlangUnitParam(
              source = "function tano(m,n) {\n"
                  + "    m = m + 1;\n"
                  + "    n = true;\n"
                  + "    return m + 1;\n"
                  + "}")
          ASTNode pAstNode) {
    var astNode = language.exec(pAstNode);
    var recordSubscriber = new RecordSubscriber(e -> e instanceof LogRecord);
    evalASTNode(astNode);
    assertThat(recordSubscriber.getList()).hasSize(0);
  }

  @Test
  public void testFunction(
      @NeverlangUnitParam(
              source = "function ack(m,n) {\n"
                  + "  if (m == 0) {\n"
                  + "    return n + 1;\n"
                  + "  }\n"
                  + "  if (n == 0) {\n"
                  + "      n = 1;\n"
                  + "  } else {\n"
                  + "      n = ack(m, n - 1);\n"
                  + "  }\n"
                  + "  return ack(m - 1, n);\n"
                  + "}")
          ASTNode pAstNode) {
    var astNode = getNewLanguage().exec(pAstNode);
    var recordSubscriber = new RecordSubscriber(e -> e instanceof LogRecord);
    var compilationHelper = evalASTNode(astNode, recordSubscriber);
    tokenAssertion(compilationHelper, 0, 15, 7, BaseLang.intType);
    tokenAssertion(compilationHelper, 0, 14, 4, BaseLang.intType);
    astNode.getValues();
    assertThat(recordSubscriber.getList()).hasSize(0);
  }

  @Test
  public void testVariableNotDeclared(
      @NeverlangUnitParam(
              source = "function ack(m) {\n"
                  + "  if (m == 0) {\n"
                  + "    return n + 1;\n"
                  + "  }\n"
                  + "  if (n == 0) {\n"
                  + "      n = 1;\n"
                  + "  } else {\n"
                  + "      n = ack(m, n - 1);\n"
                  + "  }\n"
                  + "  return ack(m - 1, n);\n"
                  + "}")
          ASTNode pAstNode) {
    var astNode = getNewLanguage().exec(pAstNode);
    var recordSubscriber = new RecordSubscriber(e -> e instanceof LogRecord);
    evalASTNode(astNode, recordSubscriber);
    assertThat(recordSubscriber.getList()).hasSize(4);
  }

  private static void tokenAssertion(
      CompilationHelper compilationHelper, int row, int col, int expected, Type type) {
    var n_token = compilationHelper
        .getGraph()
        .getIndexStructure()
        .getIndexTree(null)
        .flatMap(e -> e.<DefaultSymbolTableEntry>lookup(row, col));
    assertThat(n_token).isPresent();
    var n_occurences = compilationHelper.getGraph().getReferences(n_token.get()).toList();
    assertThat(n_occurences).hasSize(expected);
    assertThat(n_occurences).allMatch(e -> Objects.requireNonNull(e.type()).equals(type));
  }

  @Test
  public void undefinedNameSemanticError(
      @NeverlangUnitParam(
              source = "function ack2(m,n) {\n"
                  + "  if (m == 0) {\n"
                  + "    return n + 1;\n"
                  + "  }\n"
                  + "  if (n == 0) {\n"
                  + "      n = 1;\n"
                  + "  } else {\n"
                  + "      n = ack(m, n - 1);\n"
                  + "  }\n"
                  + "  return ack(m - 1, n);\n"
                  + "}")
          ASTNode pAstNode) {
    var astNode = getNewLanguage().exec(pAstNode);
    var recordSubscriber = new RecordSubscriber(e -> e instanceof LogRecord);
    var compilationHelper = evalASTNode(astNode, recordSubscriber);
    var n_token = compilationHelper
        .getGraph()
        .getIndexStructure()
        .getIndexTree(null)
        .flatMap(e -> e.<DefaultSymbolTableEntry>lookup(0, 17));
    assertThat(compilationHelper.getGraph().getReferences(n_token.get()).toList())
        .hasSize(6);
    assertThat(recordSubscriber.getList()).hasSize(2);
  }

  @Test
  public void testMultipleFunctions(@NeverlangUnitParam(files = "hello.sl") ASTNode pAstNode) {
    var l = getNewLanguage();
    // l.exec(pAstNode); Fail if I call this
    var astNode = l.exec(pAstNode);
    var recordSubscriber = new RecordSubscriber(e -> e instanceof LogRecord);
    evalASTNode(astNode, recordSubscriber);
    assertThat(recordSubscriber.getList()).hasSize(0);
  }

  static class RecordSubscriber implements Flow.Subscriber<Object> {
    private final Predicate<Object> predicate;
    private Flow.Subscription subscription;
    private final List<Object> list = new ArrayList<>();

    public RecordSubscriber(Predicate<Object> predicate) {
      this.predicate = predicate;
    }

    public List<Object> getList() {
      return list;
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
      this.subscription = subscription;
      subscription.request(1);
    }

    @Override
    public void onNext(Object item) {
      if (predicate.test(item)) {
        list.add(item);
      }
      subscription.request(1);
    }

    @Override
    public void onError(Throwable throwable) {
      throwable.printStackTrace();
    }

    @Override
    public void onComplete() {
      subscription.cancel();
    }
  }
}
