package neverlang.core.typesystem;

import java.util.HashMap;
import java.util.stream.Stream;
import neverlang.runtime.Language;

public abstract class Priority implements Comparable<Priority> {

  private static final HashMap<Language, HashMap<Class<?>, Priority>> instances = new HashMap<>();
  private int priority = 0;

  public final Priority setPriority(int priority) {
    this.priority = priority;
    return this;
  }

  public void bind(Language m) {
    instances.putIfAbsent(m, new HashMap<>());
    instances.get(m).put(this.getClass(), this);
  }

  public static Priority retrive(Language m, Class<? extends Priority> c) {
    return instances.get(m).get(c);
  }

  public static void bindAll(Language m, Stream<Priority> s) {
    s.forEach(e -> e.bind(m));
  }

  public final String getName() {
    return this.getClass().getSimpleName();
  }

  public final int getPriority() {
    return this.priority;
  }

  @Override
  public int compareTo(Priority o) {
    return Integer.compare(this.getPriority(), o.getPriority());
  }
}
