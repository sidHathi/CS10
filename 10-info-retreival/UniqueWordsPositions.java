import java.util.*;

/**
 * Identifies unique words in a document, and the positions of their occurrences, via a map
 *
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2012
 * @author Tim Pierson, CS 10, Spring 2019 -- added comments
 * @author CBK, Winter 2021 -- Green Eggs and Ham
 */
public class UniqueWordsPositions {
	public static void main(String[] args) {
		String text = "I am Sam. Sam I am. That Sam-I-am! That Sam-I-am! I do not like that Sam-I-am! Do you like green eggs and ham? I do not like them, Sam-I-am. I do not like green eggs and ham.";
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
		System.out.println(wordPositions);
	}
}
