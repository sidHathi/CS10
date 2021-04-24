/**
 * A singly-linked list implementation of the SimpleStack interface.
 * 
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2012
 * @author CBK, Spring 2016, cleaned up inner class
*/

public class SLLStack<T> implements SimpleStack<T> {
	private Element top;	// top of the stack

	/**
	 * The linked elements
	 */
	private class Element {
		private T data;
		private Element next;

		public Element(T data, Element next) {
			this.data = data;
			this.next = next;
		}
	}

	public SLLStack() {
		top = null;
	}

	public boolean isEmpty() {
		return top == null;
	}

	public T peek() throws Exception {
		if (isEmpty()) throw new Exception("empty stack");
		return top.data;
	}

	public T pop() throws Exception {
		if (isEmpty()) throw new Exception("empty stack");
		T data = top.data;
		top = top.next;
		return data;
	}

	public void push(T element) {
		top = new Element(element, top);
	}
}
