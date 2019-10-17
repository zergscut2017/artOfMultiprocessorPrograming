package locks;

import java.util.concurrent.atomic.AtomicReference;

public class CLHLock implements Lock{
  AtomicReference<QNode> tail;
  ThreadLocal<QNode> myPred;
  ThreadLocal<QNode> myNode;
  public CLHLock(){
    tail = new AtomicReference<QNode>(null);
    myNode = new ThreadLocal<QNode>(){
      protected QNode initialValue() {
        return new QNode();
      }
    };
    myPred = new ThreadLocal<QNode>(){
      protected QNode initialValue() {
        return null;
      }
    };
  }

  public void lock() {
    QNode qnode = myNode.get();
    qnode.locked.set(true);
    QNode pred = tail.getAndSet(qnode);   // swap in mynode to the tail of the queue
    myPred.set(pred);
    while (pred.locked.get()) {}    // spin until predecessor release lock
  }

  public void unlock() {
    QNode qnode = myNode.get();
    qnode.locked.set(false);    // notify successor
    myNode.set(myPred.get());   // recycle predecessor's node
  }

}
