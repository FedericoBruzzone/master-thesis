package neverlang.core.lsp.defaults.types;

import java.util.ArrayList;
import java.util.List;
import neverlang.core.lsp.defaults.types.categories.Category;
import neverlang.core.lsp.defaults.types.categories.RegexCategory;
import neverlang.core.lsp.defaults.types.categories.StringCategory;
import neverlang.core.typelang.annotations.TypeAnnotation;
import neverlang.core.typelang.annotations.TypeEnum;
import neverlang.core.typelang.annotations.TypeLangAnnotation;
import neverlang.core.typelang.annotations.TypeSystemKind;
import neverlang.core.typesystem.SymbolTableEntry;
import neverlang.core.typesystem.defaults.DefaultTypeScope;
import neverlang.core.typesystem.defaults.SingleTypeTypeBinder;
import neverlang.core.typesystem.typenv.EntryTypeBinder;

@TypeAnnotation(TypeEnum.CATEGORY)
@TypeLangAnnotation(keyword = "category", kind = TypeSystemKind.TYPE)
public class TypeCategory extends DefaultTypeScope {

  List<Category> categories = new ArrayList<>();

  @Override
  public String id() {
    return "category";
  }

  @Override
  public Class<? extends EntryTypeBinder> getTypeBinder() {
    return SingleTypeTypeBinder.class;
  }

  public void addString(String regex) {
    categories.add(new StringCategory(regex));
  }

  public void addRegex(SymbolTableEntry entry) {
    if (entry.type() instanceof TypeRegex typeRegex) {
      categories.add(new RegexCategory(typeRegex.getRegex()));
    }
  }
}
