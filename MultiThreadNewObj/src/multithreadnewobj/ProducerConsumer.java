package multithreadnewobj;

import java.util.concurrent.LinkedBlockingQueue;


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
	public int getSize() {
		return pc.buffer.size();
	}
}


// Class to implement Producer 
class Producer extends Thread {
	ProducerConsumerList pcBuffer = ProducerConsumerList.getInstance();
	volatile int nJob;
	static volatile int nWorkingProducers = 0;
	
	public Producer(String name) {
		this.setName(name);
		nJob = -1;
	}
	public Producer(String name, int nJob) {
		this.setName(name);
		this.nJob = nJob;
	}
	
	public static synchronized void setProducerCount(int count) {
		Producer.nWorkingProducers = count;
	}
	public static synchronized void decrProducerCount() {
		Producer.nWorkingProducers--;
	}
	public static boolean allIdle() {
		return (Producer.nWorkingProducers > 0 ? false : true);
	}
	
	public void setJobCount(int nJob) {
		this.nJob = nJob;
	}
	
	// "nJob = 0" is interpreted as to produce infinite jobs
	public void run() {			
		// Do the work
		if (nJob > 0) {
			for(int i=1 ; i<=nJob ; i++)
				generateAndAddItem();
		}
		else if(nJob == 0){
			while(true)
				generateAndAddItem();
		}
		Producer.decrProducerCount();
		System.out.println("Producer-" + this.getName() + " stopped");
	}
	
	void generateAndAddItem() {
		int item;
		item = nextItem();
		pcBuffer.addItem(item);
		//System.out.println("Producer-" + this.getName() + " added item#" + item + " to queue");
	}
	
	int nextItem() {
		return (int) (Math.random() * 5 + 1);	// Returns a random integer in range [1,6)
	}
}

// Class to implement Consumer 
class Consumer extends Thread {
	ProducerConsumerList pcBuffer = ProducerConsumerList.getInstance();
	int jobCount;
	
	public Consumer(String name) {
		this.setName(name);
		jobCount = 0;
	}
	
	public void run() {
		int item;
		try {
			while(true) {
				// Retrieve an item from the buffer
				//System.out.println("Consumer-" + this.getName() + " attempting to consume item from queue");
//				System.out.println(this.getName() + " on " + pcBuffer.getSize());
				item = pcBuffer.removeItem();
				//System.out.println("Consumer-" + this.getName() + " consumed item#" + item);
				jobCount++;
			}
		}
		catch(InterruptedException eIE) {
			System.out.println("Terminated " + this.getName() + "! ");
		}
		finally {
			displayStat();
		}
	}
	
	public void displayStat() {
		System.out.println("Consumer " + this.getName() + " consumed " + jobCount + " jobs");
	}
	
	public void resetStat() {
		jobCount = 0;
	}
	
	public static boolean workInProgress() {
		return (Producer.allIdle() && ProducerConsumerList.getInstance().getSize() == 0) ? false : true;
	}
}

