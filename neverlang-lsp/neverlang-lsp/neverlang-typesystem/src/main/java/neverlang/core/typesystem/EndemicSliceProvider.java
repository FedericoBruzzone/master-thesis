package neverlang.core.typesystem;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import neverlang.core.typesystem.graph.LSPGraph;
import neverlang.runtime.EndemicSlice;
import neverlang.runtime.Language;

public class EndemicSliceProvider<ID, PRIORITY extends Comparable<PRIORITY>> {
  public AbstractCompilationHelper<ID, PRIORITY> compilationHelper;
  public LSPGraph lspGraph = new LSPGraph();
  public Logger logger = java.util.logging.Logger.getLogger("LSPLogger");

  public static EndemicSliceProvider<String, Priority> base() {
    return new EndemicSliceProvider<>(new CompilationHelper());
  }

  public EndemicSliceProvider(AbstractCompilationHelper<ID, PRIORITY> compilationHelper) {
    this.compilationHelper = compilationHelper;
  }

  public EndemicSliceProvider<ID, PRIORITY> withLogger(Logger logger) {
    this.logger = logger;
    return this;
  }

  public EndemicSliceProvider<ID, PRIORITY> withLspGraph(LSPGraph graph) {
    this.lspGraph = graph;
    return this;
  }

  public EndemicSliceProvider<ID, PRIORITY> withCompilationHelper(
      AbstractCompilationHelper<ID, PRIORITY> compilationHelper) {
    this.compilationHelper = compilationHelper;
    return this;
  }

  private boolean areGraphAndCompilationHelperIn(Map<String, EndemicSlice> endemicSlices) {
    return endemicSlices.values().stream()
        .map(EndemicSlice::getDeclaredStaticInstances)
        .flatMap(e -> e.entrySet().stream())
        .map(Map.Entry::getValue)
        .anyMatch(e -> e instanceof AbstractCompilationHelper<?, ?> || e instanceof LSPGraph);
  }

  public void setTo(Language l) throws NoSuchFieldException, IllegalAccessException {
    Field fieldEndemicSlices =
        Language.class.getSuperclass().getDeclaredField("endemicSliceHashMap");
    fieldEndemicSlices.setAccessible(true);
    Map<String, EndemicSlice> endemicSlices =
        (HashMap<String, EndemicSlice>) fieldEndemicSlices.get(l);
    if (!areGraphAndCompilationHelperIn(endemicSlices)) {
      var slice = l.createInstance(CompilationEndemicSlices.class).of(this);
      slice.lazyLoad();
      endemicSlices.put(
          EndemicSliceProvider.CompilationEndemicSlices.class.getCanonicalName(), slice);
    }
  }

  public static class CompilationEndemicSlices<ID, PRIORITY extends Comparable<PRIORITY>>
      extends EndemicSlice {
    private AbstractCompilationHelper<ID, PRIORITY> compilationHelper;
    private LSPGraph lspGraph;
    private Logger logger;

    public CompilationEndemicSlices() {}

    public CompilationEndemicSlices<ID, PRIORITY> of(EndemicSliceProvider<ID, PRIORITY> provider) {
      this.compilationHelper = provider.compilationHelper;
      this.lspGraph = provider.lspGraph;
      this.logger = provider.logger;
      return this;
    }

    public CompilationEndemicSlices(
        AbstractCompilationHelper<ID, PRIORITY> compilationHelper,
        LSPGraph lspGraph,
        Logger logger) {
      this.compilationHelper = compilationHelper;
      this.lspGraph = lspGraph;
      this.logger = logger;
    }

    public void declarations() {}

    public void staticDeclarations() {

      declareStatic("CompilationHelper", compilationHelper);

      // declareStatic("LSPGraph", new simplelang.graph.LSPGraph()); // TODO: GRAPH
      declareStatic("LSPGraph", lspGraph);

      declareStatic(
          "Logger",
          //                java.util.logging.Logger.getLogger("SimpleLangLSP")
          logger);
    }

    public String[] getTags() {
      return new String[] {""};
    }
  }
}
