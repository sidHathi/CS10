import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

/**
 * Identifies unique words in a document, and the positions of their occurrences, via a map
 * Gets input from a file instead of a hard-coded string
 * 
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Spring 2015
 * @author Tim Pierson, CS 10, Spring 2019 -- added comments
 * @author CBK, Winter 2021 -- Green Eggs and Ham
 */
public class UniqueWordsPositionsFile {
	/**
	 * Collects all the lines from a file into a single string.
	 */
	private static String loadFileIntoString(String filename) throws Exception {
		BufferedReader in = new BufferedReader(new FileReader(filename));
		String str = "", line;
		while ((line = in.readLine()) != null) str += line;
		in.close();
		return str;
	}

	public static void main(String[] args) throws Exception {
		String text = loadFileIntoString("inputs/Green-Eggs-And-Ham.txt");
		String[] allWords = text.split("\\W+");  // Split at one or more non-"word" characters, i.e., split into words

		// Declare new Map, each entry in the Map is a List that will hold the index where each word occurs (might be multiple locations)
		Map<String,List<Integer>> wordPositions = new TreeMap<String,List<Integer>>(); // word -> [position1, position2, ...]

		// Loop over all the words split out of the string, adding their positions in the string to the map
		for (int i=0; i<allWords.length; i++) {
			String word = allWords[i].toLowerCase();
			
			// Check to see if we have seen this word before, update wordCounts appropriately
			if (wordPositions.containsKey(word)) {
				// Now each item in the Map is a List of Integers, add the position of the word to the List
				wordPositions.get(word).add(i);
			}
			else {
				// Add the new word with a new list containing just this position
				List<Integer> positions = new ArrayList<Integer>();
				positions.add(i);
				wordPositions.put(word, positions);
			}
		}
		System.out.println(wordPositions.size() + " words: " + wordPositions.keySet());
		System.out.println(wordPositions);
	}
}
