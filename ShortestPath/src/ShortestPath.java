import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.print.attribute.standard.ReferenceUriSchemesSupported;

import java.io.IOException;

public class ShortestPath {
	public static void main(String[] args) {
		int[][] graph;
		int nodes, edges;
		ArrayList<Integer> edge;
		
		System.out.println("Program to find shortest path in graph from a given start node using Dijkstra's algorithm");
		System.out.print("Enter number of nodes: ");
		nodes = getInputInt();
		System.out.print("Enter number of edges: ");
		edges = getInputInt();
		
		graph = new int[nodes][nodes];
		System.out.println("Enter tuple <startNode, endNode, weight> for each edge (space separated integers, first node of graph is '0')");
		for(int i=1 ; i<=edges ; i++) {
			System.out.print("Edge " + i + ": ");
			edge = getInputIntList();
			graph[edge.get(0)][edge.get(1)] = edge.get(2);	// 0-startEdge, 1-endEgde, 2-weight read from input
		}
		
		System.out.print("Enter starting node to find shortest path tree :");
		int start = getInputInt();
		
		// Calculating shortest path on the graph
		Dijkstra objDjks = new Dijkstra(graph, nodes);
		objDjks.calcShortestPath(start);
		objDjks.showShortestPath();
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
			String line = getInputStr().trim();
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
			String line = getInputStr().trim();
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

// Class to implement Dijkstra's shortest path algorithm
class Dijkstra {
	int[][] graph;
	int nodes;
	int[] distance, parent;
	boolean[] nodeVisited;
	
	public Dijkstra(int[][] g, int n) {
		graph = g;
		nodes = n;
		distance = new int[nodes];
		parent = new int[nodes];
		nodeVisited = new boolean[nodes];
	}
	// Function to initialize distance vector
	void initialize() {
		for(int i=0 ; i<nodes ; i++) {
			distance[i] = Integer.MAX_VALUE;	// Marking node distances as infinity
			nodeVisited[i] = false;				// Marking nodes to be added to shortest path tree later
			parent[i] = -1;						// Marking dummy shortest tree parent nodes 
		}
	}
	// Function to find neighbour node with minimum weight from shortest path tree
	int findNextNode() {
		int minIndex = -1;
		int minDist = Integer.MAX_VALUE;
		for(int i=0 ; i<nodes ; i++) {
			if(!nodeVisited[i] && distance[i]<minDist) {
				minDist = distance[i];
				minIndex = i; 
			}
		}
		return minIndex;
	}
	// Function to calculate shortest path using Dijkstra's algorithm
	void calcShortestPath(int startNode) {
		int u;
		initialize();
		distance[startNode] = 0;
		// Finding shortest paths
		for(int i=0 ; i < nodes-1 ; i++) {
			u = findNextNode();
			nodeVisited[u] = true;		// Marking the node as added to shortest path tree
			
			// Updating unvisited, adjacent nodes with new distances from so far formed shortest path tree
			for(int v=0 ; v<nodes ; v++) {
				if(!nodeVisited[v] && graph[u][v] != 0 && 
						distance[u] != Integer.MAX_VALUE && (distance[u] + graph[u][v]) < distance[v]) {
					distance[v] = distance[u] + graph[u][v];
					parent[v] = u;
				}
			}
		}
	}
	// Function to display shortest path
	void showShortestPath() {
		System.out.println("Shortest path tree:");
		recursivePathPrint(-1, 0); 		// Starting to print from start node(second arg=0) having parent=-1(fist arg=-1)
	}
	
	void recursivePathPrint(int p, int level) {
		for(int u=0 ; u<nodes ; u++) {
			if(parent[u] == p) {
				if(p != -1) {
					for(int space = 1 ; space <= level*4 ; space++)
						System.out.print(" ");
					System.out.println(parent[u] + " --> " + u + "(" + distance[u] + ")");
				}
				recursivePathPrint(u, level+1);
			}
		}
	}
}