package neverlang.core.typelang.annotations;

public enum TypeSystemKind {
  TYPE,
  PRIORITY,
  SIGNATURE,
  COMPILATION_HELPER,
  SYMBOL_TABLE_ENTRY_FACTORY,
  COMPILATION_UNIT,
  DEPENDENCY_GRAPH,
  SYMBOL_TABLE_ENTRY,

  NEEDED_TYPE, // Used by base types
  BASE_TYPE // New entry type
}
