import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * A simple JFrame containing a single component for drawing (via the draw method)
 * 
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Winter 2014
 * @author CBK, Spring 2015, revised to factor out the image, provide simple event handling
 */
public class DrawingGUI extends JFrame {
	public JComponent canvas;						// handles graphics display
	protected int width, height;					// the size of the drawing window
	protected Timer timer;							// one delay-driven event
	private static final int delay = 100;			// default delay for the timer (milliseconds)

	/**
	 * Creates an empty, title-less frame.
	 * Call initWindow to finish it off.
	 */
	public DrawingGUI() {
		super("");
	}

	/**
	 * Creates an empty frame.
	 * Call initWindow to finish it off.
	 */
	public DrawingGUI(String title) {
		super(title);
	}

	/**
	 * Creates a frame and finishes initializing it.
	 * 
	 * @param title		displayed in window title bar
	 * @param width		window size
	 * @param height	window size
	 */
	public DrawingGUI(String title, int width, int height) {
		super(title);
		initWindow(width, height);
	}

	/**
	 * Finishes initializing the GUI for the fixed size.
	 * 
	 * @param width		window size
	 * @param height	window size
	 */
	protected void initWindow(int width, int height) {
		this.width = width;
		this.height = height;

		// Create a canvas for drawing into, paintComponent() is called on repaint, add our object's draw function
		canvas = new JComponent() {
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				draw(g);  //our object's drawing method call
			}
		};		

		// Listen for events
		canvas.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent event) {
				handleMousePress(event.getPoint().x, event.getPoint().y);
			}
		});
		canvas.addMouseMotionListener(new MouseAdapter() {
			public void mouseMoved(MouseEvent event) {
				handleMouseMotion(event.getPoint().x, event.getPoint().y);
			}
		});
		addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent event) {
				handleKeyPress(event.getKeyChar());
			}
		});
		timer = new Timer(delay, new AbstractAction("update") {
			public void actionPerformed(ActionEvent e) {
				handleTimer();
			}
		});

		// Boilerplate to finish initializing the GUI to the specified size
		setSize(width, height);		
		canvas.setPreferredSize(new Dimension(width, height));
		getContentPane().add(canvas);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}

	/**
	 * Start the timer running.
	 */
	public void startTimer() {
		timer.start();		
	}

	/**
	 * Stops the timer.
	 */
	public void stopTimer() {
		timer.stop();		
	}

	/**
	 * Set the delay of the timer.
	 * @param delay
	 */
	public void setTimerDelay(int delay) {
		timer.setDelay(delay);
	}

	/**
	 * Method to draw in the canvas, to be overridden by subclasses.
	 * @param g
	 */
	public void draw(Graphics g) {
	}

	/**
	 * Method to handle a mouse press, to be overridden by subclasses.
	 * @param x		x coordinate of mouse press
	 * @param y		y coordinate of mouse press
	 */
	public void handleMousePress(int x, int y) {
	}

	/**
	 * Method to handle mouse motion, to be overridden by subclasses.
	 * @param x		x coordinate of mouse
	 * @param y		y coordinate of mouse
	 */
	public void handleMouseMotion(int x, int y) {
	}

	/**
	 * Method to handle a key press, to be overridden by subclasses.
	 * @param key	the key that was pressed
	 */
	public void handleKeyPress(char key) {
	}

	/**
	 * Method to respond to the timer going off, to be overridden by subclasses.
	 */
	public void handleTimer() {
	}

	/**
	 * 
	 * @param filename	for the image
	 */
	public static BufferedImage loadImage(String filename) {
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File(filename));
		}
		catch (Exception e) {
			System.err.println("Couldn't load image from `"+filename+"' -- make sure the file exists in that folder");
			System.exit(-1);
		}
		return image;
	}

	public void saveImage(BufferedImage image, String filename, String format) {
		try {
			ImageIO.write(image, format, new File(filename));
			System.out.println("Saved a snapshot in "+filename);
		}
		catch (Exception e) {
			System.err.println("Couldn't save snapshot in `"+filename+"' -- make sure the folder exists");
		}		
	}
}
