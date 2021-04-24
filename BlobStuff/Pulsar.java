/**
 * A blob that sits still and pulsates (radius increases/decreases).
 */
public class Pulsar extends Blob {
	public Pulsar(double x, double y) {
		super(x, y);
		dr = 1 + Math.random(); // random growth
	}
	
	@Override
	public void step() {
		r += dr;
		// Make sure radius is within bounds, switching between positive growing & negative growing (shrinking)
		if (r < 1 || r > 10) {
			dr = -dr;
			r += dr;
		}
	}
}
