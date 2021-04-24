import java.util.ArrayList;

/**
 * Demonstrate ArrayList features
 * @author Tim Pierson, Dartmouth CS 10, Spring 2019
 *
 */
public class ArrayListDemo {

	public static void main(String[] args) {
		ArrayList<Integer> a = new ArrayList<Integer>();
		a.add(1);
		a.add(2);
		a.add(1,3);
		System.out.println(a);
		Integer b = a.get(1);
		System.out.println(b);
		a.remove(1);
		System.out.println(a);
		a.set(1, 4);
		System.out.println(a);
		System.out.println(a.size());
	}
}
