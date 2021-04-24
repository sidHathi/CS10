import java.util.*;

/**
 * Demo of priority queue, using various comparisons
 * 
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2017
 */

public class OfficeHoursComparisons {
	public static void main(String[] args) throws Exception {
		// by default students will be ordered by howFarAlong (lowest is highest priority)
		PriorityQueue<Student> pq = new PriorityQueue<Student>();
		
		// various orders
		Map<String,Comparator<Student>> orders = new TreeMap<String,Comparator<Student>>();
		orders.put("%", (Student s1, Student s2) -> s1.howFarAlong - s2.howFarAlong);
		orders.put("-%", (Student s1, Student s2) -> s2.howFarAlong - s1.howFarAlong);
		orders.put("year", (Student s1, Student s2) -> s1.year - s2.year);
		orders.put("-year", (Student s1, Student s2) -> s2.year - s1.year);
		orders.put("name", (Student s1, Student s2) -> s1.name.compareTo(s2.name));
		orders.put("-name", (Student s1, Student s2) -> s2.name.compareTo(s1.name));
		orders.put("len", (Student s1, Student s2) -> s1.name.length() - s2.name.length());
		orders.put("-len", (Student s1, Student s2) -> s2.name.length() - s1.name.length());
		
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
				else if (terms[0].equals("priority")) {
					if (terms.length != 2 || !orders.keySet().contains(terms[1])) {
						System.err.println("Format: priority "+orders.keySet());
					}
					else {
						PriorityQueue<Student> reordered = new PriorityQueue<Student>(orders.get(terms[1]));
						reordered.addAll(pq);
						pq = reordered;
					}
				}
				else if (terms[0].equals("help")) {
					System.out.println("another happy customer: " + pq.remove());
				}
				else {
					System.err.println("Commands: add / priority / help");
				}
			}
		}
	}
}
