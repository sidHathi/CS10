import java.util.*;

/**
 * Beginnings of a library for graph analysis code
 * 
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2017 (with some inspiration from previous terms)
 * 
 */
public class GraphLib {
	/**
	 * Orders vertices in decreasing order by their in-degree
	 * @param g		graph
	 * @return		list of vertices sorted by in-degree, decreasing (i.e., largest at index 0)
	 */
	public static <V,E> List<V> verticesByInDegree(Graph<V,E> g) {
		List<V> vs = new ArrayList<V>();
		for (V v : g.vertices()) vs.add(v);
		vs.sort((v1, v2) -> g.inDegree(v2) - g.inDegree(v1));
		return vs;
	}

	/**
	 * Takes a random walk from a vertex, to a random one if its out-neighbors, to a random one of its out-neighbors
	 * Keeps going as along as a random number is less than "continueProb"
	 * Stops earlier if no step can be taken (i.e., reach a vertex with no out-edge)
	 * @param g			graph to walk on
	 * @param start		initial vertex (assumed to be in graph)
	 * @param keepOn		probability of continuing each time -- should be between 0 and 1 (non-inclusive)
	 * @return		a list of vertices starting with start, each with an edge to the sequentially next in the list
	 * 			    null if start isn't in graph
	 */
	public static <V,E> List<V> randomWalk(Graph<V,E> g, V start, double keepOn) {
		if (!g.hasVertex(start) || keepOn <= 0 || keepOn >= 1) return null;
		List<V> path = new ArrayList<V>();
		path.add(start);
		V curr = start;
		while (Math.random()<keepOn) {
			if (g.outDegree(curr) == 0) return path;
			// Pick a neighbor index
			int nbr = (int)(g.outDegree(curr) * Math.random());
			// Iterate through the out-neighbors the given number of times
			Iterator<V> iter = g.outNeighbors(curr).iterator();
			V next = iter.next();
			while (nbr > 0) {
				next = iter.next();
				nbr--;
			}
			// Got to the right neighbor; continue from there
			path.add(next);
			curr = next;
		}

		return path;
	}
	
	/**
	 * Takes a number of random walks from random vertices, keeping track of how many times it goes to each vertex
	 * Doesn't actually keep the walks themselves
	 * @param g			graph to walk on
	 * @param keepOn		probability of continuing each time -- should be between 0 and 1 (non-inclusive)
	 * @param numWalks	how many times to do that
	 * @return			vertex-hitting frequencies
	 */
	public static <V,E> Map<V,Integer> randomWalks(Graph<V,E> g, double keepOn, int numWalks) {
		if (keepOn <= 0 || keepOn >= 1) return null;
		
		// Initialize all frequencies to 0
		Map<V,Integer> freqs = new HashMap<V,Integer>();
		for (V v : g.vertices()) freqs.put(v, 0);
		
		for (int i=0; i<numWalks; i++) {
			// Pick a start index
			int start = (int)(g.numVertices()*Math.random());
			// Iterate through vertices till get there
			Iterator<V> iter = g.vertices().iterator();
			V curr = iter.next();
			while (start > 0) {
				curr = iter.next();
				start--;
			}
			while (Math.random()<keepOn && g.outDegree(curr)>0) {
				// Pick a neighbor index
				int nbr = (int)(g.outDegree(curr) * Math.random());
				// Iterate through the out-neighbors the given number of times
				iter = g.outNeighbors(curr).iterator();
				V next = iter.next();
				while (nbr > 0) {
					next = iter.next();
					nbr--;
				}
				// Keep frequency count
				freqs.put(next, 1+freqs.get(next));
				curr = next;
			}			
		}

		return freqs;
	}


	/**
	 * Orders vertices in decreasing order by their frequency in the map
	 * @param g		graph
	 * @return		list of vertices sorted by frequency, decreasing (i.e., largest at index 0)
	 */
	public static <V,E> List<V> verticesByFrequency(Graph<V,E> g, Map<V,Integer> freqs) {
		List<V> vs = new ArrayList<V>();
		for (V v : g.vertices()) vs.add(v);
		vs.sort((v1, v2) -> freqs.get(v2) - freqs.get(v1));
		return vs;
	}
	
	// SA-7
	
	/**
	 * Identifies potential neighbors w for u, with w!=u, such that there's an edge u->v and an edge v->w
	 * @param g		graph
	 * @param u		vertex of interest
	 * @return		out-neighbors of out-neighbors of v (but not w itself)
	 */
	public static <V,E> Set<V> suggestions(Graph<V,E> g, V u) {
		// TO-DO: Your code here

		Set<V> suggestionSet = new HashSet<V>();

		// Suggest outNeighbors of outNeighbors not equal to u
		for (V v: g.outNeighbors(u)){
			for (V w: g.outNeighbors(v)){
				if (!suggestionSet.contains(w) && !w.equals(u)) {
					suggestionSet.add(w);
				}
			}
		}

		return suggestionSet;
	}
	
	/**
	 * Returns a flipped version of the graph; i.e., every edge u->v in g is v->u in the returned graph, with the same edge label
	 * @param g		graph
	 * @return		flipped version
	 */
	public static <V,E> Graph<V,E> flip(Graph<V,E> g) {
		// TO-DO: Your code here

		Iterable<V> vertices = g.vertices();
		// Set that stores all the vertices that have been flipped
		Set<V> visitedVertices = new HashSet<V>();
		// loops over vertices in the graph twice
		for (V vertex: vertices){
			for (V vertex2: vertices){
				// Checks every vertex against every other vertex to find all outgoing and incoming edges
				// If both vertices aren't already in the visitedVertices set, the edge hasn't been flipped yet
				if (!vertex2.equals(vertex) && !(visitedVertices.contains(vertex2) && visitedVertices.contains(vertex))){
					// Flips edges from vertex to vertex2 to edges from vertex2 to vertex
					if(g.hasEdge(vertex, vertex2) && g.hasEdge(vertex2, vertex)){
						// Switches undirected pairs of vertices
						E label12 = g.getLabel(vertex, vertex2);
						E label21 = g.getLabel(vertex2, vertex);
						g.removeUndirected(vertex, vertex2);
						g.insertDirected(vertex2, vertex, label12);
						g.insertDirected(vertex, vertex2, label21);
					}
					else if(g.hasEdge(vertex, vertex2)){
						// Switches directional vertices from vertex to vertex2
						E label = g.getLabel(vertex, vertex2);
						g.removeDirected(vertex, vertex2);
						g.insertDirected(vertex2, vertex, label);
					}
					else if(g.hasEdge(vertex2, vertex)){
						// Switches directional vertices from vertex2 to vertex
						E label = g.getLabel(vertex, vertex2);
						g.removeDirected(vertex2, vertex);
						g.insertDirected(vertex, vertex2, label);
					}
				}
				// adds both vertices to visitedVertices set
				visitedVertices.add(vertex);
				visitedVertices.add(vertex2);
			}
		}

		return g;

	}
}
