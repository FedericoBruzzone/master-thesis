package neverlang.core.typesystem.compiler;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import neverlang.core.typesystem.AbstractCompilationHelper;
import neverlang.core.typesystem.CompilationContext;
import neverlang.runtime.Language;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Compiler {
  private final Language language;
  private final AbstractCompilationHelper<?, ?> compilationHelper;

  private final AtomicBoolean isCompiling = new AtomicBoolean(false);

  public static final Logger logger = Logger.getLogger(Compiler.class.getName());

  public Compiler(
      Language language,
      Class<? extends AbstractCompilationHelper<?, ?>> compilationUnitHolderClass) {
    this.language = language;

    var compilationHelper =
        AbstractCompilationHelper.getFromASTNode(language, compilationUnitHolderClass);
    if (compilationHelper.isPresent()) {
      this.compilationHelper = compilationHelper.get();
    } else {
      throw new IllegalStateException("Compilation helper not found");
    }
  }

  public void compile(@Nullable CompilationContext ctx) {
    if (!isCompiling() && ctx != null) {
      isCompiling.set(true);
      compilationHelper.submit(new CompilationStartEvent());
      compilationHelper.setup(ctx);
      var list = ctx.sourceStream()
          .filter(e -> {
            if (ctx.incremental()) {
              return e.dirty();
            } else {
              // pick all
              return true;
            }
          })
          .toList();

      Compiler.logger.info("Compiling %d sources".formatted(list.size()));
      var pathSet = list.stream().map(Source::path).collect(Collectors.toSet());
      compilationHelper.submit(new RecompileEvent(pathSet));
      list.forEach(compileSource());
      ctx.incrementalCompilationHelper()
          .ifPresent(e -> e.sourceToRecompile(compilationHelper, pathSet));
      compilationHelper.eval();
      isCompiling.set(false);
      Compiler.logger.info("Compilation finished");
      compilationHelper.submit(new CompilationEndEvent());
      Optional.ofNullable(ctx.onEnd()).ifPresent(Runnable::run);
    }
  }

  @NotNull
  private Consumer<Source> compileSource() {
    return e -> {
      try {
        // AST SHOULD BE CACHED
        language.exec(e.source(), e.path().toFile());
      } catch (Exception ex) {
        var logRecord = new LogRecord(Level.SEVERE, ex.getMessage());
        logRecord.setThrown(ex);
        logRecord.setParameters(new Object[] {e});
        compilationHelper.submit(logRecord);
      }
    };
  }

  public boolean isCompiling() {
    return isCompiling.get();
  }

  public AbstractCompilationHelper<?, ?> getCompilationHelper() {
    return compilationHelper;
  }

  public Language getLanguage() {
    return language;
  }
}
