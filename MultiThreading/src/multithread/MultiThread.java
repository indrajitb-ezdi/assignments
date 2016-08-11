package multithread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MultiThread {

	public static void main(String[] args) {
		// Call appropriate function
		MultiThread obj = new MultiThread();
		// obj.runProducerConsumer();
		obj.runProducerConsumerLimited();
	}
	
	void testThreading() {
		// Testing thread programming
		Thread t1 = new TestThread();
		t1.start();

		Thread t2 = new TestThread("t2");
		t2.start();
	}
	
	void runProducerConsumer() {
		int bufSize, nProcuders, nConsumers;
		Thread[] producer, consumer;
		ProducerConsumerArray pcBuf = ProducerConsumerArray.getInstance();
		
		System.out.print("Enter buffer size: ");
		bufSize = getInputInt();
		System.out.print("Enter number of Producers: ");
		nProcuders = getInputInt();
		System.out.print("Enter number of Consumers: ");
		nConsumers = getInputInt();
		
		pcBuf.createBuffer(bufSize);
		producer = new Thread[nProcuders];
		consumer = new Thread[nConsumers];
		// Creating Producers
		for(int i=0 ; i < nProcuders ; i++) {
			String name = "P" + (i+1);
			producer[i] = new Producer(name);
			System.out.println("Starting Producer-" + name);
			producer[i].start();
		}
		for(int i=0 ; i < nConsumers ; i++) {
			String name = "C" + (i+1);
			consumer[i] = new Consumer(name);
			System.out.println("Starting Consumer-" + name);
			consumer[i].start();
		}
	}
	
	void runProducerConsumerLimited() {
		Thread[] producer, consumer;
		int nProcuders, nConsumers, nJob;
		
		System.out.print("Enter number of Producers: ");
		nProcuders = getInputInt();
		System.out.print("Enter number of jobs per Producer: ");
		nJob = getInputInt();
		System.out.print("Enter number of Consumers: ");
		nConsumers = getInputInt();
		
		producer = new Thread[nProcuders];
		consumer = new Thread[nConsumers];
		// Creating Producers
		for(int i=0 ; i < nProcuders ; i++) {
			String name = "P" + (i+1);
			producer[i] = new Producer(name, nJob);
			System.out.println("Starting Producer-" + name);
			producer[i].start();
		}
		for(int i=0 ; i < nConsumers ; i++) {
			String name = "C" + (i+1);
			consumer[i] = new Consumer(name);
			System.out.println("Starting Consumer-" + name);
			consumer[i].start();
		}
		try {
			Thread.sleep(5000);
		}
		catch(InterruptedException eIE) {}
		
		while(true) {
			boolean stillProducing = false;
			for(int i=0 ; i<nProcuders ; i++)
				stillProducing = stillProducing || (producer[i].getState() != Thread.State.TERMINATED);
			if(!stillProducing) {
				for(int i=0 ; i<nConsumers ; i++)
					consumer[i].interrupt();
				break;
			}
		}
	}
	
	//Function to read string format input
	String getInputStr() {
		BufferedReader buf = new BufferedReader(new InputStreamReader(System.in));
		String line;
		try {
			line = buf.readLine();
		}
		catch(IOException eIO) {
			System.out.println("Error reading input");
			line = null;
		}
		return line;
	}
	//Function to read single integer input
	int getInputInt() {
		String line = getInputStr();
		if(line == null)
			return Integer.MIN_VALUE;
		else {
			int num;
			try {
				num = Integer.parseInt(line);
			}
			catch(NumberFormatException eNF) {
				System.out.println("Invalid integer");
				num = Integer.MIN_VALUE;
			}
			return num;
		}
	}
}
