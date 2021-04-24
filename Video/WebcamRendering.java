import java.awt.Color;
import java.awt.Graphics;

import javax.swing.SwingUtilities;

/**
 * Fun with the webcam, built on JavaCV
 * Uses one of our de-novo rendering methods to display webcam image
 * 
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2012
 * @author CBK, Winter 2014, rewritten for simplified Webcam class
 * @author CBK, Spring 2016, direct rendering, size control
 */
public class WebcamRendering extends Webcam {
    private int pixelSize = 10;        // size of the objects representing the image
    private char style = 'i';
    
    /**
	 * Renders the image as a set of rectangles tiling the window.
	 */
	public void mosaic(Graphics g) {
		// Usual loops, but step by "pixel" size and draw a rectangle of the appropriate color.
		// Also note <=, to get that last rectangle.
		// Nested loop over every pixel
		for (int y = 0; y <= image.getHeight() - pixelSize; y += pixelSize) {
			for (int x = 0; x <= image.getWidth() - pixelSize; x += pixelSize) {
				g.setColor(new Color(image.getRGB(x,y)));
				g.fillRect(x, y, pixelSize, pixelSize);										
				g.setColor(Color.black);
				g.drawRect(x, y, pixelSize, pixelSize);
			}
		}
	}

	/**
	 * Renders the image as a set of ellipses at random positions.
	 */
	public void pointillism(Graphics g) {
		// Draw some random number of points determined by the image and "pixel" sizes.
		int numPoints = image.getWidth() * image.getHeight() * 5 / pixelSize;
		for (int p=0; p<numPoints; p++) {
			// Pick a random position and size
			int x = (int) (Math.random() * image.getWidth());
			int y = (int) (Math.random() * image.getHeight());
			int s = (int) (Math.random() * pixelSize) + 1;

			// Draw an ellipse there, colored by the pixel's color
			g.setColor(new Color(image.getRGB(x,y)));
			g.fillOval(x, y, s, s);										
		}
	}

	/*
	 * DrawingGUI method, here just remembering the style for rendering
	 */
	@Override
	public void handleKeyPress(char key) {
		if (key=='+') {
			pixelSize++;
		}
		else if (key=='-') {
			if (pixelSize>0) pixelSize--;
		}
		else {
			style = key;			
		}
	}
	
	@Override
	public void draw(Graphics g) {
		if (image!=null) {
			if (style=='m') mosaic(g);
			else if (style=='p') pointillism(g);
			else super.draw(g);
		}
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new WebcamRendering();
			}
		});
	}
}
