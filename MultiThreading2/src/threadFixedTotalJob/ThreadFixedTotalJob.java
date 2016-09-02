package threadFixedTotalJob;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class ThreadFixedTotalJob {

	public static void main(String[] args) {
		Logger logger = Logger.getInstance("ProCon");
		ThreadFixedTotalJob obj = new ThreadFixedTotalJob();
		obj.runPC(logger);
		logger.close();
	}
	
	void runPC(Logger logger) {
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
			producer[i] = new Producer(name, logger);
			System.out.println("Starting Producer-" + name);
			logger.write("Starting Producer-" + name);
			producer[i].start();
		}
		for(int i=0 ; i < nConsumers ; i++) {
			String name = "C" + (i+1);
			consumer[i] = new Consumer(name, logger);
			System.out.println("Starting Consumer-" + name);
			logger.write("Starting Consumer-" + name);
			consumer[i].start();
		}
		
		do {
			logger.reset();
			System.out.print("Enter number of jobs to produce: ");
			nJob = getInputInt();
			logger.write("Number of jobs: " + nJob);
			
			PCBuffer.getInstance().setJobCount(nJob);
			
			while(PCBuffer.getInstance().jobsRemaining()) {
				System.out.println(PCBuffer.getInstance().getJobCount());
			}

			for(int i = 0 ; i < nProducers ; i++) {
				while(producer[i].isWorking());
				producer[i].displayStat();
				producer[i].resetStat();
			}
			System.out.println("All producers done");
			for(int i = 0 ; i < nConsumers ; i++) {
				while(consumer[i].getState() == Thread.State.RUNNABLE);
				consumer[i].displayStat();
				consumer[i].resetStat();
			}
			System.out.println("All consumers done");
			
			System.out.print("Do you wish to produce more items? ('y/Y'-yes; 'any other'-no) : ");
			choice = getInputStr();
		}while(choice.equals("y") || choice.equals("Y"));
		logger.reset();
		
		for(int i = 0 ; i < nProducers ; i++)
			producer[i].interrupt();
		for(int i = 0 ; i < nConsumers ; i++)
			consumer[i].interrupt();
		
		
		// Waiting for spawned threads to complete
		boolean wait = false;
		do {
			wait = false;
			for(int i = 0 ; i < nProducers ; i++) {
				if(producer[i].getState() != Thread.State.TERMINATED) {
					wait = true;
					break;
				}
			}
			if(!wait) {
				for(int i = 0 ; i < nConsumers ; i++) {
					if(consumer[i].getState() != Thread.State.TERMINATED) {
						wait = true;
						break;
					}
				}
			}
		}while(wait);
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
