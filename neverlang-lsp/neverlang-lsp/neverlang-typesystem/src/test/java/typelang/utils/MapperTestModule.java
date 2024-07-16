package typelang.utils;

import java.lang.reflect.Method;
import java.util.Map;
import neverlang.core.typelang.TypeMapperModule;
import neverlang.core.typesystem.defaults.CompilationUnit;
import neverlang.core.typesystem.defaults.DefaultSymbolTableEntry;
import neverlang.core.typesystem.defaults.DefaultSymbolTableEntryFactory;
import neverlang.core.typesystem.graph.LSPGraph;

public class MapperTestModule extends TypeMapperModule {

  public MapperTestModule() {
    super();
  }

  @Override
  public Class<CompilationUnit> compilationUnit() {
    return CompilationUnit.class;
  }

  @Override
  public Class<DefaultSymbolTableEntry> symbolTableEntry() {
    return DefaultSymbolTableEntry.class;
  }

  @Override
  public Class<DefaultSymbolTableEntryFactory> symbolTableEntryFactory() {
    return DefaultSymbolTableEntryFactory.class;
  }

  @Override
  public Class<TestCompilationHelper> compilationHelper() {
    return TestCompilationHelper.class;
  }

  @Override
  public Map<String, String> types() {
    return Map.of(
        "module", "TypeModule",
        "file", "TypeFile",
        "function", "TypeFunction");
  }

  @Override
  public Map<String, String> priorities() {
    return Map.of("module", "Priority.MODULE");
  }

  @Override
  public Map<String, String> details() {
    return Map.of(
        "modifier", "withModifier",
        "visibility", "withVisibility",
        "retention", "withRetention");
  }

  @Override
  protected Map<String, Method> callbacks() {
    return Map.of("validate", getMethod());
  }

  public String customValidationMethod() {
    return "customValidationMethod";
  }

  public static Method getMethod() {
    try {
      return TypeTest.class.getMethod("customMethod");
    } catch (NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Class<? extends LSPGraph> lspGraph() {
    return LSPGraph.class;
  }

  @Override
  public Map<String, String> baseTypes() {
    return null;
  }

  @Override
  public Map<String, String> signatures() {
    return Map.of(
        "function", "FunctionSignature",
        "identifier", "IdentifierSignature");
  }
}
