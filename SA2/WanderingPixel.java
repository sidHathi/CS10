import java.awt.*;

/**
 * A blob that moves randomly and has a color
 */
public class WanderingPixel extends Wanderer {
	private Color color;
	
	public WanderingPixel(double x, double y, double r, Color c) {
		super(x, y, r);
		this.color = c;
	}
	
	@Override
	public void draw(Graphics g) {
		g.setColor(color);
		g.fillOval((int)(x-r), (int)(y-r), (int)(2*r), (int)(2*r));
	}

	// CHANGE: Added new getter method.
	public Color getColor(){
		return this.color;
	}
}
