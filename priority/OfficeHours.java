import java.util.*;

/**
 * Demo of priority queue
 * 
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2017
 */

public class OfficeHours {
	public static void main(String[] args) throws Exception {
		// students will be ordered by howFarAlong (lowest is highest priority)
		PriorityQueue<Student> pq = new PriorityQueue<Student>();
		
		Scanner in = new Scanner(System.in);
		while (true) {
			System.out.println("currently waiting:");
			System.out.println(pq);
			System.out.println();
			System.out.println("office hours >");
			String line = in.nextLine();
			String[] terms = line.split(" ");
			if (terms.length>0) {
				if (terms[0].equals("add")) {
					if (terms.length != 4) {
						System.err.println("format: add <name> <year> <howFarAlong>");
					}
					else {
						pq.add(new Student(terms[1], Integer.parseInt(terms[2]), Integer.parseInt(terms[3])));
					}
				}
				else if (terms[0].equals("help")) {
					System.out.println("another happy customer: " + pq.remove());
				}
				else {
					System.err.println("Commands: add / help");
				}
			}
		}
	}
}
