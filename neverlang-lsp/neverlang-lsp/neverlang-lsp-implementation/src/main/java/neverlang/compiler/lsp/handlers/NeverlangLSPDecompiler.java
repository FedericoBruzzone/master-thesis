package neverlang.compiler.lsp.handlers;

import com.google.inject.Guice;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.logging.Level;
import neverlang.core.typesystem.compiler.Compiler;
import neverlang.decompiler.Decompiler;
import neverlang.decompiler.defaults.BundleDecompiler;
import neverlang.decompiler.defaults.ModuleDecompiler;
import neverlang.runtime.DexterNeverlangModule;
import org.gradle.tooling.GradleConnector;

public class NeverlangLSPDecompiler extends Decompiler {

  GradleConnector connector = GradleConnector.newConnector();

  public NeverlangLSPDecompiler() {
    super();
    setInjector(Guice.createInjector(new DexterNeverlangModule()));
    registerDecompilerStrategy(new ModuleDecompiler());
    registerDecompilerStrategy(new BundleDecompiler());
  }

  public Path storeDecompiledFilesInTempDir() throws IOException {
    Path tempDir = Files.createTempDirectory("neverlang-lsp");
    Compiler.logger.info("Created temp dir for decompiled files: " + tempDir);
    decompile().forEach(e -> {
      Path path = tempDir.resolve(e.path());
      var dir = path.getParent().toFile();
      dir.mkdirs();
      if (dir.exists() && dir.isDirectory()) {
        try {
          Files.write(
              path, e.content().getBytes(), StandardOpenOption.WRITE, StandardOpenOption.CREATE);
          path.toFile().setReadOnly();
        } catch (IOException ex) {
          Compiler.logger.log(Level.WARNING, "Could not write decompiled file", ex);
        }
      }
    });
    return tempDir;
  }
}
