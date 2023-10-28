package asd3;


public class Waiter {
	public static final Object monitor = new Object();
	public static boolean monitorState = false;
	
	public static void waitForThread() {
	  monitorState = true;
	  while (monitorState) {
	    synchronized (monitor) {
	      try {
	        monitor.wait(); // wait until notified
	      } catch (Exception e) {}
	    }
	  }
	}
	
	public static void unlockWaiter() {
	  synchronized (monitor) {
	    monitorState = false;
	    monitor.notifyAll(); // unlock again
	  }
	}
}
