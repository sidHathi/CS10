import javax.swing.*;
import java.awt.*;
import java.awt.image.*;

/**
 * A class demonstrating manipulation of image pixels.
 * 
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2012
 * @author CBK, Winter 2014, rewritten for BufferedImage
 * @author CBK, Spring 2015, refactored to separate GUI from operations
 * @author CBK, Spring 2016, integrated mouse-based interaction
 */
public class ImageProcessingGUI extends DrawingGUI {
	private int radius = 5;				// how big the drawing effects are

	private ImageProcessor proc;		// handles the image processing
	
	private char action = 'd';			// how to interpret mouse press
	private Color pickedColor = Color.black;	// from 'g' action mouse press, starts off as black

	/**
	 * Creates the GUI for the image processor, with the window scaled to the to-process image's size
	 */
	public ImageProcessingGUI(ImageProcessor proc) {
		super("Image processing", proc.getImage().getWidth(), proc.getImage().getHeight());
		this.proc = proc;
	}

	/**
	 * DrawingGUI method, here showing the current image
	 */
	@Override
	public void draw(Graphics g) {
		g.drawImage(proc.getImage(), 0, 0, null);
	}

	/**
	 * DrawingGUI method, here dispatching on image processing operations
	 * Note that there are some magic numbers here that you can play with.
	 * While that's generally bad practice, it's just for simplicity in this example.
	 */
	@Override
	public void handleKeyPress(char op) {
		System.out.println("Handling key '"+op+"'");
		if (op=='a') { // average
			proc.average(1);
		}
		else if (op=='b') { // brighten
			proc.brighten();
		}
		else if (op=='c') { // funky color scaling
			proc.scaleColor(1.25, 1.0, 0.75);
		}
		else if (op=='d') { // dim
			proc.dim();
		}
		else if (op=='f') { // flip
			proc.flip();
		}
		else if (op=='g') { // gray
			proc.gray();
		}
		else if (op=='h') { // sharpen
			proc.sharpen(3);
		}
		else if (op=='l') { // mouse => lens
			action = op;
		}
		else if (op=='m') { // scramble
			proc.scramble(5);
		}
		else if (op=='n') { // noise
			proc.noise(20);
		}
		else if (op=='p') { // mouse => pick up the color
			action = op;
		}
		else if (op=='q') { // mouse => draw squares
			action = op;
		}
		else if (op=='s') { // save a snapshot
			saveImage(proc.getImage(), "pictures/snapshot.png", "png");
		}
		else if (op=='+') { // bigger radius
			radius++;
			System.out.println("radius:"+radius);
		}
		else if (op=='-') { // smaller radius
			if (radius>0) radius--;
			System.out.println("radius:"+radius);
		}
		else {
			System.out.println("Unknown operation");
		}

		repaint(); // Re-draw, since image has changed
	}

	/**
	 * DrawingGUI method, here manipulating the image at/near the mouse location
	 */
	@Override
	public void handleMousePress(int x, int y) {
		if (action=='l') { // Lens effect
			proc.lens(x, y, radius);					
		}
		else if (action=='p') { // Picking color					
			pickedColor = new Color(proc.getImage().getRGB(x, y));
			System.out.println("picked color "+pickedColor);
		}
		else if (action=='q') { // Drawing squares
			proc.drawSquare(x, y, radius, pickedColor);
		}

		repaint(); // Re-draw everything, since image has changed
	}
	
	public static void main(String[] args) { 
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				// Load the image to process
				BufferedImage baker = loadImage("pictures/baker.jpg");
				// create a new processor, and a GUI to handle it
				new ImageProcessingGUI(new ImageProcessor(baker));
			}
		});
	}
}
