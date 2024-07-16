package neverlang.core.lsp.defaults;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Stream;
import neverlang.core.lsp.compiler.FileSystemEventQueue;
import neverlang.core.lsp.compiler.SourceEvent;

public class DefaultFileSystemEventQueue implements FileSystemEventQueue {
  private final ConcurrentLinkedQueue<SourceEvent> events = new ConcurrentLinkedQueue<>();

  @Override
  public void add(SourceEvent event) {
    events.add(event);
  }

  @Override
  public Stream<SourceEvent> getAndClearAll() {
    List<SourceEvent> list = new ArrayList<>(events);
    events.clear();
    return list.stream();
  }

  @Override
  public boolean isEmpty() {
    return events.isEmpty();
  }
}
