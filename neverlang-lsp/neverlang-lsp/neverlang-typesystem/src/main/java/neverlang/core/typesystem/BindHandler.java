package neverlang.core.typesystem;

public interface BindHandler<T extends SymbolTableEntryFactory> {
  void onBinding(T symbolTableEntryFactory);
}
