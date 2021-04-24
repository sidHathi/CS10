import java.util.Comparator;
import java.util.Map;

/**
 * Comparator for (word,count) map entries
 * 
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Spring 2015
 */
public class WordCountComparator implements Comparator<Map.Entry<String,Integer>> {
	public int compare(Map.Entry<String,Integer> e1, Map.Entry<String,Integer> e2) {
		if (e1.getValue() < e2.getValue()) return -1;
		else if (e1.getValue() > e2.getValue()) return 1;
		else return e1.getKey().compareTo(e2.getKey());
	}
}
