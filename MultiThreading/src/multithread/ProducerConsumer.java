package multithread;

//import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

//class ProducerConsumer {
//	protected static ProducerConsumer pc = new ProducerConsumer();
//	protected BlockingQueue<Integer> buffer = null;
//	
//	protected ProducerConsumer() {}
//	
//	public static ProducerConsumer getInstance() {
//		return pc;
//	}
//	public void addItem(int item) {
//		try {
//			pc.buffer.put((Integer) item);	// Blocks calling thread if buffer is full
//		}
//		catch(InterruptedException eIE) {
//			System.out.println("Buffer loading interrupted");
//		}
//	}
//	public int removeItem() {
//		int item = 0;
//		try {
//			item = pc.buffer.take();		// Blocks calling thread if buffer is empty
//		}
//		catch(InterruptedException eIE) {
//			System.out.println("Buffer unloading interrupted");
//		}
//		return item;
//	}
//}

// Singleton class to have a single bounded buffer resource  
class ProducerConsumerArray {
	private static ProducerConsumerArray pc = new ProducerConsumerArray();
	private ArrayBlockingQueue<Integer> buffer = null;
	
	private ProducerConsumerArray() {}
	
	public static ProducerConsumerArray getInstance() {
		return pc;
	}
	public void createBuffer(int n) {
		if(pc.buffer == null)
			pc.buffer = new ArrayBlockingQueue<Integer>(n, true);	// fair(ordered) scheduling
		else
			System.out.println("Buffer size cannot be redefined");
	}
	public void addItem(int item) {
		try {
			pc.buffer.put((Integer) item);	// Blocks calling thread if buffer is full
		}
		catch(InterruptedException eIE) {
			System.out.println("Buffer loading interrupted");
		}
	}
	public int removeItem() {
		int item = 0;
		try {
			item = pc.buffer.take();		// Blocks calling thread if buffer is empty
		}
		catch(InterruptedException eIE) {
			System.out.println("Buffer loading interrupted");
		}
		return item;
	}
}

//Singleton class to have a single unbounded buffer resource
class ProducerConsumerList {
	private static ProducerConsumerList pc = new ProducerConsumerList();
	private LinkedBlockingQueue<Integer> buffer = new LinkedBlockingQueue<Integer>();
	
	private ProducerConsumerList() {}
	
	public static ProducerConsumerList getInstance() {
		return pc;
	}

	public void addItem(int item) {
		try {
			pc.buffer.put((Integer) item);	// Blocks calling thread if buffer is full
		}
		catch(InterruptedException eIE) {
			System.out.println("Buffer loading interrupted");
		}
	}
	public int removeItem() throws InterruptedException {
		int item = 0;
		item = pc.buffer.take();		// Blocks calling thread if buffer is empty
		return item;
	}
	public String dumpBuffer() {
		return buffer.toString();
	}
}


// Class to implement Producer 
class Producer extends Thread {
//	ProducerConsumer pcBuffer = ProducerConsumer.getInstance();
	ProducerConsumerList pcBuffer = ProducerConsumerList.getInstance();
	volatile int nJob;
	volatile int nJobCompleted;
	
	public Producer(String name) {
		this.setName(name);
		nJob = -1;
		nJobCompleted = 0;
	}
	public Producer(String name, int nJob) {
		this.setName(name);
		this.nJob = nJob;
		nJobCompleted = 0;
	}
	
	public void setJobCount(int nJob) {
		this.nJob = nJob;
		nJobCompleted = 0;
	}
	public boolean isWorking() {
		return (nJob == 0 ? true : (nJobCompleted < nJob));
	}
	
	// "nJob = 0" is interpreted as to produce infinite jobs
	public void run() {
		while(!Thread.currentThread().isInterrupted()){
			// If current work is completed, wait till next set of jobs assigned or interrupted
			if(nJob > 0 && nJobCompleted >= nJob) continue;
			
			// Do the work
			if (nJob > 0) {
				for(int i=1 ; i<=nJob ; i++)
					generateAndAddItem();
			}
			else if(nJob == 0){
				while(true)
					generateAndAddItem();
			}
		}
		System.out.println("Producer-" + this.getName() + " stopped");
	}
	
	void generateAndAddItem() {
		int item;
		item = nextItem();
		pcBuffer.addItem(item);
		nJobCompleted++;
		//System.out.println("Producer-" + this.getName() + " added item#" + item + " to queue");
	}
	
	int nextItem() {
		return (int) (Math.random() * 5 + 1);	// Returns a random integer in range [1,6)
	}
}

// Class to implement Consumer 
class Consumer extends Thread {
//	ProducerConsumer pcBuffer = ProducerConsumer.getInstance();
	ProducerConsumerList pcBuffer = ProducerConsumerList.getInstance();
	int jobCount;
	
	public Consumer(String name) {
		this.setName(name);
		jobCount = 0;
	}
	
	public void run() {
		int item;
		try {
			while(!Thread.currentThread().isInterrupted()) {
				// Retrieve an item from the buffer
				//System.out.println("Consumer-" + this.getName() + " attempting to consume item from queue");
				item = pcBuffer.removeItem();
				//System.out.println("Consumer-" + this.getName() + " consumed item#" + item);
				jobCount++;
				// Thread.sleep(item * 1000);	// Wait for 'item' seconds
			}
		}
		catch(InterruptedException eIE) {
			System.out.print("Terminated " + this.getName() + "! ");
		}
		finally {
			System.out.println("Consumer-" + this.getName() + " stopped");
		}
	}
	
	public void displayStat() {
		System.out.println("Consumer " + this.getName() + " consumed " + jobCount + " jobs");
	}
	
	public void resetStat() {
		jobCount = 0;
	}
}

