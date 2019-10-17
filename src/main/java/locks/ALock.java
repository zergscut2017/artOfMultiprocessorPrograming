package locks;

import java.util.concurrent.atomic.AtomicInteger;

public class ALock implements Lock {
  ThreadLocal<Integer> mySlot = new ThreadLocal<Integer>(){
    protected Integer initialValue(){
      return 0;
    }
  };
  AtomicInteger next;
  volatile boolean[] flag;
  int size;
  public ALock(int capacity) {
    size = capacity;
    next = new AtomicInteger(0);
    flag = new boolean[capacity];
    flag[0] = true;
  }
  public void lock() {
    int slot = next.getAndIncrement() % size;  // Take next slot
    mySlot.set(slot);
    while(!flag[slot]){}; // spin until told to go
  }

  public void unlock(){
    int slot = mySlot.get();
    flag[slot] = false; //release the lock
    flag[(slot +1)%size] = true;  // tell next thread to go
  }
}
