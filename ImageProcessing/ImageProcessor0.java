import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * A class demonstrating manipulation of image pixels.
 * Version 0: just the core definition
 * 
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2012
 * @author CBK, Winter 2014, rewritten for BufferedImage
 * @author CBK, Spring 2015, refactored to separate GUI from operations
 */
public class ImageProcessor0 {
	private BufferedImage image;		// the current image being processed

	/**
	 * @param image		the original
	 */
	public ImageProcessor0(BufferedImage image) {
		this.image = image;
	}
	
	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

	/**
	 * Adds random noise to pixels within radius of given coordinates.
	 * (Modified copy of similar method from ImageProcessor.java
	 * @param xPos	X Position of noise
	 * @param yPos	Y Position of noise
	 * @param radius	radius of noise
	 * @param scale	max value of noise
	 */
	public void noiseAtPoint(int xPos, int yPos, int radius, double scale){

		// Nested loop over every pixel
		for (int y = (yPos-radius); y < (yPos+radius); y++) {
			for (int x = (xPos-radius); x < (xPos+radius); x++) {
				// Get current color; add noise to each channel
				Color color = new Color(image.getRGB(x, y));
				int red = (int)(ImageProcessor.constrain(color.getRed() + scale * (2*Math.random() - 1), 0, 255));
				int green = (int)(ImageProcessor.constrain(color.getGreen() + scale * (2*Math.random() - 1), 0, 255));
				int blue = (int)(ImageProcessor.constrain(color.getBlue() + scale * (2*Math.random() - 1), 0, 255));
				// Put new color
				Color newColor = new Color(red, green, blue);
				image.setRGB(x, y, newColor.getRGB());
			}
		}

	}
}
