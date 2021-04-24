import java.io.*;
import java.util.*;

/**
 * Test program for graph lib: load graph and look at vertices by degree and by random walk
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2017
 */
public class GraphLibTest {
	/**
	 * Loads a graph where vertices are strings and edges are only about existence
	 * Each line in the file is of the format <from>:<to>,<to>,<to>
	 * where <from> is a vertex label and each <to> is a vertex label of an out-neighbor
	 */
	public static Graph<String, Boolean> loadGraph(String fileName) {
		Graph<String, Boolean> g = new AdjacencyMapGraph<String, Boolean>();
		
		// A single String instance of each value read (e.g., every time read "A", use the same "A" instance)
		Map<String,String> vs = new HashMap<String,String>();
		
		// Open the file, if possible
		BufferedReader input;
		try {
			input = new BufferedReader(new FileReader(fileName));
		} 
		catch (FileNotFoundException e) {
			System.err.println("Cannot open file.\n" + e.getMessage());
			return g;
		}

		// Read the file
		try {
			// Line by line
			String line;
			int lineNum = 0;
			while ((line = input.readLine()) != null) {
				// Colon separated from:to
				String[] pieces = line.split(":");
				if (pieces.length != 2) {
					System.err.println("bad from:to in line "+lineNum+":"+line);
				}
				else {
					// Comma-separated tos
					String from = pieces[0];
					// Keep track of the canonical String, adding to graph if not already there
					if (vs.containsKey(from)) from = vs.get(from);
					else {
						vs.put(from, from);
						g.insertVertex(from);
					}
					String[] tos = pieces[1].split(",");
					for (String to : tos) {
						// Keep track of the canonical String, adding to graph if not already there
						if (vs.containsKey(to)) to = vs.get(to);
						else {
							vs.put(to, to);
							g.insertVertex(to);
						}
						g.insertDirected(from, to, true);
					}
				}
				lineNum++;
			}
		}
		catch (IOException e) {
			System.err.println("IO error while reading.\n" + e.getMessage());
		}
		finally {
			// Close the file, if possible
			try {
				input.close();
			}
			catch (IOException e) {
				System.err.println("Cannot close file.\n" + e.getMessage());
			}			
		}
		
		return g;
	}
	
	public static void main(String [] args) {
		Graph<String, Boolean> relationships = loadGraph("inputs/simpleGraph2.txt");
		System.out.println(relationships);

		System.out.println("by in degree:" + GraphLib.verticesByInDegree(relationships));

		System.out.println("some random walks from A:");
		for (int i=0; i<10; i++) {
			System.out.println(GraphLib.randomWalk(relationships, "A", 0.9));
		}

		System.out.println("walking...");
		Map<String,Integer> freqs = GraphLib.randomWalks(relationships, 0.99, 1000);
		System.out.println("by walk frequency:" + GraphLib.verticesByFrequency(relationships, freqs));
		
		// Tests for SA-7
		System.out.println(relationships);
		for (String v : relationships.vertices()) {
			System.out.println(v + ":" + GraphLib.suggestions(relationships, v));			
		}
		
		System.out.println(GraphLib.flip(relationships));
	}
}
