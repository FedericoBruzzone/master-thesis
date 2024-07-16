package neverlang.core.lsp.compiler;

import java.util.stream.Stream;

public interface FileSystemEventQueue {

  // ALL EVENTS SHOULD BE ADDED IN A SMART WAY
  // i.e if a file is saved 3 times in a row, only the last one should be compiled
  // i.e if a file is saved and then removed, it should not be compiled
  void add(SourceEvent event);

  Stream<SourceEvent> getAndClearAll();

  boolean isEmpty();
}
