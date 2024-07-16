package simplelang.typesystem;

import neverlang.core.lsp.defaults.symboltable.CompilationHelper;
import neverlang.core.lsp.defaults.types.TypeFunction;
import neverlang.core.lsp.defaults.types.TypePrimitive;
import neverlang.core.lsp.defaults.types.functions.AbstractTypeFunction;
import neverlang.core.typesystem.Scope;
import neverlang.core.typesystem.Type;
import neverlang.core.typesystem.symboltable.EntryKind;
import simplelang.symboltable.SymbolTableEntryFactory;

// import simplelang.symboltable.CompilationHelper;

public class BaseLang {

  public static final TypePrimitive intType = TypePrimitive.of(TypePrimitive.Name.INT);
  public static final TypePrimitive booleanType = TypePrimitive.of(TypePrimitive.Name.BOOLEAN);

  public static CompilationHelper compilationHelper;

  public static void bindBaseLang(Scope<String> scope) {
    new SymbolTableEntryFactory()
        .withEntryKind(EntryKind.DEFINE)
        .inScope(scope)
        .withIdentifier("int")
        .withEntryType(intType)
        .withCompilationHelper(compilationHelper)
        .bind();
    new SymbolTableEntryFactory()
        .inScope(scope)
        .withEntryKind(EntryKind.DEFINE)
        .withIdentifier("boolean")
        .withEntryType(booleanType)
        .withCompilationHelper(compilationHelper)
        .bind();
    new SymbolTableEntryFactory()
        .inScope(scope)
        .withEntryKind(EntryKind.DEFINE)
        .withIdentifier("==")
        .withEntryType(biFunction(intType, intType, booleanType))
        .withCompilationHelper(compilationHelper)
        .bind();
    new SymbolTableEntryFactory()
        .inScope(scope)
        .withEntryKind(EntryKind.DEFINE)
        .withIdentifier("+")
        .withEntryType(biFunction(intType, intType, intType))
        .withCompilationHelper(compilationHelper)
        .bind();
    new SymbolTableEntryFactory()
        .inScope(scope)
        .withEntryKind(EntryKind.DEFINE)
        .withIdentifier("-")
        .withEntryType(biFunction(intType, intType, intType))
        .withCompilationHelper(compilationHelper)
        .bind();
  }

  private static AbstractTypeFunction biFunction(Type a1, Type a2, Type returntype) {
    var function = new TypeFunction();
    new SymbolTableEntryFactory()
        .withEntryKind(EntryKind.DEFINE)
        .withPosition(0)
        .withIdentifier("x")
        .withEntryType(a1)
        .inScope(function)
        .withCompilationHelper(compilationHelper)
        .bind();
    new SymbolTableEntryFactory()
        .withEntryKind(EntryKind.DEFINE)
        .withPosition(1)
        .withIdentifier("y")
        .withEntryType(a2)
        .inScope(function)
        .withCompilationHelper(compilationHelper)
        .bind();
    new SymbolTableEntryFactory()
        .withEntryKind(EntryKind.DEFINE)
        .withIdentifier("return")
        .withEntryType(returntype)
        .inScope(function)
        .withCompilationHelper(compilationHelper)
        .bind();
    return function;
  }
}
