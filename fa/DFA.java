import java.util.*;

/**
 * Deterministic finite automata example
 * 
 * Specify states as an array of their names, with ",S" (start) after one of them and ",E" (end) after at least one of them
 * Specify transitions as an array of "from,to,label" for a unique label or "from,to,label1,label2,..." for more
 *
 * Then try matching a string
 * 
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Winter 2014
 * @author inspired by a CS 8 problem set by Scot Drysdale (more beautiful in Haskell, of course)
 * @author CBK, small revisions, Spring 2016
 *
 */
public class DFA {
	String start;
	Set<String> ends;
	Map<String, Map<Character,String>> transitions; // state -> (character -> next state)

	/**
	 * Constructs the DFA from the arrays, as specified in the overall header
	 */
	DFA(String[] ss, String[] ts) {
		ends = new TreeSet<String>();
		transitions = new TreeMap<String, Map<Character,String>>();

		// States
		for (String v : ss) {
			String[] pieces = v.split(",");
			if (pieces.length>1) {
				if (pieces[1].equals("S")) start = pieces[0];
				else if (pieces[1].equals("E")) ends.add(pieces[0]);
			}
		}

		// Transitions
		for (String e : ts) {
			String[] pieces = e.split(",");
			String from = pieces[0], to = pieces[1];
			if (!transitions.containsKey(from)) transitions.put(from, new TreeMap<Character,String>());
			for (int i=2; i<pieces.length; i++) {
				transitions.get(from).put(pieces[i].charAt(0), to);				
			}
		}

		System.out.println("start:"+start);
		System.out.println("end:"+ends);
		System.out.println("transitions:"+transitions);
	}

	/**
	 * Returns whether or not the DFA accepts the string --
	 * follows transitions according to its characters, landing in an end state at the end of the string
	 */
	public boolean match(String s) {
		String curr = start; // where we are now
		for (int i=0; i<s.length(); i++) {
			char c = s.charAt(i);
			if (!transitions.get(curr).containsKey(c)) {
				System.out.println("This isn't a DFA! No transition from "+curr+" for "+c);
				return false;
			}
			curr = transitions.get(curr).get(c); // take a step according to c
		}
		return ends.contains(curr); // did we end up in one of the desired final states?
	}

	/**
	 * Helper method to test matching against a bunch of strings, printing the results
	 */
	public void test(String[] inputs) {
		for (String s : inputs) System.out.println(s + ":" + match(s));
	}

	public static void main(String[] args) {
		String[] ss1 = { "A,S", "B,E", "C" };
		String[] ts1 = { "A,B,0", "A,C,1", "B,B,0,1", "C,C,0,1" };
		DFA dfa1 = new DFA(ss1, ts1);

		String[] testsT1 = { "0", "00", "00000", "0010101" };
		dfa1.test(testsT1);
		String[] testsF1 = { "", "1", "1100110" };
		dfa1.test(testsF1);
	}
}
