package neverlang.core.typelang;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.MapBinder;
import com.google.inject.name.Names;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import neverlang.core.typelang.blck.BlockBundle;
import neverlang.core.typelang.stmt.StmtBundle;
import neverlang.core.typelang.types.TypesBundle;
import neverlang.core.typesystem.*;
import neverlang.core.typesystem.graph.LSPGraph;
import neverlang.core.typesystem.symboltable.EntryKind;
import neverlang.parser.Production;
import neverlang.runtime.*;

public abstract class TypeMapperModule extends AbstractModule {

  public static final String symbolTableEntry = "symbolTableEntry";
  public static final String compilationUnit = "compilationUnit";
  public static final String lspGraph = "lspGraph";
  public static final String symbolTableEntryFactory = "symbolTableEntryFactory";
  public static final String compilationHelper = "compilationHelper";
  public static final String signatures = "signatures";
  public static final String types = "types";
  public static final String entries = "entries";
  public static final String priorities = "priorities";
  public static final String details = "details";
  public static final String variance = "variance";
  public static final String callbacks = "callbacks";
  public static final String baseTypes = "baseTypes";

  private Language lang;

  public abstract Class<? extends AbstractCompilationUnit<?>> compilationUnit();

  public abstract Class<? extends SymbolTableEntry> symbolTableEntry();

  public abstract Class<? extends SymbolTableEntryFactory<?, ?>> symbolTableEntryFactory();

  public abstract Class<? extends AbstractCompilationHelper<?, ?>> compilationHelper();

  public abstract Map<String, String> types();

  public abstract Map<String, String> priorities();

  public abstract Map<String, String> details();

  protected abstract Map<String, Method> callbacks();

  public abstract Class<? extends LSPGraph> lspGraph();

  public abstract Map<String, String> baseTypes();

  public Map<String, String> variance() {
    var map = new HashMap<String, String>();
    TypeLangGenerator.extractFromEnumPriority(Variance.class, map);
    return map;
  }

  public Map<String, String> entries() {
    var map = new HashMap<String, String>();
    TypeLangGenerator.extractFromEnumPriority(EntryKind.class, map);
    return map;
  }

  public abstract Map<String, String> signatures();

  public Language getLanguage(DexterNeverlangModule dexterNeverlangModule) {
    if (this.lang == null) {
      lang = new Language(dexterNeverlangModule, this) {
        @Override
        public void lazyLoad() {
          importEndemicSlices(EndemicSlices.class.getCanonicalName());
          importSlices(TypeLangMain.class.getCanonicalName());
          importBundles(
              BlockBundle.class.getCanonicalName(),
              StmtBundle.class.getCanonicalName(),
              TypesBundle.class.getCanonicalName());
          declare(role(Role.Flags.POSTORDER, "translate"));
        }
      };
      addSliceToLanguage(lang, "Signature", signatures().keySet());
      addSliceToLanguage(lang, "Priority", priorities().keySet());
      addSliceToLanguage(lang, "Type", types().keySet());
      addSliceToLanguage(lang, "Detail", details().keySet());
      addSliceToLanguage(lang, "TypeVariance", variance().keySet());
      addSliceToLanguage(lang, "Callback", callbacks().keySet());
    }
    return lang;
  }

  public void addSliceToLanguage(
      Language language, String productionName, Set<String> productions) {
    if (productions.isEmpty()) return;

    var syntax = new Syntax() {};
    syntax.declareProductions(productions.stream()
        .map(e -> syntax.p(syntax.nt(productionName), e).setScore(0))
        .toArray(Production[]::new));
    // SLICE DEFINITION
    var slice = new Slice() {};
    slice.setSyntax(syntax);
    language.addSlice(productionName + "Slice", slice);
  }

  @Override
  protected void configure() {
    super.configure();
    bind(String.class)
        .annotatedWith(Names.named(symbolTableEntry))
        .toInstance(symbolTableEntry().getCanonicalName());
    bind(String.class)
        .annotatedWith(Names.named(symbolTableEntryFactory))
        //                .toInstance(symbolTableEntryFactory().getSimpleName());
        .toInstance(symbolTableEntryFactory().getCanonicalName());
    bind(String.class)
        .annotatedWith(Names.named(compilationHelper))
        .toInstance(compilationHelper().getSimpleName());
    bind(String.class)
        .annotatedWith(Names.named(compilationUnit))
        .toInstance(compilationUnit().getSimpleName());
    bind(String.class)
        .annotatedWith(Names.named(lspGraph))
        .toInstance(lspGraph().getSimpleName());
    bind(new TypeLiteral<Class<? extends AbstractCompilationUnit<?>>>() {})
        .annotatedWith(Names.named(compilationUnit))
        .toInstance(compilationUnit());
    bind(new TypeLiteral<Class<? extends LSPGraph>>() {})
        .annotatedWith(Names.named(lspGraph))
        .toInstance(lspGraph());
    bindMap(types, types());
    bindMap(priorities, priorities());
    bindMap(entries, entries());
    bindMap(variance, variance());
    bindMap(signatures, signatures());
    bindMap(details, details());
    bindMap(callbacks, callbacks(), Method.class);
    bindMap(baseTypes, baseTypes());
  }

  public MapBinder<String, String> bindMap(String annotation, Map<String, String> map) {
    if (map == null) return null;
    return bindMap(annotation, map, String.class);
  }

  public <T> MapBinder<String, T> bindMap(String annotation, Map<String, T> map, Class<T> tClass) {

    MapBinder<String, T> entriesBinder = MapBinder.newMapBinder(
        binder(), // the binder object
        String.class, // the key type
        tClass, // the value type,
        Names.named(annotation) // AnnotationType
        );

    map.forEach((key, value) -> entriesBinder.addBinding(key).toInstance(value));
    return entriesBinder;
  }
}
