import java.util.ArrayList;

/**
 * Class to implement a min-priority queue with a sorted ArrayList.
 * 
 * @author THC and Scot Drysdale
 */

public class SortedArrayListMinPriorityQueue<E extends Comparable<E>> 
implements MinPriorityQueue<E> {
	private ArrayList<E> list; // array of queue items

	/**
	 *  Constructor
	 */
	public SortedArrayListMinPriorityQueue() {
		list = new ArrayList<E>();
	}

	/**
	 * Is the priority queue empty?
	 * @return true if the queue is empty, false if not empty.
	 */
	public boolean isEmpty() {
		return list.size() == 0;
	}

	/**
	 * Insert an element into the priority queue.  
	 * Keep in decreasing order
	 * @param element the element to insert
	 */
	public void insert(E element) {
		int p;  // Current position in the list.  

		for (p = list.size(); p > 0 && list.get(p-1).compareTo(element) < 0; p--)
			;

		list.add(p, element);
	}


	/**
	 * Return the element with the minimum key, without removing it from the queue.
	 * @return the element with the minimum key, or null if queue empty.
	 */
	public E minimum() {
		if (list.size() == 0)
			return null;
		else
			return list.get(list.size() - 1);  // Last item is smallest
	}

	/**
	 * Return the element with the minimum key, and remove it from the queue.
	 * @return the element with the minimum key, or null if queue empty.
	 */
	public E extractMin() {
		if (list.size() == 0)
			return null;
		else {                                // Shrink the size
			return list.remove(list.size()-1);  // and return the smallest element
		}
	}

	// A testing program
	public static void main (String [] args)  {
		MinPriorityQueue<String> pq = new SortedArrayListMinPriorityQueue<String>();
		pq.insert("cat");
		pq.insert("dog");
		pq.insert("bee");
		System.out.println("Smallest is: " + pq.minimum());
		System.out.println("Smallest again: " + pq.extractMin());
		System.out.println("Next smallest is: " + pq.extractMin());
		System.out.println("Is it empty? : " + pq.isEmpty());
		pq.insert("eagle");
		System.out.println("Next smallest is: " + pq.extractMin());
		System.out.println("Next smallest is: " + pq.extractMin());
		System.out.println("Is it empty? : " + pq.isEmpty());
		System.out.println("Min of empty queue: " + pq.minimum());
		System.out.println("Remove min of empty queue: " + pq.extractMin());
		pq.insert("bear");
		System.out.println("Smallest is: " + pq.minimum());
		System.out.println("Smallest again: " + pq.extractMin());
		pq.insert("cat");
		pq.insert("dog");
		pq.insert("sheep");
		pq.insert("cow");
		pq.insert("eagle");
		pq.insert("bee");
		pq.insert("lion");
		pq.insert("tiger");
		pq.insert("zebra");
		pq.insert("ant");
		System.out.println("Bigger example:");
		System.out.println("Smallest is: " + pq.extractMin());
		System.out.println("Next smallest is: " + pq.extractMin());
		System.out.println("Next smallest is: " + pq.extractMin());
		System.out.println("Next smallest is: " + pq.extractMin());
		System.out.println("Next smallest is: " + pq.extractMin());
		System.out.println("Next smallest is: " + pq.extractMin());
		System.out.println("Next smallest is: " + pq.extractMin());
		System.out.println("Next smallest is: " + pq.extractMin());
		System.out.println("Next smallest is: " + pq.extractMin());
		System.out.println("Next smallest is: " + pq.extractMin());
		System.out.println("Next smallest is: " + pq.extractMin());
	}
}