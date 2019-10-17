package locks;

public interface Lock {
  public void lock() throws InterruptedException;
  public void unlock();
}
