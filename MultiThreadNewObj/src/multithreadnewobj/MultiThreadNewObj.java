package multithreadnewobj;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MultiThreadNewObj {

	public static void main(String[] args) {
		// Call appropriate function
		MultiThreadNewObj obj = new MultiThreadNewObj();
		// obj.runProducerConsumer();
		obj.runProducerConsumerLimited();
	}
	
	
	void runProducerConsumerLimited() {
		int nProducers, nConsumers, nJob;
		String choice;
		
		System.out.print("Enter number of Producers: ");
		nProducers = getInputInt();
		System.out.print("Enter number of Consumers: ");
		nConsumers = getInputInt();
				
		do {
			Producer[] producer = new Producer[nProducers];
			Consumer[] consumer = new Consumer[nConsumers];

			System.out.print("Enter number of jobs per Producer (0-infinite): ");
			nJob = getInputInt();
			
			// Starting producer and consumer instances
			Producer.setProducerCount(nProducers);
			for(int i = 0 ; i < nProducers ; i++) {
				String name = "P" + (i+1);
				producer[i] = new Producer(name);
				producer[i].setJobCount(nJob);
				System.out.println("Starting Producer-" + name);
				producer[i].start();
			}
			for(int i=0 ; i < nConsumers ; i++) {
				String name = "C" + (i+1);
				consumer[i] = new Consumer(name);
				System.out.println("Starting Consumer-" + name);
				consumer[i].start();
			}
			
			// Wait until all items have been produced
			for(int i = 0 ; i < nProducers ; i++) {
				try {
					producer[i].join();
				}
				catch(InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			// Wait until all items have been consumed
			while(Consumer.workInProgress());
 			
			for(int i=0 ; i < nConsumers ; i++)
				consumer[i].interrupt();
			for(int i = 0 ; i < nConsumers ; i++)
				while(consumer[i].getState() != Thread.State.TERMINATED);
			
			System.out.print("Do you wish to produce more items? ('y/Y'-yes; 'any other'-no) : ");
			choice = getInputStr();
		}while(choice.equals("y") || choice.equals("Y"));
		
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
