/**
 * Simple interface for graphs, simplified from Goodrich & Tamassia
 * Centered on directed edges, with undirected edges treated as pairs of directed edges 
 *
 * @param <V>	the type of vertices
 * @param <E>	the type of edge labels

 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Spring 2015 (based on G&T)
 */
public interface Graph<V,E> {
	/** How many vertices in the graph */
	public int numVertices();

	/** How many edges in the graph */
	public int numEdges();

	/** An iterable collection of the graph's vertices */
	public Iterable<V> vertices();

	/** Whether or not v is a vertex in the graph */
	public boolean hasVertex(V v);
	
	/** How many edges from the vertex in the graph */
	public int outDegree(V v);

	/** How many edges to the vertex in the graph */
	public int inDegree(V v);

	/** An iterable collection of the neighbors with edges from the vertex */
	public Iterable<V> outNeighbors(V v);

	/** An iterable collection of the neighbors with edges to the vertex */
	public Iterable<V> inNeighbors(V v);

	/** Whether or not there is an edge from u to v */
	public boolean hasEdge(V u, V v);

	/** The label on the edge from u to v */
	public E getLabel(V u, V v);
	
	/** Adds the vertex to the graph */
	public void insertVertex(V v);

	/** Adds (or replaces) an edge from u to v with the label  */
	public void insertDirected(V u, V v, E e);

	/** Adds (or replaces) a pair of edges between u & v with the label  */
	public void insertUndirected(V u, V v, E e);

	/** Removes a vertex and all its incident edges */
	public void removeVertex(V v);

	/** Removes a directed edge from u to v */
	public void removeDirected(V u, V v);

	/** Removes the pair of edges between u and v */
	public void removeUndirected(V u, V v);
}
