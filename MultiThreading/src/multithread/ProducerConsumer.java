package multithread;

import java.util.concurrent.ArrayBlockingQueue;

// Singleton class to have a single buffer resource  
public class ProducerConsumer {
	private static ProducerConsumer pc = new ProducerConsumer();
	private ArrayBlockingQueue<Integer> buffer = null;
	
	private ProducerConsumer() {}
	
	public static ProducerConsumer getInstance() {
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

// Class to implement Producer 
class Producer extends Thread {
	ProducerConsumer pcBuffer = ProducerConsumer.getInstance();
	
	public Producer(String name) {
		this.setName(name);
	}
	
	public void run() {
		int item;
		try {
			while(true) {
				item = nextItem();
				//System.out.println("Producer-" + this.getName() + " produced item#" + item);
				// Store the item produced in the buffer
				pcBuffer.addItem(item);
				System.out.println("Producer-" + this.getName() + " added item#" + item + " to queue");
				// Thread.sleep(item * 1000);	// Wait for 'item' seconds
				Thread.sleep(item * 0);	// Wait for 'item' seconds
			}
		}
		catch(InterruptedException eIE) {
			System.out.println("Producer-" + this.getName() + " interrupted");
		}
	}
	
	int nextItem() {
		return (int) (Math.random() * 5 + 1);	// Returns a random integer in range [1,6)
	}
}

// Class to implement Consumer 
class Consumer extends Thread {
	ProducerConsumer pcBuffer = ProducerConsumer.getInstance();
	
	public Consumer(String name) {
		this.setName(name);
	}
	
	public void run() {
		int item;
		try {
			while(true) {
				// Retrieve an item from the buffer
				//System.out.println("Consumer-" + this.getName() + " attempting to consume item from queue");
				item = pcBuffer.removeItem();
				System.out.println("Consumer-" + this.getName() + " consumed item#" + item);
				// Thread.sleep(item * 1000);	// Wait for 'item' seconds
				Thread.sleep(item * 0);	// Wait for 'item' seconds
			}
		}
		catch(InterruptedException eIE) {
			System.out.println("Consumer-" + this.getName() + " interrupted");
		}
	}
}

