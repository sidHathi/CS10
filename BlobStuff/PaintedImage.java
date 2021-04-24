import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.*;

/**
 * Custom rendering of an image, by animated blobs.
 * 
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Spring 2016
 */
public class PaintedImage extends DrawingGUI {
	private static final int numBlobs = 20000;			// setup: how many blobs
	private static final int numToMove = 5000;			// setup: how many blobs to animate each frame

	private BufferedImage original;						// the picture to paint
	private BufferedImage result;						// the picture being painted
	private ArrayList<Blob> blobs;						// the blobs representing the picture
	
	public PaintedImage(BufferedImage original) {
		super("Animated Picture", original.getWidth(), original.getHeight());
		
		this.original = original;
		result = new BufferedImage(original.getWidth(), original.getHeight(), BufferedImage.TYPE_INT_ARGB);
		
		// Create a bunch of random blobs.
		blobs = new ArrayList<Blob>();
		for (int i=0; i<numBlobs; i++) {
			int x = (int)(original.getWidth()*Math.random());
			int y = (int)(original.getHeight()*Math.random());
			blobs.add(new Wanderer(x, y, 1));
		}

		// Timer drives the animation.
		startTimer();
	}

	/**
	 * DrawingGUI method, here just drawing all the blobs
	 */
	@Override
	public void draw(Graphics g) {
		g.drawImage(result, 0, 0, null);
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
			// Pick a random blob, leave a trail where it is, and ask it to move.
			Blob blob = blobs.get((int)(Math.random()*blobs.size()));
			int x = (int)blob.getX(), y = (int)blob.getY();
			// Careful to stay within the image
			if (x>=0 && x<width && y>=0 && y<height) {
				result.setRGB(x, y, original.getRGB(x, y));				
			}
			blob.step();
		}
		// Now update the drawing
		repaint();
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new PaintedImage(loadImage("pictures/baker.jpg"));
			}
		});
	}
}
