package multithread3;

import java.util.LinkedList;
import java.util.Queue;


class PCBuffer {
	private static PCBuffer pc = new PCBuffer();
	
	public static PCBuffer getInstance() {
		return PCBuffer.pc;
	}
	
	private Queue<Integer> buffer;
	private int capacity;
	
	private PCBuffer() {
		buffer = new LinkedList<Integer>();
		capacity = 0;
	}
	
	public void setCapacity(int limit) {
		capacity = limit;
		buffer.clear();
	}
	public int getCapacity() {
		return capacity;
	}
	public int getSize() {
		return buffer.size();
	}
	public void addItem(int data) throws InterruptedException {
		
		synchronized(buffer) {
			if(capacity > 0) {
				while(buffer.size() >= capacity) {
					buffer.wait();
				}
			}
			buffer.offer(data);
			buffer.notifyAll();
		}
	}
	public int removeItem() throws InterruptedException {
		int val = Integer.MIN_VALUE;
		synchronized(buffer) {
			while(buffer.size() == 0) {
				buffer.wait();
			}
			val = buffer.poll();
			if(capacity > 0) {
				buffer.notifyAll();
			}
		}
		return val;
	}
}


//Class to implement Producer 
class Producer extends Thread {
	PCBuffer pcBuffer = PCBuffer.getInstance();
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
		try {
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
		}
		catch (InterruptedException eIO) {
			System.out.println("Producer " + this.getName() + " interrupted in attempt to add item");
		}
		finally {
			System.out.println("Producer-" + this.getName() + " stopped");
		}
	}
	
	void generateAndAddItem() throws InterruptedException {
		int item;
		item = nextItem();
		pcBuffer.addItem(item);
		//System.out.println("Producer-" + this.getName() + " added item#" + item + " to queue");
	}
	
	int nextItem() {
		return (int) (Math.random() * 5 + 1);	// Returns a random integer in range [1,6)
	}
}

//Class to implement Consumer 
class Consumer extends Thread {
	PCBuffer pcBuffer = PCBuffer.getInstance();
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
		return (Producer.allIdle() && PCBuffer.getInstance().getSize() == 0) ? false : true;
	}
}
