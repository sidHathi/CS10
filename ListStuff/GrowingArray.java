/**
 * An array (expanding as needed) implementation of the SimpleList interface
 * 
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2012, 
 * based on a version in Goodrich & Tamassia
 */
public class GrowingArray<T> implements SimpleList<T> {
	private T[] array;
	private int size;		// how much of the array is actually filled up so far
	private static final int initCap = 10; // how big the array should be initially

	public GrowingArray() {
		array = (T[]) new Object[initCap];  // java generics oddness -- create an array of objects and typecast to array of T
		size = 0;
	}

	public int size() {
		return size;
	}

	public void add(int idx, T item) throws Exception {        
		if (idx > size || idx < 0) throw new Exception("invalid index");
		if (size == array.length) {
			// Double the size of the array, to leave more space
			T[] copy = (T[]) new Object[size*2];
			// Copy it over
			for (int i=0; i<size; i++) copy[i] = array[i];
			array = copy;
		}
		// Shift right to make room
		for (int i=size-1; i>=idx; i--) array[i+1] = array[i];
		array[idx] = item;
		size++;
	}

	public void remove(int idx) throws Exception {
		if (idx > size-1 || idx < 0) throw new Exception("invalid index");
		// Shift left to cover it over
		for (int i=idx; i<size-1; i++) array[i] = array[i+1];
		size--;
	}

	public T get(int idx) throws Exception {
		if (idx >= 0 && idx < size) return array[idx];
		else throw new Exception("invalid index");
	}

	public void set(int idx, T item) throws Exception {
		if (idx >= 0 && idx < size) array[idx] = item;
		else throw new Exception("invalid index");
	}

	public String toString() {
		String result = "[";
		for (int i=0; i<size; i++) {
			if (i>0) result += ",";
			result += array[i];
		}
		result += "]";

		return result;
	}
}
