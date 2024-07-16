package neverlang.core.typelang;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class NamingHelper {
  private final AtomicBoolean hasHelper = new AtomicBoolean(false);
  private final AtomicBoolean hasUnit = new AtomicBoolean(false);

  private final Map<String, AtomicInteger> counters = new HashMap<>();

  public NamingHelper() {}

  public String current(String key) {
    counters.putIfAbsent(key, new AtomicInteger(-1));
    return key + counters.get(key).get();
  }

  public String currentPlus(String key, int sum) {
    counters.putIfAbsent(key, new AtomicInteger(-1));
    return key + (counters.get(key).get() + sum);
  }

  public String next(String key) {
    counters.putIfAbsent(key, new AtomicInteger(-1));
    return key + counters.get(key).incrementAndGet();
  }

  public boolean setHelper() {
    return hasHelper.getAndSet(true);
  }

  public boolean setUnit() {
    return hasUnit.getAndSet(true);
  }

  public boolean hasHelper() {
    return hasHelper.get();
  }

  public boolean hasUnit() {
    return hasUnit.get();
  }
}
