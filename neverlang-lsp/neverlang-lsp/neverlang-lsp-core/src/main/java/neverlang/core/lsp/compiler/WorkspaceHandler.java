package neverlang.core.lsp.compiler;

import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Flow;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import neverlang.core.lsp.capabilities.CapabilitiesHolder;
import neverlang.core.lsp.capabilities.Subscribable;
import neverlang.core.lsp.defaults.DefaultFileSystemEventQueue;
import neverlang.core.lsp.services.Workspace;
import neverlang.core.typesystem.*;
import neverlang.core.typesystem.compiler.Compiler;
import neverlang.core.typesystem.compiler.IncrementalCompilationHelper;
import neverlang.core.typesystem.compiler.Source;
import neverlang.core.typesystem.compiler.SourceSet;
import neverlang.core.typesystem.graph.IndexNode;
import neverlang.core.typesystem.graph.Indexable;
import neverlang.core.typesystem.graph.LSPGraph;
import neverlang.core.typesystem.typenv.EntryType;
import neverlang.runtime.Language;
import neverlang.runtime.Role;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class WorkspaceHandler implements Flow.Subscriber<SourceEvent> {
  private final AtomicBoolean isNotInitialized = new AtomicBoolean(true);

  @Nullable
  private final IncrementalCompilationHelper incrementalCompilationHelper;

  private Flow.Subscription subscription;

  @Nullable
  private SourceSet sourceSet;

  private final FileSystemEventQueue fileSystemEventQueue;
  public final Workspace workspace;
  private CapabilitiesHolder capabilityHolder;
  private Compiler compiler;

  public WorkspaceHandler(
      @NotNull Workspace workspace,
      @Nullable IncrementalCompilationHelper incrementalCompilationHelper) {
    this(workspace, incrementalCompilationHelper, new DefaultFileSystemEventQueue());
  }

  public WorkspaceHandler(
      @NotNull Workspace workspace,
      @Nullable IncrementalCompilationHelper incrementalCompilationHelper,
      FileSystemEventQueue fileSystemEventQueue) {
    this.fileSystemEventQueue = fileSystemEventQueue;
    this.incrementalCompilationHelper = incrementalCompilationHelper;
    this.workspace = workspace;
  }

  public void initialize() {
    try {
      Compiler.logger.warning(
          "Initializing workspace with root: " + getRootDir().toString());
      this.sourceSet = getSourceSet(getRootDir());
      var compiler = getCompiler();
      subscribers().forEach(e -> compiler.getCompilationHelper().subscribe(e));
      compile(null);
    } catch (Exception e) {
      Compiler.logger.log(Level.SEVERE, e.getMessage(), e);
    }
    isNotInitialized.getAndSet(true);
  }

  public Stream<Flow.Subscriber<Object>> subscribers() {
    return capabilityHolder.stream()
        .filter(e -> e instanceof Subscribable)
        .map(e -> (Subscribable) e)
        .map(Subscribable::getSubscriber);
  }

  public Path getRootDir() {
    return workspace.path();
  }

  public boolean canHandle(Path path) {
    return path.startsWith(getRootDir());
  }

  public abstract SourceSet getSourceSet(Path rootDir);

  public abstract List<Priority> priorities();

  public abstract Language language();

  public abstract Class<? extends AbstractCompilationHelper<?, ?>> compilationHelper();

  public abstract Stream<Role> lspRoles();

  @NotNull
  public Compiler buildCompiler() {
    Language l = language();

    // TODO: Remove
    // --------------------------------------------------------------------------------
    l.loadLanguage();

    try {
      // ROLES
      var fieldRoles = Language.class.getDeclaredField("roles");
      fieldRoles.setAccessible(true);
      Role[] roles = lspRoles().toArray(Role[]::new);
      fieldRoles.set(l, roles);

      // ENDEMIC SLICES
      new EndemicSliceProvider<>(compilationHelper).setTo(l);

    } catch (NoSuchFieldException | IllegalAccessException e) {
      e.printStackTrace();
    }
    // --------------------------------------------------------------------------------

    var priorities = priorities();
    IntStream.range(0, priorities.size())
        .mapToObj(i -> priorities.get(i).setPriority(i))
        .forEach(e -> e.bind(l));

    return new Compiler(l, compilationHelper());
  }

  public Compiler getCompiler() {
    if (compiler == null) {
      compiler = buildCompiler();
    }
    return compiler;
  }

  public Stream<Source> getSourceSetStream() {
    return Optional.ofNullable(sourceSet).map(SourceSet::getClonedStream).orElse(Stream.empty());
  }

  private void compile(@Nullable Runnable callback) {
    var context = new CompilationContext(
        new CompilationUnitToken(), incrementalCompilationHelper, getSourceSetStream(), callback);
    getCompiler().compile(context);
  }

  @Override
  public void onSubscribe(Flow.Subscription subscription) {
    if (this.subscription != null) {
      throw new IllegalStateException("Subscription already set");
    }
    this.subscription = subscription;
    subscription.request(1);
  }

  @Override
  public void onNext(SourceEvent item) {
    subscription.request(1);
    if (canHandle(item.path())) {
      fileSystemEventQueue.add(item);
      CompletableFuture.runAsync(this::startCompiling);
    }
  }

  private void startCompiling() {
    if (!fileSystemEventQueue.isEmpty() && sourceSet != null) {
      if (getCompiler().isCompiling()) {
        Compiler.logger.info("Is already compiling");
      } else {
        var shouldCompile = new AtomicBoolean(false);
        var updateStream = fileSystemEventQueue
            .getAndClearAll()
            .filter(e -> e.eventCase() != null)
            .peek(e -> shouldCompile.compareAndSet(false, shouldCompile(e)))
            .map(e -> Map.entry(e.path(), Objects.requireNonNull(e.eventCase())));
        sourceSet.updateAll(updateStream);
        if (shouldCompile.get()) {
          compile(this::startCompiling);
        }
      }
    }
  }

  private boolean shouldCompile(SourceEvent item) {
    return item instanceof SaveSourceEvent
        || item instanceof CreateSourceEvent
        || item instanceof RemoveSourceEvent;
  }

  @Override
  public void onError(Throwable throwable) {
    throwable.printStackTrace();
  }

  @Override
  public void onComplete() {
    subscription.cancel();
  }

  protected AbstractCompilationHelper<?, ?> compilationHelper = new CompilationHelper();

  public final AbstractCompilationHelper<?, ?> getCompilationHelper() {
    return compilationHelper;
  }

  protected final void setCompilationHelper(AbstractCompilationHelper<?, ?> compilationHelper) {
    this.compilationHelper = compilationHelper;
  }

  /*
  public AbstractCompilationHelper<?,?> getCompilerHelper() {
      return getCompiler().getHelper();
  }
  */

  public WorkspaceHandler waitUntilIsNotCompiling() {
    var compiler = getCompiler();
    // TODO: check a better way to wait for the compiler to finish
    while (isNotInitialized.get() && compiler.isCompiling()) {
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    return this;
  }

  public <T extends Indexable> Optional<T> lookupToken(Path path, int row, int col) {
    return getIndexTree(path).flatMap(indexNode -> indexNode.lookup(row, col));
  }

  public Optional<EntryType> getDefinition(SymbolTableEntry symbolTableEntry) {
    return getGraph().map(e -> e.getDefinition(symbolTableEntry));
  }

  public Optional<LSPGraph> getGraph() {
    return Optional.ofNullable(getCompiler().getCompilationHelper().getGraph());
  }

  public Optional<IndexNode> getIndexTree(Path path) {
    return getGraph().map(LSPGraph::getIndexStructure).flatMap(e -> e.getIndexTree(path));
  }

  public Optional<Stream<EntryType>> getReferences(SymbolTableEntry symbolTableEntry) {
    return getGraph().map(e -> e.getReferences(symbolTableEntry));
  }

  public void setCapabilityHolder(CapabilitiesHolder capabilityHolder) {
    this.capabilityHolder = capabilityHolder;
  }

  public <RETURN> RETURN dispatch(Object params) {
    return capabilityHolder.dispatch(params);
  }
}
