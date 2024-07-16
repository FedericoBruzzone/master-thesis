package simplelang.symboltable;

// @TypeLangAnnotation(
//        label = SimpleLangModule.LANGUAGE,
//        kind = TypeSystemKind.PRIORITY
// )
// public enum Priorities implements Comparable<Priorities> {
//    SOURCES,
//    FILE,
//    FUNCTION,
// }

//// Extebnsible enum pattern
// public interface IPrior {
//    String name();
// }
//
//
// @TypeLangAnnotation(...)
// public enum Sources implements IPrior {
//    SOURCES
// }
//
// @TypeLangAnnotation(...)
// public enum File implements IPrior {
//    FILE
// }
//
// public Comparable<IPrior> getPriorities() {
//    var prior = List.of(Sources.SOURCES, File.FILE);
//    return new Comparable<IPrior>() {
//        @Override
//        public int compareTo(@NotNull IPrior o) {
//            return 0;
//        }
//    }
// }
