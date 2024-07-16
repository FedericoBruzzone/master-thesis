package neverlang.core.typesystem;

public interface CompilationUnitExecutor {

  void registerDependency(
      AbstractCompilationUnit<?> dependee, AbstractCompilationUnit<?> dependent);

  void registerIndependent(AbstractCompilationUnit<?> independent);

  void execute(CompilationContext compilationContext);
}
