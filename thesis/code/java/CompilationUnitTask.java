public record CompilationUnitTask<PRIORITY extends Comparable<PRIORITY>>(
    AtomicReference<CompilationUnitToken> token,
    Consumer<Context> consumer,
    Context context,
    Class<? extends AbstractCompilationHelper<?, ?>> aClass,
    PRIORITY priority)
    implements Comparable<CompilationUnitTask<PRIORITY>> {
  public CompilationUnitTask(
      Consumer<Context> consumer,
      Context context,
      Class<? extends AbstractCompilationHelper<?, ?>> aClass,
      PRIORITY priority) {
    this(new AtomicReference<>(), consumer, context, aClass, priority);
  }

  public void run(CompilationContext compilationContext) {

    var oldToken = token.getAndSet(compilationContext.token());
    if (oldToken == null) {
      Compiler.logger.fine("Compilation unit run");
      consumer.accept(context);
    } else if (oldToken.equals(compilationContext.token())) {
      Compiler.logger.fine("Already ran");
    } else if (compilationContext.incremental()) {
      Compiler.logger.fine("Incremental run");
      var helper =
          context.root().<AbstractCompilationHelper<?, ?>>getValue(
                  "$" + aClass.getSimpleName()
          );
      compilationContext
          .incrementalCompilationHelper()
          .ifPresent(e ->
              e.beforeCompilationUnitRecompilation(
                  helper, context, compilationContext.token())
              );
      consumer.accept(context);
    }
  }

  @Override
  public int compareTo(CompilationUnitTask<PRIORITY> o) {
    return this.priority().compareTo(o.priority());
  }

}

