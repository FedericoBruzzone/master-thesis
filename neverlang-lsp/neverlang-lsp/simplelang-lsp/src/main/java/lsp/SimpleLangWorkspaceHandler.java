package lsp;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;
import neverlang.core.lsp.compiler.WorkspaceHandler;
import neverlang.core.lsp.defaults.priorities.File;
import neverlang.core.lsp.defaults.priorities.Function;
import neverlang.core.lsp.defaults.priorities.Sources;
import neverlang.core.lsp.defaults.symboltable.CompilationHelper;
import neverlang.core.lsp.services.Workspace;
import neverlang.core.typesystem.AbstractCompilationHelper;
import neverlang.core.typesystem.Priority;
import neverlang.core.typesystem.compiler.SourceSet;
import neverlang.core.typesystem.defaults.DefaultIncrementalCompilationHelper;
import neverlang.core.typesystem.defaults.DefaultSourceSet;
import neverlang.runtime.*;
import simplelang.SimpleLang;
import simplelang.SimpleLangModule;

public class SimpleLangWorkspaceHandler extends WorkspaceHandler {

  public SimpleLangWorkspaceHandler() {
    super(new Workspace(Path.of(""), ""), new DefaultIncrementalCompilationHelper());
    setCompilationHelper(new CompilationHelper());
  }

  public SimpleLangWorkspaceHandler(Workspace workspace) {
    super(workspace, new DefaultIncrementalCompilationHelper());
    setCompilationHelper(new CompilationHelper());
  }

  @Override
  public SourceSet getSourceSet(Path rootDir) {
    return new DefaultSourceSet.Builder(".sl").buildFromRootDir(rootDir);
  }

  @Override
  public Language language() {
    return new SimpleLang(new SimpleLangModule());
  }

  @Override
  public Stream<Role> lspRoles() {
    return Stream.of(new LayeredRole(List.of(
        new Role("before_each", Role.Flags.MANUAL), new Role("type_checker", Role.Flags.MANUAL))));
  }

  @Override
  public Class<? extends AbstractCompilationHelper<?, ?>> compilationHelper() {
    return CompilationHelper.class;
  }

  @Override
  public List<Priority> priorities() {
    return List.of(new Sources(), new File(), new Function());
  }
}
