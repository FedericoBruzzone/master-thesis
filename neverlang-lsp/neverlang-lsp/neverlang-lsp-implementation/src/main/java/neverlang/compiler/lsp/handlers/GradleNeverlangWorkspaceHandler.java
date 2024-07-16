package neverlang.compiler.lsp.handlers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.stream.Stream;
import neverlang.core.lsp.services.Workspace;
import neverlang.core.typesystem.compiler.Compiler;
import neverlang.core.typesystem.compiler.SourceSet;
import neverlang.core.typesystem.defaults.DefaultSourceSet;
import neverlang.gradle.NeverlangModel;
import org.gradle.tooling.BuildLauncher;
import org.gradle.tooling.ProjectConnection;
import org.gradle.tooling.model.GradleProject;
import org.jetbrains.annotations.Nullable;

public class GradleNeverlangWorkspaceHandler extends NeverlangWorkspaceHandler {
  private final NeverlangModel neverlangModel;
  private final NeverlangLSPDecompiler decompiler;
  private SourceSet sourceSet;
  private Path[] roots;

  public GradleNeverlangWorkspaceHandler(
      Workspace workspace,
      NeverlangModel neverlangModel,
      @Nullable NeverlangLSPDecompiler decompiler) {
    super(workspace);
    this.neverlangModel = neverlangModel;
    this.decompiler = decompiler;
  }

  @Override
  public boolean canHandle(Path path) {
    return Optional.ofNullable(roots)
        .map(e -> Arrays.stream(e).anyMatch(path::startsWith))
        .orElse(false);
  }

  @Override
  public SourceSet getSourceSet(Path rootDir) {
    if (sourceSet == null) {
      Stream<Path> tempDir = Stream.empty();
      if (decompiler != null) {
        try {
          tempDir = Stream.of(decompiler.storeDecompiledFilesInTempDir());
        } catch (IOException ex) {
          Compiler.logger.log(Level.SEVERE, "Could not decompile files", ex);
        }
      }
      this.roots = Stream.concat(tempDir, neverlangModel.srcDirs().stream().map(File::toPath))
          .filter(Objects::nonNull)
          .toArray(Path[]::new);

      sourceSet = new DefaultSourceSet.Builder(".nl").buildFromMultipleRoots(roots);
    }
    return sourceSet;
  }

  public static GradleNeverlangWorkspaceHandler of(
      ProjectConnection connection, GradleProject project) {
    var projectPath = project.getProjectDirectory().toPath();
    var neverlangModel = connection.getModel(NeverlangModel.class);
    var subprojectWorkspace = new Workspace(projectPath, project.getName());
    Compiler.logger.info(
        "Launching neverlangCompileClasspath for " + project.getName() + " in " + projectPath);
    BuildLauncher build = connection.newBuild();
    build.withArguments("-q", "--console=plain");
    build.forTasks("neverlangCompileClasspath");
    var outputStream = new ByteArrayOutputStream();
    build.setStandardOutput(outputStream);
    build.run();
    var classpath = Arrays.stream(outputStream.toString().split(":"))
        .map(Path::of)
        .filter(p -> !p.startsWith(projectPath))
        .map(Path::toFile)
        .map(File::toURI)
        .map(uri -> {
          try {
            return uri.toURL();
          } catch (Exception ex) {
            return null;
          }
        })
        .filter(Objects::nonNull)
        .toArray(URL[]::new);
    var decompiler = new NeverlangLSPDecompiler();
    decompiler.setClasspath(classpath);
    return new GradleNeverlangWorkspaceHandler(subprojectWorkspace, neverlangModel, decompiler);
  }
}
