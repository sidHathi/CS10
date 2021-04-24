/** Demonstrate memory allocation for primitives
 * 
 * @author Tim Pierson, Dartmouth CS 10, Winter 2018
 *
 */
public class MemoryAllocationPrimitives {

	public static void main(String[] args) {
		//declare local variables
		int i; double d; boolean b; char c;
		
		//assign values to local variables
		i=7; d=1.6; b=true; c='a';
		
		//print new values
		System.out.println("Local variables: "+
				"i="+i+" d="+d+" b="+b+" c="+c);
	}
}
