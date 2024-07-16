// package simplelang.symboltable;
//
// import neverlang.core.typesystem.*;
// import neverlang.core.typesystem.symboltable.EntryKind;
// import simplelang.SimpleLangModule;
// import simplelang.typesystem.BaseLang;
//// import simplelang.typesystem.NeededTypes.BifunctionNeededType;
// import neverlang.core.lsp.defaults.types.TypeSourceSet;
// import neverlang.core.typelang.annotations.TypeLangAnnotation;
// import neverlang.core.typelang.annotations.TypeSystemKind;
//
// import java.util.ArrayList;
// import java.util.List;
// import java.util.stream.Stream;
//
// @TypeLangAnnotation(
//        label = SimpleLangModule.LANGUAGE,
//        kind = TypeSystemKind.COMPILATION_HELPER
// )
// public class CompilationHelper extends AbstractCompilationHelper<String, Priority> {
//
//    @Override
//    public void beforeAll() {
////        BaseLang.compilationHelper = this;
////        BaseLang.bindBaseLang(getRoot());
//    }
//
//    @Override
//    protected Scope<String> generateRootType() {
//        return new TypeSourceSet();
//    }
// }
