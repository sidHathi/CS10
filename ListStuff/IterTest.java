/**
 * Simple tests of list iterators
 */
public class IterTest {
	public static void main(String[] args) throws Exception {
		SimpleIList<String> list1 = new ISinglyLinked<String>(), list2 = new ISinglyLinked<String>();
		//SimpleIList<String> list1 = new IGrowingArray<String>(), list2 = new IGrowingArray<String>();
		list1.add(0, "e");
		list1.add(0, "d");
		list1.add(0, "c");
		list1.add(0, "b");
		list1.add(0, "a");
		list2.add(0, "a");
		list2.add(1, "b");
		list2.add(2, "c");
		list2.add(3, "d");
		list2.add(4, "e");

		// Print the elements, using an index
		for (int i=0; i<list1.size(); i++)
			System.out.println(list1.get(i));

		// See if the two are equal, using an index
		boolean equal = list1.size() == list2.size();
		for (int i=0; equal && i<list1.size(); i++)
			equal &= list1.get(i) == list2.get(i);
		System.out.println(equal);

		// Print the elements, using an iterator
		for (SimpleIterator<String> i = list1.newIterator(); i.hasNext(); )
			System.out.println(i.next());

		// See if the two are equal, using an iterator
		equal = true;
		SimpleIterator<String> i1, i2;
		for (i1 = list1.newIterator(), i2 = list2.newIterator(); equal && i1.hasNext() && i2.hasNext(); )
			equal &= i1.next().equals(i2.next());
		equal &= (!i1.hasNext() && !i2.hasNext());
		System.out.println(equal);
	}
}
