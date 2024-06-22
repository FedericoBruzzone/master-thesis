public class TypingEnvironment<IDENTIFIER> {

  private final HashMap<IDENTIFIER, EntryTypeBinder> map;
  private final Class<? extends EntryTypeBinder> typeBinderClass;

  private TypingEnvironment(HashMap<IDENTIFIER, EntryTypeBinder> map,
                            Class<? extends EntryTypeBinder> typeBinderClass) {
    this.map = map;
    this.typeBinderClass = typeBinderClass;
  }

  public TypingEnvironment<IDENTIFIER> bindTypeToIdentifier(
    IDENTIFIER variable,
    SymbolTableEntry anyType) {
    try {
      var typeBinder = map.getOrDefault(variable, this.typeBinderClass
                                                      .getConstructor()
                                                      .newInstance());
      typeBinder.bindEntry(anyType);
      map.putIfAbsent(variable, typeBinder);
    } catch (InstantiationException | IllegalAccessException
            | InvocationTargetException | NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
    return this;
  }

  public Stream<SymbolTableEntry> getTypesBoundedWith(IDENTIFIER t) {
    if (map.containsKey(t)) { return map.get(t).stream(); }
    else { return Stream.empty(); }
  }

  public void removeIf(Predicate<SymbolTableEntry> predicate) {
    map.entrySet().stream()
      .peek(e -> e.getValue().removeIf(predicate))
      .filter(e -> !e.getValue().isBound())
      .map(Map.Entry::getKey)
      .distinct()
      .toList()
      .forEach(map::remove);
  }

  public Stream<Map.Entry<IDENTIFIER, EntryTypeBinder>> stream() {
    return map.entrySet().stream();
  }

  public static class Builder<IDENTIFIER> {

    private Class<? extends EntryTypeBinder> typeBinderClass =
        MultipleTypeTypeBinder.class;

    public Builder<IDENTIFIER> setTypeBinder(
        Class<? extends EntryTypeBinder> typeBinderClass) {
      this.typeBinderClass = typeBinderClass;
      return this;
    }

    public TypingEnvironment<IDENTIFIER> build() {
      return new TypingEnvironment<>(new HashMap<>(), typeBinderClass);
    }
  }

}
