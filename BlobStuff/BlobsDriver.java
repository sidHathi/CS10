import java.util.ArrayList;
/**
 * Demonstrate ArrayList holding multiple Blobs
 * @author Chris Bailey-Kellogg
 * @author Tim Pierson, Dartmouth CS 10, Spring 2019 -- added print statements
 *
 */
public class BlobsDriver {

	public static void main(String[] args) {
		ArrayList<Blob> blobs = new ArrayList<Blob>();
		blobs.add(new Wanderer(10,10));
		blobs.add(new Bouncer(20,30,800,600));
		blobs.get(0).step(); // => the wanderer
		blobs.get(1).step(); // => the bouncer
		System.out.println(blobs.size()); // => 2
		System.out.println(blobs.get(0).getX());
	}
}
