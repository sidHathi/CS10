import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 * A blob that moves randomly and draws itself as an image
 */
public class WanderingImage extends Wanderer {
	private BufferedImage img;
	
	public WanderingImage(double x, double y, BufferedImage img) {
		super(x, y, Math.max(img.getHeight()/2, img.getWidth()/2));
		this.img = img;
	}
	
	@Override
	public void draw(Graphics g) {
		g.drawImage(img, (int)(x-r), (int)(y-r), null);
	}
}
