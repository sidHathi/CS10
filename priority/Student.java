/**
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Spring 2016
 * @author CBK, Fall 2016, updated to use Java's PQ, more examples
 * @author CBK, Fall 2017, factored out driver, added office hours example
 */
public class Student implements Comparable<Student> {
	public String name;
	public int year;
	public int howFarAlong;
	
	public Student(String name, int year) {
		this.name = name;
		this.year = year;
	}

	public Student(String name, int year, int howFarAlong) {
		this.name = name;
		this.year = year;
		this.howFarAlong = howFarAlong;
	}

	/**
	 * Comparable: who is less far along (and thus needs more help)
	 * If this is, then negative; if s2 is, then positive
	 */
	@Override
	public int compareTo(Student s2) {
		return howFarAlong - s2.howFarAlong;
	}

	@Override
	public String toString() {
		return name + " '"+year+" ("+howFarAlong+"%)";
	}
}
