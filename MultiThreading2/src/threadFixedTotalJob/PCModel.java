package threadFixedTotalJob;

import java.util.concurrent.LinkedBlockingQueue;

// Class to implement buffer for producer-consumer problem
class PCBuffer {
	private static PCBuffer pc = new PCBuffer();
	private LinkedBlockingQueue<Integer> buffer;
	private int nJobs;
	
	private PCBuffer() {
		buffer = new LinkedBlockingQueue<Integer>();
		nJobs = -1;
	}
	public static PCBuffer getInstance() {
		return PCBuffer.pc;
	}
	
	public void setJobCount(int count) {
		pc.nJobs = count;
	}
	
	public boolean jobsRemaining() {
		return (pc.nJobs > 0 ? true : false);
	}
	
	public void addItem(int item) throws JobsCompletedException {
		// Synchronizing on object pc for atomic update of nJobs counter
		if(pc.nJobs != -1){
			synchronized (pc) {
				try {
					if(pc.nJobs > 0) {
						pc.buffer.put((Integer) item);	// Blocks calling thread if buffer is full
						pc.nJobs--;
					}
					else if(pc.nJobs == 0) {
						pc.nJobs = -1;
						throw new JobsCompletedException(); 
					}
				}
				catch(InterruptedException eIE) {
					System.out.println("Buffer loading interrupted");
				}
			}
		}
	}
	public int removeItem() throws InterruptedException {
		int item = 0;
		item = pc.buffer.take();		// Blocks calling thread if buffer is empty
		return item;
	}
}

//Class to implement Producer 
class Producer extends Thread {
	PCBuffer pcBuffer = PCBuffer.getInstance();
	
	public Producer(String name) {
		this.setName(name);
	}
		
	public void run() {		
		while(!Thread.currentThread().isInterrupted()){
			if(pcBuffer.jobsRemaining()) {
				try {
					generateAndAddItem();
				}
				catch (JobsCompletedException eJobs) {
					System.out.println("No more jobs to produce");
				}
			}
		}
	}
	
	void generateAndAddItem() throws JobsCompletedException {
		int item;
		item = nextItem();
		pcBuffer.addItem(item);
		System.out.println("Producer-" + this.getName() + " added item#" + item + " to queue");
	}
	
	int nextItem() {
		return (int) (Math.random() * 5 + 1);	// Returns a random integer in range [1,6)
	}
}

//Class to implement Consumer 
class Consumer extends Thread {
	PCBuffer pcBuffer = PCBuffer.getInstance();
	
	public Consumer(String name) {
		this.setName(name);
	}
	
	public void run() {
		int item;
		try {
			while(!Thread.currentThread().isInterrupted()) {
				// Retrieve an item from the buffer
				item = pcBuffer.removeItem();
				System.out.println("Consumer-" + this.getName() + " consumed item#" + item);
			}
		}
		catch(InterruptedException eIE) {
			System.out.println("Terminated " + this.getName() + "!");
		}
		finally {
			System.out.println("Consumer-" + this.getName() + " stopped");
		}
	}
}

// Custom exception class
class JobsCompletedException extends Exception {
	public JobsCompletedException() {
		super();
	}
}
