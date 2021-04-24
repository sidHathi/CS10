/**
 * A very simple implementation of a class implementing the Point2D interface
 * Called it a "Dot" to distinguish from Point
 * Can keep a name for testing/debugging purposes
 * 
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2016
 * @author CBK, Winter 2021, named dots for testing/debugging
 */
public class Dot implements Point2D {
	private double x, y;
	private String name;

	public Dot(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public Dot(double x, double y, String name) {
		this.x = x;
		this.y = y;
		this.name = name;
	}
	
	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}
	
	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public String toString() {
		if (name == null) return "("+x+","+y+")";
		return name + "@("+x+","+y+")";
	}
}
