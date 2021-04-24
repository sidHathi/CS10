import java.io.*;
import java.util.*;

/**
 * Searches for a query word or set of words (not a phrase) in a set of documents
 *
 * Example Shakespeare files obtained from Project Gutenberg (www.gutenberg.org) and stripped down to the public domain text
 * 
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Winter 2014, extending an idea by CBK and Haris Baig, Fall 2012
 * @author CBK, Spring 2015, comparator
 * @author Tim Pierson, CS 10, Spring 2019 -- added comments
 * 
 */

public class Search {
	private static final String directory = "inputs/shakespeare"; // where all the files are
	
	Map<String, Map<String,Integer>> file2WordCounts;	// filename -> (map word -> count)
	// that is, for each file, for each word that it has, how many times it appears
	Map<String,Integer> numWords;						// filename -> # words
	Map<String,Integer> totalCounts;					// word -> count, total over files
	Map<String,Integer> numFiles;						// word -> # files containing it
	
	Search() {
		// Constructor, initialize Maps
		file2WordCounts = new TreeMap<String, Map<String,Integer>>();
		numWords = new TreeMap<String,Integer>();
		totalCounts = new TreeMap<String,Integer>();
		numFiles = new TreeMap<String,Integer>();
	}
	
	/**
	 * Loads the word->count map from the file (like UniqueWordsCounts, but from a file)
	 */
	void loadFile(File file) throws Exception {
		Map<String,Integer> wordCounts = new TreeMap<String,Integer>();

		// Loop over lines
		BufferedReader in = new BufferedReader(new FileReader(file));
		String line;
		int n=0; // # words
		while ((line = in.readLine()) != null) {
			// Loop over all the words split out of the string, adding to map or incrementing count
			String[] words = line.toLowerCase().split("\\W+"); // this says to split by one or more non-word characters
			for (String word: words) {
				if (word.isEmpty()) continue;
				if (wordCounts.containsKey(word)) {
					// Increment the count
					wordCounts.put(word, wordCounts.get(word)+1);
				}
				else {
					// Add the new word
					wordCounts.put(word, 1);
				}
				n++;
			}
		}
		in.close();
		
		file2WordCounts.put(file.getName(), wordCounts); // maps filename -> word -> count
		numWords.put(file.getName(), n); // maps filename -> number of words
	}
	
	/**
	 * Sums the word counts and file counts over the various files
	 */
	void computeTotals() {
		// Loop through all file names in file2WordCounts to create a set of words in *all* files and how many files each word appears in
		for (String file : file2WordCounts.keySet()) { //keySet() is an iterator consisting of file names
			// Look at all the words in that file
			Map<String,Integer> wordCounts = file2WordCounts.get(file);
			for (String word : wordCounts.keySet()) {
				// first record how many times each word is seen in all files
				if (totalCounts.containsKey(word)) {
					// have seen this word before, add how many times it appeared in this file to counts from previous files
					totalCounts.put(word, totalCounts.get(word) + wordCounts.get(word));
				}
				else {
					// have not seen this word before, add how many times it appeared in this file
					totalCounts.put(word, wordCounts.get(word));
				}
				
				// next, count how many files each word appears in
				if (numFiles.containsKey(word)) {
					// have seen this word before, increment word count
					numFiles.put(word, numFiles.get(word) + 1);
				}
				else {
					// have not seen this word before, set count to 1
					numFiles.put(word, 1);
				}
			}
		}
	}
	
	/**
	 * Prints word counts in the map, up to the given number of entries, either the most frequent (+number) or least frequent (-number)
	 */
	void printWordCounts(Map<String,Integer> wordCounts, int number) {
		// Sort the entries by their counts, breaking ties by alphabetical order
		List<Map.Entry<String,Integer>> entries = new ArrayList<Map.Entry<String,Integer>>(wordCounts.entrySet());
		Collections.sort(entries, new WordCountComparator());

		if (number > 0) {
			// Most frequent -- at end of list
			for (int i=0; i<Math.min(number, entries.size()); i++)
				System.out.println(entries.get(entries.size()-1-i));
		}
		else {
			// Least frequent -- at front of list
			for (int i=0; i<Math.min(-number, entries.size()); i++)
				System.out.println(entries.get(i));
		}
	}
	
	/**
	 * Returns the names of the files in which the term appears
	 */
	public Set<String> search(String term) {
		Set<String> good = new TreeSet<String>();
		for (String file : file2WordCounts.keySet()) {
			if (file2WordCounts.get(file).containsKey(term)) good.add(file);
		}
		return good;
	}
	
	/**
	 * Returns the names of the files in which all the terms appear
	 */
	public Set<String> search(String[] terms) {
		Set<String> good = new TreeSet<String>(file2WordCounts.keySet());
		
		// Intersection what was good for the previous terms with what is good for this term
		for (String term : terms) 
			good.retainAll(search(term));
		
		return good;
	}
	
	/**
	 * Returns the tf-idf score of the terms in the file
	 */
	public double tfIdf(String filename, String terms[]) {
		double s = 0;
		
		// Sum over terms
		for (String term : terms) {
			if (file2WordCounts.get(filename).containsKey(term))
				// term frequency * log (# files total / # files with term)
				s += file2WordCounts.get(filename).get(term) * Math.log((double)file2WordCounts.keySet().size() / numFiles.get(term));			
		}
		
		return s;
	}
	
	public static void main(String[] args) throws Exception {	
		Search searcher = new Search();

		// Find all the .txt files in the directory and load their word counts
		File dir = new File(directory);
		for (File file : dir.listFiles()) {
			if (file.isFile() && file.getName().endsWith(".txt")) {
				System.out.println("indexing "+file.getName());
				searcher.loadFile(file);
			}
		}

		// Total across files
		searcher.computeTotals();
		
		// Search!
		Scanner in = new Scanner(System.in);
		while (true) {
			System.out.println();
			System.out.println("search for >");
			String line = in.nextLine();
			String[] terms = line.split(" ");
			if (terms[0].equals("#")) {
				// Report word counts
				if (terms.length==1)
					System.err.println("Please enter the number of words (negative for least frequent) and optionally a filename (else total)");
				else if (terms.length==2)
					searcher.printWordCounts(searcher.totalCounts, Integer.parseInt(terms[1]));
				else if (searcher.file2WordCounts.containsKey(terms[2])) 
					searcher.printWordCounts(searcher.file2WordCounts.get(terms[2]), Integer.parseInt(terms[1]));
				else
					System.err.println("Sorry, I haven't read '"+terms[2]+"'");
			}
			else if (terms.length == 1) {
				// Report files containing word
				Set<String> files = searcher.search(terms[0]);
				if (files.isEmpty()) System.out.println("Sorry, couldn't find anything");
				else {
					System.out.println("Appears in "+files);
					for (String file : files) {
						System.out.println(file + ":" + searcher.file2WordCounts.get(file).get(terms[0]));
					}
				}
			}
			else {
				// Report files containing all words
				Set<String> files = searcher.search(terms);
				if (files.isEmpty()) System.out.println("Sorry, couldn't find anything");
				else {
					System.out.println("Appears in "+files);				
					for (String file : files) {
						// Give components of tf-idf score
						System.out.println(file + ":" + searcher.tfIdf(file, terms));
						for (String term : terms) 
							System.out.println("  "+term+" "+searcher.file2WordCounts.get(file).get(term)+"; in "+searcher.numFiles.get(term));
					}
				}
			}
		}
	}
}
