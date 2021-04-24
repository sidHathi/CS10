/**
 * A simple interface for a Queue ADT
 *
 * @author Scot Drysdale
 * @author Chris Bailey-Kellogg: modified dequeue and front to throw an Exception
 */
public interface SimpleQueue<T> {

	/**
	 * Add item to rear of queue
	 * @param item item to be enqueued
	 */
	public void enqueue(T item);

	/**
	 * Remove item from front of queue
	 * @return the item removed from the front of the queue
	 */
	public T dequeue() throws Exception;

	/**
	 * Return the item at the front of queue, but do not remove it
	 * @return the item at the front of the queue
	 */
	public T front() throws Exception;

	/**
	 * Is the queue empty?
	 * @return true iff queue is empty
	 */
	public boolean isEmpty();

}
