import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.*;

/**
 * Custom rendering of an image, by animated blobs.
 * 
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2012
 * @author CBK, Winter 2014, revised for DrawingFrame and independent Agent files
 * @author CBK, Spring 2015, DrawingGUI
 * @author CBK, Spring 2016, revised for Blobs
 */
public class AnimatedImage extends DrawingGUI {
	private static final int radius = 5;				// setup: blob size
	private static final int numBlobs = 20000;			// setup: how many blobs
	private static final int numToMove = 1000;			// setup: how many blobs to animate each frame

	private ArrayList<Blob> blobs;						// the blobs representing the picture
	
	public AnimatedImage(BufferedImage img) {
		// Size of the window is scaled up from the original image by the blob radius
		super("Animated Picture", img.getWidth()*radius, img.getHeight()*radius);
		
		// Create a bunch of random blobs.
		blobs = new ArrayList<Blob>();
		for (int i=0; i<numBlobs; i++) {
			int x = (int)(img.getWidth()*Math.random());
			int y = (int)(img.getHeight()*Math.random());
			Color color = new Color(img.getRGB(x,y));
			blobs.add(new WanderingPixel(x*radius, y*radius, (int)(1+Math.random()*radius), color));
		}

		// Timer drives the animation.
		startTimer();
	}

	/**
	 * DrawingGUI method, here just drawing all the blobs
	 */
	@Override
	public void draw(Graphics g) {
		for (Blob blob : blobs) {
			blob.draw(g);
		}		
	}

	/**
	 * DrawingGUI method, here moving some of the blobs
	 */
	@Override
	public void handleTimer() {
		for (int b = 0; b < numToMove; b++) {
			// Pick a random blob and ask it to move.
			blobs.get((int)(Math.random()*blobs.size())).step();
		}
		// Now update the drawing
		repaint();
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				// note: using downsampled version, to reduce computational expense
				new AnimatedImage(loadImage("pictures/baker-200-150.jpg"));
			}
		});
	}
}
