package simplelang;

import java.nio.file.*;
import java.util.logging.LogRecord;
import lsp.SimpleLangWorkspaceHandler;
import neverlang.core.lsp.compiler.WorkspaceHandler;
import neverlang.core.lsp.services.Workspace;
import neverlang.core.typesystem.CompilationContext;
import neverlang.core.typesystem.CompilationUnitToken;
import neverlang.core.typesystem.compiler.Compiler;
import neverlang.core.typesystem.compiler.IncrementalCompilationHelper;
import neverlang.core.typesystem.compiler.SourceEventCase;
import neverlang.core.typesystem.compiler.SourceSet;
import neverlang.core.typesystem.defaults.DefaultIncrementalCompilationHelper;
import neverlang.core.typesystem.defaults.DefaultSourceSet;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

public class IncrementalTest {

  private final IncrementalCompilationHelper incrementalCompilationHelper =
      new DefaultIncrementalCompilationHelper();

  public static final String base_dir = "incrementalTest";

  private static SourceSet sourceSet;

  private static Compiler compiler;

  @TempDir
  public static Path tempDir;

  private static SimpleLangTest.RecordSubscriber recordSubscriber;

  @BeforeAll
  public static void setUP() {
    Utils.updateVersion(tempDir, base_dir, 1);
    WorkspaceHandler wh = new SimpleLangWorkspaceHandler(new Workspace(tempDir, "simplelang"));
    wh.buildCompiler();

    sourceSet = new DefaultSourceSet.Builder(".sl").buildFromRootDir(tempDir);

    recordSubscriber = new SimpleLangTest.RecordSubscriber(e -> e instanceof LogRecord);
    compiler = wh.getCompiler(); // new Compiler(new SimpleLang(new SimpleLangModule()),
    // CompilationHelper.class);
    compiler.getCompilationHelper().subscribe(recordSubscriber);
  }

  @Order(1)
  @Test
  public void v1() {
    compiler.compile(new CompilationContext(
        new CompilationUnitToken(), sourceSet.getClonedStream(), incrementalCompilationHelper));
  }

  @Order(2)
  @Test
  public void v2() {
    Utils.updateVersion(tempDir, base_dir, 2);
    sourceSet.update(tempDir.resolve("test.sl"), SourceEventCase.MODIFIED);
    var token = new CompilationUnitToken();
    compiler.compile(
        new CompilationContext(token, sourceSet.getClonedStream(), incrementalCompilationHelper));
  }
}
