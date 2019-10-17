package locks;

import java.util.concurrent.atomic.AtomicBoolean;

public class TTASLock implements Lock {
  AtomicBoolean state = new AtomicBoolean(false);

  public void lock(){
    while(true){
      while(state.get()) {}  //Wait until lock looks free
      if (!state.getAndSet(true)){  // then try to acquire it
        return;
      }
    }
  }

  public void unlock(){
    state.set(false);
  }

}
