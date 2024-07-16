package neverlang.core.lsp.capabilities;

import java.util.concurrent.Flow;

public interface Subscribable {
  Flow.Subscriber<Object> getSubscriber();
}
