import java.util.*;

public class PopularPath<V,E> {
    private Graph<V,E> g;                             // the network being analyzed -- assume already set
    private List<Map<V, Map<V,Integer>>> backCounts;  // for each path length (# steps away from start)
                                                      // the backtrace count: v -> u ->how many times the u->v edge from g was used
                                                      // in a path of that length

    /**
     * Returns a random neighbor of u in g.
     * Extracted from version in lecture notes.
     * Already implemented and used correctly; just here in case you wondered what was going on.
     */
    public V randomNeighbor(V u) {
        // A random number from 0 (inclusive) to # neighbors (not inclusive)
        int nbr = (int)(g.outDegree(u) * Math.random());
        // Iterate through the out-neighbors the given number of times
        Iterator<V> iter = g.outNeighbors(u).iterator();
        V v = iter.next();
        while (nbr > 0) {
            v = iter.next();
            nbr--;
        }
        return v;
    }

    /**
     * Takes numExplorations random walks from start
     * For each step in in the walk, chooses whether to continue or not with probability keepOn
     * (if no out edge, then necessary stops)
     * Stores edge use counts in instance variable backCounts
     */
    public void exploreFrom(V start, double keepOn, int numExplorations) {
        if (!g.hasVertex(start) || keepOn <= 0 || keepOn >= 1) return;

        // The random walk structure from lecture is here; you just need to augment it to keep the counts
        for (int exploration=0; exploration<numExplorations; exploration++) {
            V curr = start;
            while (Math.random()<keepOn) {
                if (g.outDegree(curr) == 0) break; // end of the line
                V next = randomNeighbor(curr);     // choose a random neighbor

                //  Initializes backtrace map if this is the first run through for this exploration
                if (backCounts.get(exploration) == null){
                    backCounts.add(new HashMap<>());
                }

                // Updates the counts stored in the backtrace map
                if (!backCounts.get(exploration).containsKey(next)){
                    backCounts.get(exploration).put(next, new HashMap<>());
                }
                if (!backCounts.get(exploration).get(next).containsKey(curr)){
                    backCounts.get(exploration).get(next).put(curr, 0);
                }
                backCounts.get(exploration).get(next).put(curr, backCounts.get(exploration).get(next).get(curr) + 1);

                curr = next;
            }
        }
    }

    /**
     * Finds a path from end back to the start used in exploreFrom according to the stats in backCounts:
     * using the smallest number of steps by which end can be reached,
     * such that each step from v back to some u choses as u the most-frequent predecessor for v.
     * Throws an exception if there is no path to end.
     */
    public List<V> path(V end) throws Exception {

        for (int i = 0; i<backCounts.size(); i++){
            // Finds the first item in the backtrace list where the end appears
            if (backCounts.get(i).containsKey(end)){
                int step = i;
                List<V> path = new ArrayList<V>();
                V curr = end;
                // Moves backwards in the list, finding the back trace for the node, adding it to the path and repeating until
                // we reach the beginning of the list
                while (step >= 0){
                    path.add(curr);
                    if (backCounts.get(step).containsKey(curr)) {
                        curr = getMostPopularVertex(backCounts.get(step).get(curr));
                    }
                    else{
                        throw new Exception("Path failed");
                    }
                    step --;
                }
            }
        }

        throw new Exception("vertex not in backtrace");
    }

    public V getMostPopularVertex(Map<V, Integer> backCount){
        int bestScore = 0;
        V bestVertex = null;
        for (V vertex: backCount.keySet()){
            if (backCount.get(vertex) > bestScore){
                bestScore = backCount.get(vertex);
                bestVertex = vertex;
            }
        }
        return bestVertex;
    }
}
