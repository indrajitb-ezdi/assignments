import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Stack;

public class QueueUsingStack {
	// Two stacks used to implement queue
	Stack<Integer> s1;
	Stack<Integer> s2;
	
	public QueueUsingStack() {
		s1 = new Stack<Integer>();
		s2 = new Stack<Integer>();
	}
	
	// Function to add element in Queue
	void add(int x) {
		s1.push(x);
	}
	// Function to delete element from Queue
	void delete() {
		if(s1.size() == 0) {
			System.out.println("Empty queue");
		}
		else if(s1.size() == 1) {
			System.out.println("Deleted: " + s1.pop());
		}
		else {
			while(s1.size() > 1) {
				s2.push(s1.pop());
			}
			System.out.println("Deleted: " + s1.pop());
			while(s2.size() > 0) {
				s1.push(s2.pop());
			}
		}
	}
	// Function to display current queue
	void show() {
		System.out.print("Queue: ");
		while(s1.size() > 0) {
			s2.push(s1.pop());
		}
		while(s2.size() > 0) {
			System.out.print(s2.peek() + " ");
			s1.push(s2.pop());
		}
		System.out.println();
	}
	
	public static void main(String[] args) {
		QueueUsingStack myQueue = new QueueUsingStack();
		int ch = 0;
		BufferedReader buf = new BufferedReader(new InputStreamReader(System.in));;
		
		do {
			System.out.print("Enter (1-add, 2-delete, 3-show, 0-exit): ");
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
					myQueue.add(val);
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
				myQueue.delete();
				break;
				
			case 3:
				myQueue.show();
			}
		}while(ch != 0);
	}
}
