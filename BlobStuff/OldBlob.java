import java.awt.*;

/**
 * Animated blob, defined by a position and size, 
 * and the ability to step (move/grow), draw itself, and see if a point is inside.
 * 
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Spring 2016, based on animated agents from previous terms
 */

public class OldBlob {
	protected double x=0, y=0, r=5;			// position and size
	protected double dx=0, dy=0, dr=0;		// velocity and growth

	public OldBlob() {
		// Do nothing; everything has its default value
	}

	/**
	 * @param x		initial x coordinate
	 * @param y		initial y coordinate
	 */
	public OldBlob(double x, double y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * @param x		initial x coordinate
	 * @param y		initial y coordinate
	 * @param r		initial radius
	 */
	public OldBlob(double x, double y, double r) {
		this.x = x;
		this.y = y;
		this.r = r;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getR() {
		return r;
	}

	public void setR(double r) {
		this.r = r;
	}

	/**
	 * Sets the velocity.
	 * @param dx	new dx
	 * @param dy	new dy
	 */
	public void setVelocity(double dx, double dy) {
		this.dx = dx;
		this.dy = dy;
	}
	
	/**
	 * Sets the direction of growth.
	 * @param dr	new dr
	 */
	public void setGrowth(double dr) {
		this.dr = dr;
	}
	
	/**
	 * Updates the blob, moving & growing.
	 */
	public void step() {
		x += dx;
		y += dy;
		r += dr;
	}

	/**
	 * Tests whether the point is inside the blob.
	 * @param x2
	 * @param y2
	 * @return		is (x2,y2) inside the blob?
	 */
	public boolean contains(double x2, double y2) {
		double dx = x-x2;
		double dy = y-y2;
		return dx*dx + dy*dy <= r*r;
	}

	/**
	 * Draws the blob on the graphics. Called by jwt every time repaint is called.
	 * @param g
	 */
	public void draw(Graphics g) {
		g.fillOval((int)(x-r), (int)(y-r), (int)(2*r), (int)(2*r));
	}
}
