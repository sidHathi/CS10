import java.util.*;

/**
 * Nondeterministic finite automata example
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
public class NFA {
	String start;
	Set<String> ends;
	Map<String, Map<Character,List<String>>> transitions; // state -> (character -> [next states])
	// note the difference from DFA: can have multiple different transitions from state for character

	/**
	 * Constructs the NFA from the arrays, as specified in the overall header
	 */
	NFA(String[] ss, String[] ts) {
		ends = new TreeSet<String>();
		transitions = new TreeMap<String, Map<Character,List<String>>>();

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
			if (!transitions.containsKey(from)) transitions.put(from, new TreeMap<Character,List<String>>());
			for (int i=2; i<pieces.length; i++) {
				char c = pieces[i].charAt(0);
				// difference from DFA: list of next states
				if (!transitions.get(from).containsKey(c)) transitions.get(from).put(c, new ArrayList<String>());
				transitions.get(from).get(c).add(to);				
			}
		}

		System.out.println("start:"+start);
		System.out.println("end:"+ends);
		System.out.println("transitions:"+transitions);
	}

	/**
	 * Returns whether or not the NFA accepts the string --
	 * follows transitions according to its characters, landing in an end state at the end of the string
	 */
	public boolean match(String s) {
		// difference from DFA: multiple current states
		Set<String> currStates = new TreeSet<String>();
		currStates.add(start);
		for (int i=0; i<s.length(); i++) {
			char c = s.charAt(i);
			Set<String> nextStates = new TreeSet<String>();
			// transition from each current state to each of its next states
			for (String state : currStates) {
				if (transitions.get(state).containsKey(c)) {
					nextStates.addAll(transitions.get(state).get(c));
				}
			}
			if (nextStates.isEmpty()) return false; // no way forward for this input
			currStates = nextStates;
		}
		// end up in multiple states -- accept if any is an end state
		for (String state : currStates) {
			if (ends.contains(state)) return true;
		}
		return false;
	}

	/**
	 * Helper method to test matching against a bunch of strings, printing the results
	 */
	public void test(String name, String[] inputs) {
		System.out.println("***" + name);
		for (String s : inputs) System.out.println(s + ":" + match(s));
	}

	public static void main(String[] args) {
		String[] ss1 = { "A,S", "B,E", "C" };
		String[] ts1 = { "A,B,0", "A,C,1", "B,B,0,1", "C,C,0,1" };
		NFA dfa1 = new NFA(ss1, ts1);
		
		String[] testsT1 = { "0", "00", "00000", "0010101" };
		dfa1.test("testsT1", testsT1);
		String[] testsF1 = { "", "1", "1100110" };
		dfa1.test("testsF1", testsF1);
		
		String[] ss1b = { "A,S", "B,E" };
		String[] ts1b = { "A,B,0", "B,B,0,1" };
		NFA nfa1 = new NFA(ss1b, ts1b);

		nfa1.test("testsT1b", testsT1);
		nfa1.test("testsF1b", testsF1);
		
		String[] ss2 = { "A,S", "B", "C", "D,E", "E,E" };
		String[] ts2 = { "A,A,0,1", "A,B,1", "B,D,1", "D,D,0,1", "A,C,0", "C,E,0", "E,E,0,1" };
		NFA nfa2 = new NFA(ss2, ts2);
		
		String[] testsT2 = { "00", "001", "0001100", "010110101", "101010100" };
		nfa2.test("testsT2", testsT2);
		String[] testsF2 = { "0", "010", "0101010", "1", "101010101010101010101" };
		nfa2.test("testsF2", testsF2);
	}
}
