package threadFixedTotalJob;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class ThreadFixedTotalJob {

	public static void main(String[] args) {
		ThreadFixedTotalJob obj = new ThreadFixedTotalJob();
		obj.runPC();
	}
	
	void runPC() {
		Producer[] producer;
		Consumer[] consumer;
		int nProducers, nConsumers, nJob;
		String choice;
		
		System.out.print("Enter number of Producers: ");
		nProducers = getInputInt();
		System.out.print("Enter number of Consumers: ");
		nConsumers = getInputInt();
		
		producer = new Producer[nProducers];
		consumer = new Consumer[nConsumers];
		// Creating Producers
		for(int i=0 ; i < nProducers ; i++) {
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
		
		do {
			System.out.print("Enter number of jobs to produce: ");
			nJob = getInputInt();
			
			PCBuffer.getInstance().setJobCount(nJob);
			
			System.out.print("Do you wish to produce more items? ('y/Y'-yes; 'any other'-no) : ");
			choice = getInputStr();
		}while(choice.equals("y") || choice.equals("Y"));
		
		for(int i = 0 ; i < nProducers ; i++)
			producer[i].interrupt();
		for(int i = 0 ; i < nConsumers ; i++)
			consumer[i].interrupt();
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
