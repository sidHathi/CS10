/**
 * A blob that moves in a particular direction, but bounces off the walls.
 */
public class Bouncer extends Blob {
	private int xmax, ymax;	// size of bouncing area

	/**
	 * Initializes with the given coordinates and bouncing area size, and random step sizes
	 */
	public Bouncer(double x, double y, int xmax, int ymax) {
		super(x, y);
		this.xmax = xmax; this.ymax = ymax;

		// Step size randomly between -r and +r
		dx = 2 * r * (Math.random() - 0.5);
		dy = 2 * r * (Math.random() - 0.5);
	}

	@Override
	public void step() {
		x += dx;
		y += dy;
		// Handle the bounce, accounting for radius.
		if (x > xmax - r) { //right side
			x = xmax - r;
			dx = -dx;
		}
		else if (x < r) { //left side
			x = r;
			dx = -dx;
		}
		if (y > ymax - r) { //bottom
			y = ymax - r;
			dy = -dy;
		}
		else if (y < r) { //top
			y = r;
			dy = -dy;
		}
	}
}
