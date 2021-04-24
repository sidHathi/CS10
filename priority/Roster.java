import java.io.*;
import java.util.*;

/**
 * Demo of file input (happens to be with PQ)
 * 
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2016
 * @author CBK, Fall 2017, updated comparator for sorting; a few other minor things
 */
public class Roster {

	/**
	 * Loads a roster from a comma-separated values format file
	 * Each line should be <student name>,<student year>; e.g. "Alice,20"
	 */
	public static List<Student> readRoster(String fileName) {
		List<Student> roster = new ArrayList<Student>();
		BufferedReader input;
		
		// Open the file, if possible
        try {
    			input = new BufferedReader(new FileReader(fileName));
        } 
        catch (FileNotFoundException e) {
            System.err.println("Cannot open file.\n" + e.getMessage());
            return roster;
        }
        
        // Read the file
		try {
			// Line by line
			String line;
			int lineNum = 0;
			while ((line = input.readLine()) != null) {
				System.out.println("read @"+lineNum+"`"+line+"'");
				// Comma separated
				String[] pieces = line.split(",");
				if (pieces.length != 2) {
					System.err.println("bad #columns in line "+lineNum+":"+line);
				}
				else {
					// Extract year as an integer, if possible
					try {
						Student s = new Student(pieces[0], Integer.parseInt(pieces[1]));
						System.out.println("=>"+s);
						roster.add(s);
					}
					catch (NumberFormatException e) {
						System.err.println("bad number in line "+lineNum+":"+line);
					}
				}
				lineNum++;
			}
		}
		catch (IOException e) {
			System.err.println("IO error while reading.\n" + e.getMessage());
		}

		// Close the file, if possible
		try {
			input.close();
		}
		catch (IOException e) {
			System.err.println("Cannot close file.\n" + e.getMessage());
		}

		return roster;
	}
	
	/**
	 * Variation using finally
	 */
	public static List<Student> readRoster2(String fileName) throws IOException {
		List<Student> roster = new ArrayList<Student>();
		BufferedReader input;
		
		// Open the file, if possible
        try {
    			input = new BufferedReader(new FileReader(fileName));
        } 
        catch (FileNotFoundException e) {
            System.err.println("Cannot open file.\n" + e.getMessage());
            return roster;
        }
        
        // Read the file
		try {
			// Line by line
			String line;
			int lineNum = 0;
			while ((line = input.readLine()) != null) {
				System.out.println("read @"+lineNum+"`"+line+"'");
				// Comma separated
				String[] pieces = line.split(",");
				if (pieces.length != 2) {
					System.err.println("bad #columns in line "+lineNum+":"+line);
				}
				else {
					// Extract year as an integer, if possible
					try {
						Student s = new Student(pieces[0], Integer.parseInt(pieces[1]));
						System.out.println("=>"+s);
						roster.add(s);
					}
					catch (NumberFormatException e) {
						System.err.println("bad number in line "+lineNum+":"+line);
					}
				}
				lineNum++;
			}
		}
		finally {
			// Close the file, if possible
			try {
				input.close();
			}
			catch (IOException e) {
				System.err.println("Cannot close file.\n" + e.getMessage());
			}			
		}

		return roster;
	}

	public static void main(String[] args) throws Exception {
		List<Student> roster = readRoster("inputs/roster.csv");
		PriorityQueue<Student> pq = new PriorityQueue<Student>((Student s1, Student s2) -> s1.name.compareTo(s2.name));
		pq.addAll(roster);
		System.out.println("\nsorted roster:");
		while (!pq.isEmpty()) System.out.println(pq.remove());

		// do it again with the other reader
		System.out.println("\n\n***repeat....");
		roster = readRoster2("inputs/roster.csv");
		pq = new PriorityQueue<Student>((Student s1, Student s2) -> s1.name.compareTo(s2.name));
		pq.addAll(roster);
		System.out.println("\nsorted roster:");
		while (!pq.isEmpty()) System.out.println(pq.remove());
	}
}
