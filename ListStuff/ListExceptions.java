/**
 * Illustrations of handling exceptions, with lists
 */
public class ListExceptions {
	public static void main(String[] args) { // note: no "throws exception", as every method that could is wrapped
		
		SimpleList<String> list = new SinglyLinked<String>();
		System.out.println(list);
		try {
			list.add(0, "a");
			list.add(1, "c");
			list.add(1, "b");
			list.set(2, "e");
			list.add(0, "z");
			list.remove(2);
			list.remove(0);
			list.remove(1);
			System.out.println(list);	
		}
		catch (Exception e) {
			System.err.println("why did I catch it?"); // shouldn't print -- we know this code is fine
		}
		
		try {
			list.add(-1, "?");
		}
		catch (Exception e) {
			System.out.println("caught it!"); // should print -- we know this is bogus
		}

		try {
			list.add(-1, "?");
		}
		catch (Exception e) {
			System.out.println("caught it again!"); // should print -- we know this is bogus
		}
		finally {
			System.out.println("finally 1"); // executed whether or not caught an error
		}
	
		try {
			list.add(0, "?");
		}
		catch (Exception e) {
			System.out.println("why did I catch it again!"); // shouldn't print -- we know this code is fine
		}
		finally {
			System.out.println("finally 2"); // executed whether or not caught an error
		}
}
}
