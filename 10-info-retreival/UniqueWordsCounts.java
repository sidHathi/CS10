import java.util.*;

/**
 * Identifies unique words in a document, and their numbers of occurrences, via a map
 *
 * @author Haris Baig and Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2012
 * @author Tim Pierson, CS 10, Spring 2019 -- added comments
 * @author CBK, Winter 2021 -- Green Eggs and Ham
 */
public class UniqueWordsCounts {
	public static void main(String[] args) {
		String text = "I am Sam. Sam I am. That Sam-I-am! That Sam-I-am! I do not like that Sam-I-am! Do you like green eggs and ham? I do not like them, Sam-I-am. I do not like green eggs and ham.";
		String[] allWords = text.split("\\W+");  // Split at one or more non-"word" characters, i.e., split into words
		
		// Declare new Map to hold count of each word
		Map<String,Integer> wordCounts = new TreeMap<String,Integer>(); // word -> count

		// Loop over all the words split out of the string, adding to map or incrementing count
		for (String s: allWords) {
			String word = s.toLowerCase();
			
			// Check to see if we have seen this word before, update wordCounts appropriately
			if (wordCounts.containsKey(word)) {
				// Have seen this word before, increment the count
				wordCounts.put(word, wordCounts.get(word)+1);
			}
			else {
				// Have not seen this word before, add the new word
				wordCounts.put(word, 1);
			}
		}
		// Print word counts
		System.out.println(wordCounts);
	}
}
