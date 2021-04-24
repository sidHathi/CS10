import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;

/**
 * Animated blobs.  Like BlobGUI, but now with multiple blobs
 * 
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Spring 2016, based on animated agents from previous terms
 */
public class BlobsGUI extends DrawingGUI {
	private static final int width=800, height=600;	// size of the world

	private ArrayList<Blob> blobs;					// list of all the blobs to handle
	private char blobType = '0';						// what type of blob to create
	private int delay = 100;							// for the timer
	
	public BlobsGUI() {
		super("Animated Blobs", width, height);
		
		// Initialize empty list of blobs. What happens if we forget to do this?
		// (You will run into that situation in the future, I guarantee.)
		blobs = new ArrayList<Blob>();
		
		// Timer drives the animation.
		startTimer();
	}
	
	/**
	 * DrawingGUI method, here either detecting which blob was clicked,
	 * or creating a new blob.
	 */
	@Override
	public void handleMousePress(int x, int y) {
		// Check if hit a blob
		for (Blob blob : blobs) {
			if (blob.contains(x, y)) {
				System.out.println("back off!");
				return;
			}
		}
		
		// Create a new blob
		if (blobType=='0') {
			blobs.add(new Blob(x,y));
		}
		else if (blobType=='b') {
			blobs.add(new Bouncer(x,y,width,height));
		}
		else if (blobType=='p') {
			blobs.add(new Pulsar(x,y));
		}
		else if (blobType=='w') {
			blobs.add(new Wanderer(x,y));
		}
		else if (blobType=='u') {
			blobs.add(new WanderingPulsar(x,y));
		}
		else {
			System.err.println("Unknown blob type "+blobType);
		}
		
		// Redraw with added blob
		repaint();
	}

	/**
	 * DrawingGUI method, here just remembering the type of blob to create
	 */
	@Override
	public void handleKeyPress(char k) {
		System.out.println("Handling key '"+k+"'");
		if (k == 'f') { // faster
			if (delay>1) delay /= 2;
			setTimerDelay(delay);
			System.out.println("delay:"+delay);
		}
		else if (k == 's') { // slower
			delay *= 2;
			setTimerDelay(delay);
			System.out.println("delay:"+delay);
		}
		else { // blob type
			blobType = k;			
		}
	}
	
	/**
	 * DrawingGUI method, here just drawing all the blobs
	 */
	@Override
	public void draw(Graphics g) {
		// Ask all the blobs to draw themselves.
		for (Blob blob : blobs) {
			blob.draw(g);
		}		
	}
	
	/**
	 * DrawingGUI method, here having all the blobs take a step
	 */
	@Override
	public void handleTimer() {
		// Ask all the blobs to move themselves.
		for (Blob blob : blobs) {
			blob.step();
		}
		// Now update the GUI.
		repaint();
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new BlobsGUI();
			}
		});
	}
}
