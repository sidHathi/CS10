import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.*;

/**
 * Simple illustration of GUI to display an image
 * 
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Spring 2016
 * @author Tim Pierson, Dartmouth CS10, Fall 2019, added explicit repaint to constructor
 */
public class SmileGUI extends DrawingGUI {
	private BufferedImage img;
	
	public SmileGUI(BufferedImage img) {
		super("Smile!", img.getWidth(), img.getHeight());
		this.img = img;
		repaint();
	}

	/**
	 * DrawingGUI method, here showing the image
	 */
	@Override
	public void draw(Graphics g) {
		g.drawImage(img, 0, 0, null);
	}

	public static void main(String[] args) { 
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				// Load image into memory from disk
				BufferedImage img = loadImage("pictures/smiley.png");

				// Create a GUI to display it
				new SmileGUI(img);
			}
		});
	}
}
