import java.awt.*;
import javax.swing.*;

/**
 * Animated blob.
 * Version 0: core code, just creating a blob and animating it moving in a fixed direction.
 * 
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Spring 2016, based on animated agents from previous terms
 */
public class BlobGUI0 extends DrawingGUI {
	private static final int width=800, height=600;	// size of the world

	private Blob blob;								// the blob
	
	public BlobGUI0() {
		super("Animated Blob", width, height);
	
		// Create blob at the center.
		blob = new Blob(width/2, height/2);  // What happens if this isn't here? Try it so you see the symptom.
		blob.setVelocity(0.5, 0.5); // small steps
		
		// Timer drives the animation.
		startTimer(); //ticks every 100ms by default
	}
	
	/**
	 * DrawingGUI method, here just drawing the blob
	 */
	@Override
	public void draw(Graphics g) {
		// Ask the blob to draw itself.
		blob.draw(g);
	}
	
	/**
	 * DrawingGUI method, here having the blob take a step
	 */
	@Override
	public void handleTimer() {
		// Ask the blob to move itself.
		blob.step();
		// Now update the GUI.
		repaint(); //canvas set up in DrawGUI.java initWindow calls draw everytime repaint called
	}

	/**
	 * DrawingGUI method, here either detecting if the blob was clicked or else moving it.
	 */
	@Override
	public void handleMousePress(int x, int y) {
		if (blob.contains(x, y)) {
			// Clicked on it
			System.out.println("back off!");
		}
		else {
			// Moved it
			blob.setX(x);
			blob.setY(y);
			// Redraw with moved blob
			repaint();
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new BlobGUI0();
			}
		});
	}
}
