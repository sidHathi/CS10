import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Libary class that contains functions for processing Kevin Bacon Graphs
 */
public class PS4GraphLib {

    /**
     * Uses breadth first search to return a graph respresentation of a tree
     * where each vertex is node with directed edges pointing toward their parent
     * @param g
     * @param source
     * @param <V>
     * @param <E>
     * @return
     */
    public static <V,E> Graph<V,E> bfs(Graph<V,E> g, V source){
        Graph<V, E> bfsTree = new AdjacencyMapGraph<V, E>();
        bfsTree.insertVertex(source);

        Set<V> visited = new HashSet<V>();
        Queue<V> queue = new LinkedList<V>();

        queue.add(source);
        visited.add(source);
        while(!queue.isEmpty()){
            V u = queue.remove();
            for (V v: g.outNeighbors(u)){
                if (!visited.contains(v)) {
                    visited.add(v);
                    queue.add(v);
                    bfsTree.insertVertex(v);
                    bfsTree.insertDirected(v, u, g.getLabel(u, v));
                }
            }
        }

        return bfsTree;
    }

    /**
     * Returns the shortest path between some vertex in the tree and the root
     * in list form
     * @param tree
     * @param v
     * @param <V>
     * @param <E>
     * @return
     */
    public static <V,E> List<V> getPath(Graph<V,E> tree, V v){
        ArrayList<V> shortestPath = new ArrayList<V>();
        V currentVertex = v;
        shortestPath.add(currentVertex);

        while (tree.outDegree(currentVertex) > 0){
            for (V u: tree.outNeighbors(currentVertex)){
                shortestPath.add(u);
                currentVertex = u;
            }
        }

        return shortestPath;
    }

    /**
     * Returns a set of vertices who aren't connected to the root
     * @param graph
     * @param subgraph
     * @param <V>
     * @param <E>
     * @return
     */
    public static <V,E> Set<V> missingVertices(Graph<V,E> graph, Graph<V,E> subgraph){
        Set<V> missingVertices = new HashSet<V>();

        for (V v: graph.vertices()){
            if (!subgraph.hasVertex(v)){
                missingVertices.add(v);
            }
        }

        return missingVertices;
    }

    /**
     * Returns the average separation of each vertex in the tree using the recursive helper function sumHelper
     * @param tree
     * @param root
     * @param <V>
     * @param <E>
     * @return
     */
    public static <V,E> double averageSeparation(Graph<V,E> tree, V root){

        // Passed as array for contiguous placement in memory
        int sum[] = {0};

        sumHelper(tree, root, 0, sum);

        return ((double)(sum[0]))/((double)tree.numVertices());

    }

    /**
     * For each node in the tree, this helper function updates the sum based on the vertex's separation from the root
     * and calls the itself on the node's children
     * @param tree
     * @param currentVertex
     * @param pathDistance
     * @param sum
     * @param <V>
     * @param <E>
     */
    public static <V,E> void sumHelper(Graph<V,E> tree, V currentVertex, int pathDistance, int[] sum){

        sum[0] += pathDistance;

        if (tree.inDegree(currentVertex) > 0){
            for (V u: tree.inNeighbors(currentVertex)){
                sumHelper(tree, u, pathDistance+1, sum);
            }
        }
    }

    /**
     * Manually initializes a map of actors and movies based on PS4 doc and uses it to display
     * function outputs to the console.
     */
    public static void testFunctions(){
        Graph<String, Set<String>> testGraph = new AdjacencyMapGraph<String, Set<String>>();

        String kb = "Kevin Bacon";
        String a = "Alice";
        String c = "Charlie";
        String b = "Bob";
        String d = "Dartmouth";
        String n = "Nobody";
        String nf = "Nobody's Friend";

        Set<String> kba = new HashSet<String>();
        kba.add("A Movie");
        kba.add("E Movie");

        Set<String> kbb = new HashSet<String>();
        kbb.add("A Movie");

        Set<String> ab = new HashSet<String>();
        ab.add("A Movie");

        Set<String> ac = new HashSet<String>();
        ac.add("D Movie");

        Set<String> bc = new HashSet<String>();
        bc.add("C Movie");

        Set<String> cd = new HashSet<String>();
        cd.add("B Movie");

        Set<String> nnf = new HashSet<String>();
        nnf.add("F Movie");

        testGraph.insertVertex(kb);
        testGraph.insertVertex(a);
        testGraph.insertVertex(c);
        testGraph.insertVertex(b);
        testGraph.insertVertex(d);
        testGraph.insertVertex(n);
        testGraph.insertVertex(nf);

        testGraph.insertUndirected(kb, a, kba);
        testGraph.insertUndirected(kb, b, kbb);
        testGraph.insertUndirected(a, b, ab);
        testGraph.insertUndirected(a, c, ac);
        testGraph.insertUndirected(b, c, bc);
        testGraph.insertUndirected(c, d, cd);
        testGraph.insertUndirected(n, nf, nnf);

        System.out.println((testGraph));
        System.out.println(bfs(testGraph, kb));
        System.out.println(getPath(bfs(testGraph, kb), c));
        System.out.println(missingVertices(testGraph, bfs(testGraph, kb)));
        System.out.println(averageSeparation(bfs(testGraph, kb), kb));

    }

    public static void main(String[] args){
        testFunctions();
    }

}
