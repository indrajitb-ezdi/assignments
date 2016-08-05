import java.util.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class SortArray {

	public static void main(String[] args) {
		Sort sortObj = new BubbleSort();
		int ch = 0;
		do {
			System.out.println();
			System.out.println("1 - Insert integers (space separated)");
			System.out.println("2 - Remove integer");
			System.out.println("3 - Remove all integers");
			System.out.println("4 - Display array");
			System.out.println("5 - Sort using bubble sort (ascending)");
			System.out.println("6 - Sort using bubble sort (descending)");
			System.out.println("7 - Sort using quick sort (ascending)");
			System.out.println("8 - Sort using quick sort (descending)");
			System.out.println("0 - Exit");
			System.out.print("Enter choice: ");
			ch = getInputInt();
			
			switch(ch) {
			case 1:
				System.out.print("Enter list of integers: ");
				sortObj.insertList(getInputIntList());
				break;
			case 2:
				System.out.print("Enter integer to remove: ");
				if(sortObj.remove(getInputInt()))
					System.out.println("First occurance removed");
				else
					System.out.println("No element removed");
				break;
			case 3:
				sortObj.removeAll();
				System.out.println("All elements removed");
				break;
			case 4:
				sortObj.show();
				break;
			case 5:
				sortObj = new BubbleSort(sortObj);
				sortObj.sort();
				break;
			case 6:
				sortObj = new BubbleSort(sortObj);
				sortObj.sortReverse();
				break;
			case 7:
				sortObj = new QuickSort(sortObj);
				sortObj.sort();
				break;
			case 8:
				sortObj = new QuickSort(sortObj);
				sortObj.sortReverse();
				break;
			default:
			}
		}while(ch != 0);
	}
	
	//Function to read string format input
	static String getInputStr() {
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
	static int getInputInt() {
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
	//Function to read list of space-separated integer input
	static ArrayList<Integer> getInputIntList() {
		String line = getInputStr();
		ArrayList<Integer> numList = new ArrayList<Integer>();
		if(line != null) {
			String[] items = line.trim().split("\\s+");		// delimiting on space characters
			for(int i=0 ; i<items.length ; i++) {
				try {
					numList.add(Integer.parseInt(items[i]));
				}
				catch(NumberFormatException eNF) {
					System.out.println("Skipping non-integer input");
				}
			}
		}
		return numList;
	}
}


// Class to sort integers
abstract class Sort {
	ArrayList<Integer> arr;
	
	void insert(int val) {
		arr.add(val);
	}
	void insertList(ArrayList<Integer> valList) {
		if(valList.size() > 0) {
			for(int i=0 ; i<valList.size() ; i++)
				insert(valList.get(i));
		}
	}
	boolean remove(int val) {
		return arr.remove((Integer) val);
	}
	void removeAll() {
		arr.clear();
	}
	void show() {
		System.out.print("Array: ");
		for(int i=0 ; i<arr.size(); i++)
			System.out.print(arr.get(i) + " ");
		System.out.println();
	}
	void swap(int i, int j) {
		int tmp = arr.get(i);
		arr.set(i, arr.get(j));
		arr.set(j, tmp);
	}
	
	protected abstract void sort();
	void sortReverse() {
		sort();
		for(int i=0 ; i<arr.size()/2; i++) {
			swap(i, arr.size()-1-i);
		}
		System.out.println("Reverse sorted!");
		show();
	}
}

class BubbleSort extends Sort {
	
	public BubbleSort() {
		arr = new ArrayList<Integer>();
	}
	public BubbleSort(Sort obj) {
		this.arr = obj.arr;
	}
	
	// Implements bubble sort
	@Override
	protected void sort() {
		System.out.println("Sorting ...");
		System.out.print("... ");
		show();
		for(int i=arr.size()-1 ; i>0 ; i--) {
			boolean swapped = false;
			for(int j=0 ; j<i ; j++) {
				if(arr.get(j) > arr.get(j+1)) {
					int tmp = arr.get(j);
					arr.set(j, arr.get(j+1));
					arr.set(j+1, tmp);
					swapped = true;
				}
			}
			System.out.print("... ");
			show();
			if(!swapped)
				break;
		}
		System.out.println("Sorted!");
		show();
	}
}

class QuickSort extends Sort {

	public QuickSort() {
		arr = new ArrayList<Integer>();
	}
	public QuickSort(Sort obj) {
		this.arr = obj.arr;
	}
	
	// Implements quick sort
	@Override
	protected void sort() {
		System.out.println("Sorting ...");
		System.out.print("... ");
		show();
		qSort(0, arr.size()-1);
		System.out.println("Sorted!");
		show();
	}
	void qSort(int left, int right) {
		if(left < right) {
			int mid = partition(left, right);
			System.out.print("... ");
			show();
			qSort(left, mid-1);
			qSort(mid+1, right);
		}
	}
	int partition(int start, int end) {
		int pivot = arr.get(end);
		int pos = start;
		for(int i=start ; i<=end-1 ; i++) {
			if(arr.get(i) < pivot) {
				swap(pos, i);
				pos++;
			}
		}
		swap(pos, end);
		return pos;
	}
}
