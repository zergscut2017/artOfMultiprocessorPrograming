package locks;

import java.util.concurrent.atomic.AtomicBoolean;

public class QNode {
  AtomicBoolean locked = new AtomicBoolean(true);
  QNode next = null;    // used by MSCLock
}

