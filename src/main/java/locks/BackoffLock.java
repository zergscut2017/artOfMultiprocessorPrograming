package locks;

import java.util.concurrent.atomic.AtomicBoolean;

import static java.lang.Math.random;

public class BackoffLock implements Lock {
  private static final int MIN_DELAY = 10;
  private static final int MAX_DELAY = 100;
  private AtomicBoolean state = new AtomicBoolean(false);

  public void lock() throws InterruptedException {
    int delay = MIN_DELAY; // minimum delay
    while (true) {
      while (state.get()){};      // wait until the lock looks free
      if (!state.getAndSet(true)){  // try to acquire it
        return;     // if win, return
      }
      Thread.sleep((long) (random() % delay));  // backoff for a random delay
      if (delay < MAX_DELAY) {
        delay = 2 * delay;  // double max delay
      }
    }
  }

  public void unlock() {
    state.set(false);
  }

}
