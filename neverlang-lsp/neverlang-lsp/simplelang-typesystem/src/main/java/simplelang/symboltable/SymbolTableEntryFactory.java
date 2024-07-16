package simplelang.symboltable;

import neverlang.core.lsp.defaults.typenv.OperatorEntry;
import neverlang.core.lsp.defaults.typenv.ParamTypeEntry;
import neverlang.core.lsp.defaults.typenv.ReturnEntry;
import neverlang.core.typelang.annotations.TypeLangAnnotation;
import neverlang.core.typelang.annotations.TypeSystemKind;
import neverlang.core.typesystem.EntryDetails;
import neverlang.core.typesystem.symbols.Token;
import simplelang.SimpleLangModule;

@TypeLangAnnotation(
    label = SimpleLangModule.LANGUAGE,
    kind = TypeSystemKind.SYMBOL_TABLE_ENTRY_FACTORY)
public class SymbolTableEntryFactory
    extends neverlang.core.typesystem.SymbolTableEntryFactory<String, SymbolTableEntryFactory> {

  private boolean isOperator;

  @Override
  public String getIdentifierFromToken(Token token) {
    return token.text();
  }

  private boolean isReturn() {
    return getIdentifier().equals("return");
  }

  @Override
  public EntryDetails entryDetails() {
    if (entryDetails != null) {
      return entryDetails;
    }

    if (isOperator) {
      return new OperatorEntry();
    } else if (isReturn()) {
      return new ReturnEntry();
    } else if (position != null) {
      return new ParamTypeEntry(position);
    } else {
      return super.entryDetails();
    }
  }

  public SymbolTableEntryFactory isOperator() {
    this.isOperator = true;
    return this;
  }
}
