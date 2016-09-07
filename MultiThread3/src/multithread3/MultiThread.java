package multithread3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MultiThread {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

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
