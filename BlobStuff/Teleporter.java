/**
 * A blob that at random times jumps to a random position.
 * @author cbk, Winter 2021, introduced this random telporting behavior, rather than on command
 */
public class Teleporter extends Blob {
	private int xmax, ymax;		// size of teleporting area
	private int countdown = 0;	// how many steps left before teleporting

	public Teleporter(double x, double y, int xmax, int ymax) {
		super(x, y);
		this.xmax = xmax; this.ymax = ymax;
	}
	
	/**
	 * Moves the blob to a random new position
	 */
	@Override
	public void step() {
		if (countdown > 0) {
			countdown--; // one step closer to teleporting
		}
		else {
			x = Math.random() * xmax;
			y = Math.random() * ymax;
			countdown = (int)(1 + 10 * Math.random()); // at least one step between jumps, and up to 10
		}
	}
}
