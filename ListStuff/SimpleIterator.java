/**
 * A basic interface for a generic list iterator
 */
public interface SimpleIterator<T> {
	/**
	 * Is there anything left to retrieve?
	 */
	public boolean hasNext();

	/**
	 * Returns the next item and advances the iterator.
	 * Throws an exception if there is no next item.
	 */
	public T next() throws Exception;
}
