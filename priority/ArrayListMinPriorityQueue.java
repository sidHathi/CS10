import java.util.ArrayList;

/**
 * Class to implement a min-priority queue with an ArrayList.

 * @author THC and Scot Drysdale
 */
public class ArrayListMinPriorityQueue<E extends Comparable<E>> 
implements MinPriorityQueue<E> {
	private ArrayList<E> list;  // list of elements

	/**
	 * Constructor
	 */
	public ArrayListMinPriorityQueue() {
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
		list.add(element);
	}

	/**
	 * Return the index of the element with the minimum key.
	 * @return the index of the element with the minimum key.
	 */
	private int indexOfMinimum() {
		// Search through the entire array for the smallest element.
		int smallest = 0;

		for (int i = 1; i < list.size(); i++) {
			// If the current smallest is greater than the element at index i,
			// then make the element at index i the new smallest.
			if (list.get(smallest).compareTo(list.get(i)) > 0)
				smallest = i;
		}

		return smallest;
	}

	/**
	 * Return the element with the minimum key, without removing it from the queue.
	 * @return the element with the minimum key, or null if queue empty.
	 */
	public E minimum() {
		if (list.size() <= 0)
			return null;
		else
			return list.get(indexOfMinimum());
	}

	/**
	 * Return the element with the minimum key, and remove it from the queue.
	 * @return the element with the minimum key, or null if queue empty.
	 */
	public E extractMin() {
		if (list.size() <= 0)
			return null;
		else {
			int smallest = indexOfMinimum();   // index of the element with the minimum key
			E minElement = list.get(smallest);  // the actual element

			// Move the element in the last position to this position.
			// Faster than removing from the middle.
			list.set(smallest, list.get(list.size()-1));

			// We no longer have an element in that last position.
			list.remove(list.size()-1);

			// Return the element with the minimum key.
			return minElement;
		}
	}

	// A testing program
	public static void main (String [] args)  {
		MinPriorityQueue<String> pq = new ArrayListMinPriorityQueue<String>();
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