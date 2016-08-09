package multithread;

public class MultiThread {

	public static void main(String[] args) {
		// Testing thread programming
		Thread t1 = new TestThread();
		t1.start();

		Thread t2 = new TestThread("t2");
		t2.start();
	}

}
