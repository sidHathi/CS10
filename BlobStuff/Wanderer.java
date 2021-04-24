import java.awt.*;

/**
 * A blob that moves randomly.
 */
public class Wanderer extends Blob {

	private Color color;

	public Wanderer(double x, double y) {
		super(x, y);
	}
		
	public Wanderer(double x, double y, double r) {
		super(x, y, r);
	}
		
	@Override
	public void step() {
		// Choose a new step between -1 and +1 in each of x and y
		dx = 2 * (Math.random()-0.5);
		dy = 2 * (Math.random()-0.5);
		x += dx;
		y += dy;
	}
}
