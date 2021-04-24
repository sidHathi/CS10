import java.util.*;

/**
 * Demo of Comparable and Comparator
 * 
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Spring 2016
 * @author CBK, Fall 2016, updated to use Java's PQ, more examples
 * @author CBK, Fall 2017, factored out from Student, more examples
 */
public class Comparisons {
	public static void main(String[] args) {
		ArrayList<Student> students = new ArrayList<Student>();
		students.add(new Student("charlie", 18, 50));
		students.add(new Student("alice", 20, 95));
		students.add(new Student("bob", 19, 17));
		students.add(new Student("elvis", 50, 0));
		students.add(new Student("denise", 21, 17));
		System.out.println("the list:" + students);
		
		// Use the compareTo method (howFarAlong)
		PriorityQueue<Student> pq = new PriorityQueue<Student>();
		pq.addAll(students);
		System.out.println("how far along:");
		while (!pq.isEmpty()) System.out.println(pq.remove());
		
		// Use a custom Comparator.compare (lexicographic)
		class NameComparator implements Comparator<Student> {
			public int compare(Student s1, Student s2) {
				return s1.name.compareTo(s2.name);
			}
		} 
		Comparator<Student> nameCompare = new NameComparator();
		pq = new PriorityQueue<Student>(nameCompare);
		pq.addAll(students);
		System.out.println("\nlexicographic:");
		while (!pq.isEmpty()) System.out.println(pq.remove());

		// Use a custom Comparator.compare (length of name)
		class NameLengthComparator implements Comparator<Student> {
			public int compare(Student s1, Student s2) {
				return s1.name.length() - s2.name.length();
			}
		} 
		Comparator<Student> lenCompare = new NameLengthComparator();
		pq = new PriorityQueue<Student>(lenCompare);
		pq.addAll(students);
		System.out.println("\nlength:");
		while (!pq.isEmpty()) System.out.println(pq.remove());
		
		// Use a custom Comparator via Java 8 anonymous function (year)
		pq = new PriorityQueue<Student>((Student s1, Student s2) -> s1.year - s2.year);
		pq.addAll(students);
		System.out.println("\nyear:");
		while (!pq.isEmpty()) System.out.println(pq.remove());
	}

}
