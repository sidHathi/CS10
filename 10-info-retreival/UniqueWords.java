import javax.swing.*;
import java.util.*;

/**
 * Identifies unique words in a document, via a set
 *
 * @author Haris Baig and Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2012
 * @author Tim Pierson, CS 10, Spring 2019 -- added comments
 * @author CBK, Winter 2021 -- Green Eggs and Ham
 */
public class UniqueWords {
	public static void main(String[] args) {
		String text = "I am Sam. Sam I am. That Sam-I-am! That Sam-I-am! I do not like that Sam-I-am! Do you like green eggs and ham? I do not like them, Sam-I-am. I do not like green eggs and ham.";
		String[] allWords = text.split("\\W+");  // Split at one or more non-"word" characters, i.e., split into words

		// Declare new Set to hold unique words
		Set<String> uniqueWords = new TreeSet<String>();

		// Loop over all the words split out of the string, adding to set
		for (String s: allWords) {
			uniqueWords.add(s.toLowerCase()); // Calling add() method for duplicate words just overwrites existing entries
		}

		System.out.println(allWords.length + " words"); //note: this is not the set, this is the String array of words after parsing String page
		System.out.println(uniqueWords.size() + " unique words"); //this is the set, size returns how many elements (e.g., the unique words) are present
		System.out.println(uniqueWords); //print the unique words
	}
}
