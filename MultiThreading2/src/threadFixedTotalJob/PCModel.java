package threadFixedTotalJob;

import java.util.concurrent.LinkedBlockingQueue;

// Class to implement buffer for producer-consumer problem
class PCBuffer {
	private static PCBuffer pc = new PCBuffer();
	private LinkedBlockingQueue<Integer> buffer;
	private volatile int nJobs;
	
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
	
	public int getJobCount() {
		return nJobs;
	}
	
	public boolean jobsRemaining() {
		return (pc.nJobs > 0 ? true : false);
	}
	
	public void addItem(int item) throws JobsCompletedException, InterruptedException {
		// Synchronizing on object pc for atomic update of nJobs counter
		if(pc.nJobs != -1){
			synchronized (pc) {
				if(pc.nJobs > 0) {
					pc.buffer.put((Integer) item);	// Blocks calling thread if buffer is full
					pc.nJobs--;
				}
				else if(pc.nJobs == 0) {
					pc.nJobs = -1;
					throw new JobsCompletedException(); 
				}
			}
		}
	}
	public int removeItem() throws InterruptedException {
		int item = 0;
		item = pc.buffer.take();		// Blocks calling thread if buffer is empty
//		System.out.println("Taken from buffer: " + item);
		return item;
	}
}

//Class to implement Producer 
class Producer extends Thread {
	PCBuffer pcBuffer = PCBuffer.getInstance();
	Logger logger;
	int jobCount;
	boolean working;
	
	public Producer(String name, Logger logObj) {
		this.setName(name);
		logger = logObj;
		jobCount = 0;
		working = false;
	}
		
	@Override
	public void run() {		
		while(!Thread.currentThread().isInterrupted()){
			if(pcBuffer.jobsRemaining()) {
				working = true;
				try {
					generateAndAddItem();
					jobCount++;
				}
				catch (JobsCompletedException eJobs) {
//					System.out.println("No more jobs to produce");
					logger.write("No more jobs to produce");
				}
				catch(InterruptedException eIE) {
//					System.out.println("Buffer loading interrupted");
					logger.write("Buffer loading interrupted");
				}
			}
			else
				working = false;
		}
//		System.out.println("Producer-" + this.getName() + " stopped");
		logger.write("Producer-" + this.getName() + " stopped");
	}
	
	void generateAndAddItem() throws JobsCompletedException, InterruptedException {
		int item;
		item = nextItem();
//		System.out.println(this.getName() + " generated " + item);
		pcBuffer.addItem(item);
//		System.out.println("Producer-" + this.getName() + " added item#" + item + " to queue");
		logger.write("Producer-" + this.getName() + " added item#" + item + " to queue");
	}
	
	int nextItem() {
		int val = (int) (Math.random() * 5 + 1);
//		System.out.println("Randomly generated: " + val);
		return val;	// Returns a random integer in range [1,6)
	}
	
	public void displayStat() {
		logger.write("Producer " + this.getName() + " produced " + jobCount + " jobs");
	}
	
	public void resetStat() {
		jobCount = 0;
	}
	public boolean isWorking() {
		return working;
	}
}

//Class to implement Consumer 
class Consumer extends Thread {
	PCBuffer pcBuffer = PCBuffer.getInstance();
	Logger logger;
	int jobCount;
	
	public Consumer(String name, Logger logObj) {
		this.setName(name);
		logger = logObj;
		jobCount = 0;
	}
	
	@Override
	public void run() {
		int item;
		try {
			while(!Thread.currentThread().isInterrupted()) {
				// Retrieve an item from the buffer
				item = pcBuffer.removeItem();
				jobCount++;
//				System.out.println("Consumer-" + this.getName() + " consumed item#" + item);
				logger.write("Consumer-" + this.getName() + " consumed item#" + item);
			}
//			System.out.println("Consumer-" + this.getName() + " stopped");
			logger.write("Consumer-" + this.getName() + " stopped");
		}
		catch(InterruptedException eIE) {
//			System.out.println("Terminated " + this.getName() + "!");
			logger.write("Terminated " + this.getName() + "!");
		}
	}
	
	public void displayStat() {
		logger.write("Consumer " + this.getName() + " consumed " + jobCount + " jobs");
	}
	
	public void resetStat() {
		jobCount = 0;
	}
}

// Custom exception class
class JobsCompletedException extends Exception {
	public JobsCompletedException() {
		super();
	}
}
