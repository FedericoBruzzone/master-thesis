package neverlang.compiler.lsp.handlers;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;
import neverlang.compiler.lsp.NeverlangLangLSP;
import neverlang.compiler.lsp.NeverlangLangLSPModule;
import neverlang.compiler.lsp.typesystem.priorities.Bundle;
import neverlang.compiler.lsp.typesystem.priorities.Slice;
import neverlang.compiler.lsp.typesystem.priorities.Syntax;
import neverlang.core.lsp.compiler.WorkspaceHandler;
import neverlang.core.lsp.defaults.priorities.*;
import neverlang.core.lsp.defaults.symboltable.CompilationHelper;
import neverlang.core.lsp.services.Workspace;
import neverlang.core.typesystem.AbstractCompilationHelper;
import neverlang.core.typesystem.Priority;
import neverlang.core.typesystem.compiler.SourceSet;
import neverlang.core.typesystem.defaults.DefaultIncrementalCompilationHelper;
import neverlang.core.typesystem.defaults.DefaultSourceSet;
import neverlang.runtime.Language;
import neverlang.runtime.LayeredRole;
import neverlang.runtime.Role;

public class NeverlangWorkspaceHandler extends WorkspaceHandler {

  public NeverlangWorkspaceHandler(Workspace workspace) {
    super(workspace, new DefaultIncrementalCompilationHelper());
    setCompilationHelper(new CompilationHelper());
  }

  @Override
  public SourceSet getSourceSet(Path rootDir) {
    return new DefaultSourceSet.Builder(".nl").buildFromRootDir(rootDir);
  }

  @Override
  public Language language() {
    return new NeverlangLangLSP(new NeverlangLangLSPModule());
  }

  @Override
  public Class<? extends AbstractCompilationHelper<?, ?>> compilationHelper() {
    return CompilationHelper.class;
  }

  @Override
  public Stream<Role> lspRoles() {
    return Stream.of(new LayeredRole(List.of(
        new Role("before_each", Role.Flags.MANUAL), new Role("type_checker", Role.Flags.MANUAL))));
  }

  @Override
  public List<Priority> priorities() {
    return List.of(
        new Sources(),
        new neverlang.core.lsp.defaults.priorities.Module(),
        new Syntax(),
        new neverlang.compiler.lsp.typesystem.priorities.Role(),
        new Slice(),
        new Bundle(),
        new neverlang.core.lsp.defaults.priorities.Language());
  }
}
