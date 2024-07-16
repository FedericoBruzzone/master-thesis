package neverlang.core.lsp.defaults;

import java.util.concurrent.Flow;
import java.util.function.Predicate;

public class CompilationSubscriber implements Flow.Subscriber<Object> {

  private final Predicate<Object> predicate;
  private final Runnable runnable;
  private Flow.Subscription subscription;

  public CompilationSubscriber(Predicate<Object> predicate, Runnable runnable) {
    this.predicate = predicate;
    this.runnable = runnable;
  }

  @Override
  public void onSubscribe(Flow.Subscription subscription) {
    this.subscription = subscription;
    subscription.request(1);
  }

  @Override
  public void onNext(Object item) {
    if (predicate.test(item)) {
      runnable.run();
    }
    subscription.request(1);
  }

  @Override
  public void onError(Throwable throwable) {
    subscription.request(1);
  }

  @Override
  public void onComplete() {
    subscription.cancel();
  }
}
