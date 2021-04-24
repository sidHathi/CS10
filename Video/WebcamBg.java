import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * Fun with the webcam, built on JavaCV
 * Replaces background (as denoted by mouse press) with scenery
 * 
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2012
 * @author CBK, Winter 2014, rewritten for Webcam class
 * @author Tim Pierson, Spring 2019, added usage comments and small refactoring
 * 
 * Usage: after camera image appears, click mouse with only the background visible (e.g., you are not visible),
 * this will set the background to be removed, then move into the frame.  The background should be replaced with
 * Baker tower and the previous background subtracted from the current image.
 */
public class WebcamBg extends Webcam {
	private static final int backgroundDiff=250;	// setup: threshold for considering a pixel to be background

	private BufferedImage background;		// the stored background grabbed from the webcam
	private BufferedImage scenery;			// the replacement background
	
	public WebcamBg(BufferedImage scenery) {
		this.scenery = scenery;
	}

	/**
	 * DrawingGUI method, here setting background as a copy of the current image.
	 */
	@Override
	public void handleMousePress(int x, int y) {
		if (image != null) {
			background = new BufferedImage(image.getColorModel(), image.copyData(null), image.getColorModel().isAlphaPremultiplied(), null);
			System.out.println("background set");
		}
	}

	/**
	 * Webcam method, here subtracting background.
	 * Updates image so that pixels that look like background are replaced with scenery.
	 */
	@Override
	public void processImage() {
		if (background != null) {
			// Nested loop over every pixel
			for (int y = 0; y < Math.min(image.getHeight(), scenery.getHeight()); y++) {
				for (int x = 0; x < Math.min(image.getWidth(), scenery.getWidth()); x++) {
					// Euclidean distance squared between colors
					Color c1 = new Color(image.getRGB(x,y));
					Color c2 = new Color(background.getRGB(x,y));
					int d = (c1.getRed() - c2.getRed()) * (c1.getRed() - c2.getRed())
							+ (c1.getGreen() - c2.getGreen()) * (c1.getGreen() - c2.getGreen())
							+ (c1.getBlue() - c2.getBlue()) * (c1.getBlue() - c2.getBlue());
					if (d < backgroundDiff) {
						// Close enough to background, so replace
						image.setRGB(x,y,scenery.getRGB(x,y));
					}
				}
			}
		}
	}

	public static void main(String[] args) { 
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				// Read an image. For best effect, use something same size as webcam (accounting for scale)
				BufferedImage image = loadImage("pictures/baker-640-480.jpg");
				new WebcamBg(image);
			}
		});
	}
}
