/**
 * Test program for graph interface
 */
public class RelationshipsTest {
	public static void main(String [] args) {
		Graph<String, String> relationships = new AdjacencyMapGraph<String, String>();

		relationships.insertVertex("Alice");
		relationships.insertVertex("Bob");
		relationships.insertVertex("Charlie");
		relationships.insertVertex("Dartmouth");
		relationships.insertVertex("Elvis");
		relationships.insertDirected("Alice", "Dartmouth", "follower");
		relationships.insertDirected("Bob", "Dartmouth", "follower");
		relationships.insertDirected("Charlie", "Dartmouth", "follower");
		relationships.insertDirected("Elvis", "Dartmouth", "follower");
		relationships.insertUndirected("Alice", "Bob", "friend");
		relationships.insertDirected("Alice", "Elvis", "friend"); // not symmetric!
		relationships.insertDirected("Charlie", "Elvis", "follower");

		System.out.println("The graph:");
		System.out.println(relationships);

		System.out.println("\nLinks to Dartmouth = " + relationships.inDegree("Dartmouth"));

		System.out.println("\nLinks from Alice:");
		for (String to : relationships.outNeighbors("Alice"))
			System.out.println(to + " ("+relationships.getLabel("Alice", to)+")");

		System.out.println("\nLinks to Dartmouth:");
		for (String from : relationships.inNeighbors("Dartmouth"))
			System.out.println(from + " ("+relationships.getLabel(from, "Dartmouth")+")");

		System.out.println("\nElvis has left the building");
		relationships.removeVertex("Elvis");
		System.out.println("\nLinks from Alice:");
		for (String to : relationships.outNeighbors("Alice"))
			System.out.println(to + " ("+relationships.getLabel("Alice", to)+")");

		System.out.println("\nAlice & Charlie work together");
		relationships.insertUndirected("Alice", "Charlie", "co-worker");
		System.out.println("\nLinks from Alice:");
		for (String to : relationships.outNeighbors("Alice"))
			System.out.println(to + " ("+relationships.getLabel("Alice", to)+")");
		System.out.println("\nLinks from Charlie:");
		for (String to : relationships.outNeighbors("Charlie"))
			System.out.println(to + " ("+relationships.getLabel("Charlie", to)+")");
		
		System.out.println("\nAlice unfrieds Bob");
		relationships.removeDirected("Alice", "Bob");
		System.out.println("and Charlie gets fired");
		relationships.removeUndirected("Alice", "Charlie");
		System.out.println("\nLinks from Alice:");
		for (String to : relationships.outNeighbors("Alice"))
			System.out.println(to + " ("+relationships.getLabel("Alice", to)+")");
		
		System.out.println("\nThe final graph:");
		System.out.println(relationships);
	}
}
