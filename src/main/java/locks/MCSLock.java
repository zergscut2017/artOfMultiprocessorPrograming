package locks;

import java.util.concurrent.atomic.AtomicReference;

public class MCSLock implements Lock {
  AtomicReference<QNode> tail;
  ThreadLocal<QNode> myNode;
  public MCSLock(){
    tail = new AtomicReference<QNode>(null);
    myNode = new ThreadLocal<QNode>(){
      protected  QNode initialValue(){
        return new QNode();
      }
    };
  }

  public void lock() {
    QNode qnode = myNode.get();
    QNode pred = tail.getAndSet(qnode);   // add myNode to tail of queue
    if (pred != null) {         // if the queue is not empty
      qnode.locked.set(true);   // set my state to wait the lock is free
      pred.next = qnode;      // set next of predecessor to me
      while (qnode.locked.get()) {}   // wait until unlocked
    }
  }

  public  void unlock() {
    QNode qnode = myNode.get();
    if(qnode.next == null) {        // if I don't have any successor
      if (tail.compareAndSet(qnode, null))     // double check the tail of queue is me and set the tail to null
        return;                                       // if really no other in the tail, return
      while (qnode.next == null) {}                   // if the tail is not me, there must be someone else become the tail
                                                      // wait the successor catchup (let the next be the successor)
    }
    qnode.next.locked.set(false);                     // pass lock to successor
    qnode.next = null;
  }

}
