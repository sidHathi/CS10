import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

/**
 * DFS and BFS traversals of Graph.  backTrack Map keeps track of prior vertex when vertex is discovered.
 * @author Tim Pierson, Dartmouth CS 10, Spring 2019
 *
 * @param <V> Type for vertices
 * @param <E> Type for edge labels
 */
public class GraphTraversal<V,E> {

	public Map<V,V> backTrack; //keep track of prior vertex when vertex is discovered -> Map<current vertex, prior vertex>
	
	/**
	 * Constructor.  Initialize backTrack to new HashMap.
	 */
	public GraphTraversal() {
		backTrack = new HashMap<V,V>();
	}
	
	/**
	 * Depth First Search
	 * @param G -- graph to search
	 * @param start -- starting vertex
	 */
	public void DFS(AdjacencyMapGraph<V,E> G, V start) {
		System.out.println("\nDepth First Search from " + start);
		backTrack = new HashMap<V,V>(); //initialize backTrack
		backTrack.put(start, null); //load start node with null parent
		Set<V> visited = new HashSet<V>(); //Set to track which vertices have already been visited
		Stack<V> stack = new Stack<V>(); //stack to implement DFS
		
		stack.push(start); //push start vertex
		while (!stack.isEmpty()) { //loop until no more vertices
			V u = stack.pop(); //get most recent vertex
			if (!visited.contains(u)) { //if not already visited
				visited.add(u); //add to visited Set
				for (V v : G.outNeighbors(u)) { //loop over out neighbors
					if (!visited.contains(v)) { //if neighbor not visited, then neighbor is discovered from this vertex
						stack.push(v); //push non-visted neighbor onto stack
						backTrack.put(v, u); //save that this vertex was discovered from prior vertex
					}
				}
			}
		}
	}
	
	/** Breadth First Search
	 * 
	 * @param G -- graph to search
	 * @param start -- starting vertex
	 */
	public void BFS(AdjacencyMapGraph<V,E> G, V start) {
		System.out.println("\nBreadth First Search from " + start);
		backTrack = new HashMap<V,V>(); //initialize backTrack
		backTrack.put(start, null); //load start vertex with null parent
		Set<V> visited = new HashSet<V>(); //Set to track which vertices have already been visited
		Queue<V> queue = new LinkedList<V>(); //queue to implement BFS
		
		queue.add(start); //enqueue start vertex
		visited.add(start); //add start to visited Set
		while (!queue.isEmpty()) { //loop until no more vertices
			V u = queue.remove(); //dequeue
			for (V v : G.outNeighbors(u)) { //loop over out neighbors
				if (!visited.contains(v)) { //if neighbor not visited, then neighbor is discovered from this vertex
					visited.add(v); //add neighbor to visited Set
					queue.add(v); //enqueue neighbor
					backTrack.put(v, u); //save that this vertex was discovered from prior vertex
				}
			}
		}
	}
	
	/**
	 * Find a path from start vertex to end vertex.  Start at end vertex and work backward using 
	 * backTrack to start vertex.  Assumes either DFS or BFS has run and backTrack is filled.
	 * @param start -- starting vertex
	 * @param end -- ending vertex
	 * @return arraylist of nodes on path from start to end, empty if no path
	 */
	public ArrayList<V> findPath(V start, V end) {
		//check that DFS or BFS have already been run from start
		if (backTrack.isEmpty() || !backTrack.containsKey(start) || 
				(backTrack.containsKey(start) && backTrack.get(start) != null)) {
			System.out.println("Run DFS or BFS on " + start + " before trying to find a path");
			return new ArrayList<V>();
		}
		System.out.println("Path from " + start + " to " + end);
		//make sure end vertex in backTrack
		if (!backTrack.containsKey(end)) {
			System.out.println("\tNo path found");
			return new ArrayList<V>();
		}
		//start from end vertex and work backward to start vertex
		ArrayList<V> path = new ArrayList<V>(); //this will hold the path from start to end vertex
		V current = end; //start at end vertex
		//loop from end vertex back to start vertex
		while (current != null) {
			path.add(0,current); //add this vertex to front of arraylist path
			current = backTrack.get(current); //get vertex that discovered this vertex
		}
		System.out.println(path);
		return path;
	}
	
	public static void main(String[] args) {
		//set up graph from class introducing Graphs
		GraphTraversal<String,String> GT = new GraphTraversal<String,String>();
		AdjacencyMapGraph<String,String> g = new AdjacencyMapGraph<String,String>();
		g.insertVertex("Alice");
		g.insertVertex("Bob");
		g.insertVertex("Charlie");
		g.insertVertex("Dartmouth");
		g.insertVertex("Elvis");
		g.insertDirected("Alice", "Dartmouth", "follower");
		g.insertDirected("Bob", "Dartmouth", "follower");
		g.insertDirected("Charlie", "Dartmouth", "follower");
		g.insertDirected("Elvis", "Dartmouth", "follower");
		g.insertUndirected("Alice", "Bob", "friend"); // symmetric, undirected edge
		g.insertDirected("Alice", "Elvis", "friend"); // not symmetric, directed edge!
		g.insertDirected("Charlie", "Elvis", "follower");
		
		//run DFS from Alice
		GT.DFS(g,"Alice");
		//find path from start to end
		GT.findPath("Bob", "Dartmouth"); //DFS wasn't run from Bob, should reject this
		GT.findPath("Alice","Dartmouth");
		GT.findPath("Alice","Charlie");
		GT.findPath("Alice","Alice");
		
		//run BFS
		GT.BFS(g,"Alice");
		
		//find path from start to end
		GT.findPath("Alice","Dartmouth");
		GT.findPath("Alice","Charlie");
		
		//set up graph from Graph Traversal class
		AdjacencyMapGraph<String,String> g2 = new AdjacencyMapGraph<String,String>();
		g2.insertVertex("A"); g2.insertVertex("B"); g2.insertVertex("C"); g2.insertVertex("D");
		g2.insertVertex("E"); g2.insertVertex("F"); g2.insertVertex("G"); g2.insertVertex("H"); g2.insertVertex("I");
		g2.insertUndirected("A", "B", "");
		g2.insertUndirected("B", "F", "");
		g2.insertUndirected("F", "H", "");
		g2.insertUndirected("A", "C", "");
		g2.insertUndirected("A", "D", "");
		g2.insertUndirected("D", "G", "");
		g2.insertUndirected("G", "I", "");
		g2.insertUndirected("A", "E", "");
		g2.insertDirected("I", "H", ""); //directed edge not from class
		
		//run DFS from A and find path to B
		GT.DFS(g2,"A");
		GT.findPath("A", "B");
		
		//run BFS from A and find path to B
		GT.BFS(g2,"A");
		GT.findPath("A", "B");
		
	}

}
