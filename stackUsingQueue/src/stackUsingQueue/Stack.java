package stackUsingQueue;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.*;

public class Stack {
	// Two queues to be used to implement stack
	Queue q1;
	Queue q2;
	
	public Stack() {
		q1 = new Queue();
		q2 = new Queue();
	}
	
	void push(int x) {
		q1.add(x);
		System.out.println("Pushed: " + x);
	}
	void pop() {
		if(q1.getSize() == 0)
			System.out.println("Empty stack");
		else if(q1.getSize() == 1) {
			System.out.println("Popped: " + q1.delete());
		}
		else {
			while(q1.getSize() > 1) {
				q2.add(q1.delete());
			}
			System.out.println("Popped: " + q1.delete());
			while(q2.getSize() > 0) {
				q1.add(q2.delete());
			}
		}
	}
	void show() {
		System.out.print("Stack: ");
		q1.show();
	}
	
	public static void main(String[] args) {		
		Stack myStack = new Stack();
		int ch = 0;
		BufferedReader buf = new BufferedReader(new InputStreamReader(System.in));;
		
		do {
			System.out.print("Enter (1-push, 2-pop, 3-show, 0-exit): ");
			try {
				ch = Integer.parseInt(buf.readLine());
			}
			catch(NumberFormatException eNF) {
				System.out.println("Invalid number");
				System.exit(0);
			}
			catch(IOException eIO) {
				System.out.println("Invalid number");
				System.exit(0);
			}
			switch(ch) {
				case 1:
					try {
						int val = Integer.parseInt(buf.readLine());
						myStack.push(val);
					}
					catch(NumberFormatException eNF) {
						System.out.println("Invalid number");
						System.exit(0);
					}
					catch(IOException eIO) {
						System.out.println("Invalid number");
						System.exit(0);
					}
					break;
					
				case 2:
					myStack.pop();
					break;
					
				case 3:
					myStack.show();	
			}
		}while(ch != 0);
	}
}

class Queue {
	ArrayList q;

	public Queue() {
		q = new ArrayList<Integer>();
	}
	void add(int x) {
		q.add(x);
	}
	int delete() {
		int val = Integer.MIN_VALUE;
		if(q.size() > 0) {
			val = (int) q.remove(0);
		}
		return val;
	}
	void show() {
		for(int i=0 ; i<q.size(); i++) {
			System.out.print(q.get(i) + " ");
		}
		System.out.println();
	}
	int getSize() {
		return q.size();
	}
}
