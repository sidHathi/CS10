import java.util.*;

/**
 * Simple stack-based parenthesis matching
 * 
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2012
 * @author Tim Pierson, Dartmouth CS 10, Spring 2019 -- used Character on stack vs. integer
 */
public class MatchParens {
	public static String opens = "({[<“";	// opening parens
	public static String closes = ")}]>”";	// closing parens, in same order as their partners in opens

	/**
	 * Checks whether s is properly parenthesized and prints an appropriate message
	 */
	public static boolean check(String s) {
		System.out.println("checking "+s);
		Stack<Character> parenStack = new Stack<Character>();	// all the opened but not yet closed parens
		// loop through each character in String s
		for (int i = 0; i<s.length(); i++) { 
			Character c = s.charAt(i); //get character c at index i
			if ((opens.indexOf(c)) >= 0) { //check if c is an open paren
				//found open paren, push it on stack
				parenStack.push(c); 
			}
			else if ((closes.indexOf(c)) >= 0) { //check if c is a close paren
				//found close paren
				if (parenStack.isEmpty()) { //empty stack means no matching open
					System.out.println("\t unopened at position "+i);
					return false;
				}
				//see if close matches open paren on top of stack (both should have same index)
				if (opens.indexOf(parenStack.pop()) != closes.indexOf(c)) {
					System.out.println("\t mismatched at position "+i);
					return false;
				}
			}
		}

		//done with all characters in String s, check if stack not empty
		if (!parenStack.isEmpty()) {
			System.out.println("\t "+parenStack.size() + " unclosed");
			return false;
		}
		
		System.out.println("\t passed");
		return true;
	}

	public static void main(String args[]) {
		check("()");
		check("(]");
		check("({{}})[<>]");
		check("()(())((()))(((())))");
		check("(");
		check(")");
		check("(ok)");
		check("<<<<<<>>>>>");
		check("<<<<>>>>>");
		check("Students: [{“id”: 123, “name” : “Alice”},{“id”: 987, “name” : “Bob”}]");
	}
}
