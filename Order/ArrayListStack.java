import java.util.ArrayList;

/**
 * An ArrayList implementation of the SimpleStack interface.
 * 
 * @author Scot Drysdale 
 */

public class ArrayListStack<T> implements SimpleStack<T> {

	private ArrayList<T> list;    // Holds the stack

	/**
	 *  Construct an empty stack
	 */
	public ArrayListStack() {
		list = new ArrayList<T>();
	}

	public boolean isEmpty() {
		return list.size() == 0;
	}

	public T peek() throws Exception {
		if (isEmpty())
			throw new Exception("empty stack");
		else
			return list.get(list.size()-1);
	}

	public T pop() throws Exception {
		if (isEmpty())
			throw new Exception("empty stack");
		else
			return list.remove(list.size()-1);
	}

	public void push(T element) {
		list.add(element);
	}

	/**
	 * Test program
	 */
	public static void main (String [] args) throws Exception {
		ArrayListStack<String> stack = new ArrayListStack<String>();
		stack.push("cat");
		stack.push("dog");
		stack.push("bee");
		System.out.println("Top is: " + stack.peek());
		System.out.println("Top again: " + stack.pop());
		System.out.println("Next top is: " + stack.pop());
		System.out.println("Is it empty? : " + stack.isEmpty());
		stack.push("eagle");
		System.out.println("Next top is: " + stack.pop());
		System.out.println("Next top is: " + stack.pop());
		System.out.println("Is it empty? : " + stack.isEmpty());
		stack.push("bear");
		System.out.println("top is: " + stack.peek());
		System.out.println("top again: " + stack.pop());
		stack.push("cat");
		stack.push("dog");
		stack.push("sheep");
		stack.push("cow");
		stack.push("eagle");
		stack.push("bee");
		stack.push("lion");
		stack.push("tiger");
		stack.push("zebra");
		stack.push("ant");
		System.out.println("Bigger example:");
		System.out.println("top is: " + stack.pop());
		System.out.println("Next top is: " + stack.pop());
		System.out.println("Next top is: " + stack.pop());
		System.out.println("Next top is: " + stack.pop());
		System.out.println("Next top is: " + stack.pop());
		System.out.println("Next top is: " + stack.pop());
		System.out.println("Next top is: " + stack.pop());
		System.out.println("Next top is: " + stack.pop());
		System.out.println("Next top is: " + stack.pop());
		System.out.println("Next top is: " + stack.pop());
		System.out.println("peek of empty stack..." + stack.isEmpty());
		stack.peek();
	}

}
