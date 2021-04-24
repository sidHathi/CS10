/** Demonstrate memory allocation for objects
 * 
 * @author Tim Pierson, Dartmouth CS 10, Winter 2018
 *
 */
public class MemoryAllocationObjects {

	public static void main(String[] args) {
		//declare Blob objects
		Blob alice = new Blob();
		Blob bob; //notice no new keyword
		bob = alice; //bob equals alice
		Blob charlie = new Blob();
		System.out.println("alice.x="+alice.x+
				" bob.x="+bob.x);
		
		//update alice's x
		alice.setX(3);
		System.out.println("alice.x="+alice.x+
				" bob.x="+bob.x);
		
		//printing objects implicitly calls toString()
		System.out.println("alice="+alice+
				" bob="+bob+" charlie="+charlie);
	}
}
