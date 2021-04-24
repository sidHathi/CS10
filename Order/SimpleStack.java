/**
 * A simple Stack ADT interface. 
 *
 * @author Scot Drysdale
 * @author Chris Bailey-Kellogg: modified pop and peek to throw an Exception
 */
public interface SimpleStack<T> {

	/**
	 * Add an element onto the top of the stack
	 * @param element element to be pushed onto the stack
	 */
	public void push(T element);

	/**
	 * Remove and return the top element
	 * @return an element from the top of the stack.
	 */
	public T pop() throws Exception;

	/**
	 * Look at the top element without removing it
	 * @return the element on the top of the stack without changing it.
	 */
	public T peek() throws Exception;

	/**
	 * Is the stack empty?
	 * @return true iff stack is empty
	 */
	public boolean isEmpty();
}
