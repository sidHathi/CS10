import java.util.ArrayList;

/**
 * Class to implement a min-priority queue with a heap in an ArrayList.
 * Parts borrowed from Heapsort.java
 * 
 * @author Scot Drysdale
 */
public class HeapMinPriorityQueue<E extends Comparable<E>> 
implements MinPriorityQueue<E> {
	private ArrayList<E> heap;

	/**
	 *  Constructor
	 */
	public HeapMinPriorityQueue() {
		heap = new ArrayList<E>();
	}

	/**
	 * Return the element with the minimum key, and remove it from the queue.
	 * @return the element with the minimum key, or null if queue empty.
	 */
	public E extractMin() {
		if (heap.size() <= 0)
			return null;
		else {
			E minVal = heap.get(0);
			heap.set(0, heap.get(heap.size()-1));  // Move last to position 0
			heap.remove(heap.size()-1);
			minHeapify(heap, 0);
			return minVal;
		}
	}

	/**
	 * Insert an element into the priority queue.  
	 * Keep in heap order
	 * @param element the element to insert
	 */
	public void insert(E element) {
		heap.add(element);        // Put new value at end;
		int loc = heap.size()-1;  // and get its location

		// Swap with parent until parent not larger
		while (loc > 0 && heap.get(loc).compareTo(heap.get(parent(loc))) < 0) {
			swap(heap, loc, parent(loc));
			loc = parent(loc);
		}
	}

	/**
	 * Is the priority queue empty?
	 * @return true if the queue is empty, false if not empty.
	 */
	public boolean isEmpty() {
		return heap.size() == 0;
	}


	/**
	 * Return the element with the minimum key, without removing it from the queue.
	 * @return the element with the minimum key, or null if queue empty.
	 */
	public E minimum() {
		if (heap.size() <= 0)
			return null;
		else
			return heap.get(0);
	}

	/**
	 * Restore the min-heap property.  When this method is called, the min-heap
	 * property holds everywhere, except possibly at node i and its children.
	 * When this method returns, the min-heap property holds everywhere.
	 * @param a the list to sort
	 * @param i the position of the possibly bad spot in the heap
	 */
	private static <E extends Comparable<E>> void 
	minHeapify(ArrayList<E> a, int i) {
		int left = leftChild(i);    // index of node i's left child
		int right = rightChild(i);  // index of node i's right child
		int smallest;    // will hold the index of the node with the smallest element
		// among node i, left, and right

		// Is there a left child and, if so, does the left child have an
		// element smaller than node i?
		if (left <= a.size()-1 && a.get(left).compareTo(a.get(i)) < 0)
			smallest = left;   // yes, so the left child is the largest so far
		else
			smallest = i;      // no, so node i is the smallest so far

		// Is there a right child and, if so, does the right child have an
		// element smaller than the larger of node i and the left child?
		if (right <= a.size()-1 && a.get(right).compareTo(a.get(smallest)) < 0)
			smallest = right;  // yes, so the right child is the largest

		// If node i holds an element smaller than both the left and right
		// children, then the min-heap property already held, and we need do
		// nothing more.  Otherwise, we need to swap node i with the larger
		// of the two children, and then recurse down the heap from the larger child.
		if (smallest != i) {
			swap(a, i, smallest);
			minHeapify(a, smallest);
		}
	} 

	// Swap two locations i and j in ArrayList a.
	private static <E> void swap(ArrayList<E> a, int i, int j) {
		E t = a.get(i);
		a.set(i, a.get(j));
		a.set(j, t);
	}

	// Return the index of the left child of node i.
	private static int leftChild(int i) {
		return 2*i + 1;
	}

	// Return the index of the right child of node i.
	private static int rightChild(int i) {
		return 2*i + 2;
	}

	// Return the index of the parent of node i
	// (Parent of root will be -1)
	private static int parent(int i) {
		return (i-1)/2;
	}

	//A testing program
	public static void main (String [] args)  {
		System.out.println(parent(0));
		MinPriorityQueue<String> pq = new HeapMinPriorityQueue<String>();
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

