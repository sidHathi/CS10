import java.awt.*;
import java.awt.image.*;

import javax.swing.*;

import org.bytedeco.javacv.*;
import org.bytedeco.javacv.Frame;

import org.bytedeco.opencv.opencv_core.*;
import static org.bytedeco.opencv.global.opencv_core.*;
import static org.bytedeco.opencv.global.opencv_imgproc.*;

/**
 * Class to handle webcam capture and processing, packaging up JavaCV stuff.
 * 
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Winter 2014
 * @author Modified by Travis Peters, Dartmouth CS 10, Winter 2015
 * @author CBK, updated to JavaCV 1.1, Spring 2016
 * @author Tim Pierson, Dartmouth CS 10, Fall 2019, updated to JavaCV 1.5.1
 * @author Tim Pierson, Dartmouth CS 10, Fall 2019, added operating system check
 *
 */
public class WebcamTest extends JFrame {
	protected boolean mac = true;					// set to true for mac, false for windows
	
	protected double scale = 1.0;					// to downsize the image (for speed), set this to a fraction < 1
	protected boolean mirror = true;				// make true in order to mirror left<->right so your left hand is on the left side of the image

	protected int width, height;					// the size of the grabbed images (scaled if so specified)
	protected BufferedImage image;					// image grabbed from webcam (if any)

	private Grabby grabby;							// handles webcam grabbing
	private JComponent canvas;						// handles graphics display

	private FrameGrabber grabber;					// JavaCV

	public WebcamTest() {
		super("Webcam");

		//try to determine if this is a Mac OS (if doesn't work, manually set mac instance variable)
		String os = System.getProperty("os.name");
		if (os.contains("Mac")) {
			System.out.println("Looks like you're on a Mac");
			mac = true;
		}
		else {
			System.out.println("Looks like you're NOT on a Mac");
			mac = false;
		}
		//mac = true; //set manually if code above doesn't work

		// Create our graphics-handling component.
		canvas = new JComponent() {
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(image, 0, 0, null);
			}
		};

		try {
			if (mac) {
				// this seems to work for Mac people
				grabber = new OpenCVFrameGrabber(0);
			}
			else {
				// this seems to work for Windows people
				grabber = FrameGrabber.createDefault(0);
			}
			grabber.start();
			System.out.println("Started!");
		} catch (Exception e) {
			System.err.println("Failed to start frame grabber");
			System.err.println(e);
			System.exit(-1);
		}

		// Get size and figure out scaling
		width = grabber.getImageWidth();
		height = grabber.getImageHeight();
		System.out.println("Native camera size "+width+"*"+height);
		if (scale != 1) {
			width = (int)(width*scale);
			height = (int)(height*scale);
			System.out.println("Scaled to "+width+"*"+height);
		}

		// Set the size as determined by the grabber.
		setSize(width, height);		

		// Boilerplate to finish initializing the GUI.
		canvas.setPreferredSize(new Dimension(getWidth(), getHeight()));
		getContentPane().add(canvas);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);

		// Spawn a separate thread to handle grabbing.
		grabby = new Grabby();
		grabby.execute();
	}

	/**
	 * Handles grabbing an image from the webcam (following JavaCV examples)
	 * storing it in image, and telling the canvas to repaint itself.
	 */
	private class Grabby extends SwingWorker<Void, Void> {
		protected Void doInBackground() throws Exception {
		    OpenCVFrameConverter.ToIplImage grabberConverter = new OpenCVFrameConverter.ToIplImage();
		    Java2DFrameConverter paintConverter = new Java2DFrameConverter();
		    while (!isCancelled()) {
				IplImage grabbed = null;
				while (grabbed == null) {
					try {
						grabbed = grabberConverter.convert(grabber.grab());
					}	
					catch (Exception e) {
						System.err.println("failed grab");
						Thread.sleep(100); // wait a bit
					}
				}
				if (mirror) {
					cvFlip(grabbed, grabbed, 1);
				}
				if (scale != 1) {
					IplImage resized = IplImage.create(width, height, grabbed.depth(), grabbed.nChannels());
					cvResize(grabbed, resized);
					grabbed = resized;
				}
				Frame frame = grabberConverter.convert(grabbed);
				image = paintConverter.getBufferedImage(frame);
				canvas.repaint();
				Thread.sleep(100); // slow it down
			}
			// All done; clean up
			grabber.stop();
			grabber.release();
			grabber = null;
			return null;
		}
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new WebcamTest();
			}
		});
	}
}