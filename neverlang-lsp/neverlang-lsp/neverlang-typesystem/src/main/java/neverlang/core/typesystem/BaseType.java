package neverlang.core.typesystem;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.function.Supplier;
import neverlang.core.typesystem.symboltable.EntryKind;

public abstract class BaseType<ID, FACTORY extends SymbolTableEntryFactory<ID, ?>> {
  protected SymbolTableEntryFactory<ID, FACTORY> symbolTableEntryFactory;
  protected AbstractCompilationHelper compilationHelper;
  protected Scope<ID> scope;
  private Supplier<SymbolTableEntryFactory<?, ?>> symbolTableEntryFactorySupplier;

  public BaseType withScope(Scope<ID> scope) {
    this.scope = scope;
    return this;
  }

  public BaseType withSymbolTableEntryFactory(
      SymbolTableEntryFactory<ID, FACTORY> symbolTableEntryFactory) {
    this.symbolTableEntryFactory = symbolTableEntryFactory;
    this.symbolTableEntryFactorySupplier = () -> {
      try {
        return symbolTableEntryFactory.getClass().getConstructor().newInstance();
      } catch (InstantiationException
          | IllegalAccessException
          | InvocationTargetException
          | NoSuchMethodException e) {
        throw new RuntimeException(e);
      }
    };
    return this;
  }

  public BaseType withCompilationHelper(AbstractCompilationHelper compilationHelper) {
    this.compilationHelper = compilationHelper;
    return this;
  }

  public abstract BaseTypeParams<ID> baseTypeParams();

  public void bind() {
    if (compilationHelper == null || scope == null || symbolTableEntryFactory == null) {
      throw new IllegalStateException((compilationHelper == null ? "Compilation helper, " : "")
          + (scope == null ? "Scope, " : "")
          + (symbolTableEntryFactory == null ? "SymbolTableEntryFactory, " : "")
          + "cannot be null");
    }

    BaseTypeParams<ID> baseTypeParams = baseTypeParams();

    Type entryType = baseTypeParams
        .type
        .withCompilationHelper(compilationHelper)
        .withSymbolTableEntryFactory(symbolTableEntryFactorySupplier)
        .bind(baseTypeParams.neededType);

    symbolTableEntryFactory
        .inScope(scope)
        .withEntryKind(baseTypeParams.entryKind)
        .withIdentifier(baseTypeParams.identifier)
        .withEntryType(entryType)
        .withCompilationHelper(compilationHelper)
        .withEntryDetails(baseTypeParams.entryDetails)
        .bind();
  }

  public record BaseTypeParams<ID>(
      EntryKind entryKind,
      ID identifier,
      Type type,
      List<Type> neededType,
      EntryDetails entryDetails) {}
}
