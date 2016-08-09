package multithread;

public class TestThread extends Thread {
	public TestThread() {
		this.setName("Unnamed");
	}
	public TestThread( String name ) {
		this.setName(name);
	}
	
	public void run() {
		int num;
		try {
			System.out.println("Thread-" + this.getName() + " running");
			while( true ) {
				num = nextRandom(10, 50);
				if( num >= 40 )
					break;
				else
					System.out.println("Thread-" + this.getName() + ": " + num);
				Thread.sleep(1000);
			}
			System.out.println("Thread-" + this.getName() + " completed");
		}
		catch (InterruptedException eIE) {
			System.out.println("Thread-" + this.getName() + " interrupted!");
		}
	}
	
	int nextRandom(int min, int max) {
		int range = (max - min) + 1;
		return (int) (Math.random() * range + min);
	}
}
