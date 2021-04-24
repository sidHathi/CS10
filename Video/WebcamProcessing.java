import javax.swing.SwingUtilities;

/**
 * Fun with the webcam, built on JavaCV
 * Just applies one of our image processing methods to the webcam image
 * 
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2012
 * @author CBK, Winter 2014, rewritten for simplified Webcam class
 * @author CBK, Spring 2015, refactored for ImageProcessor
 */
public class WebcamProcessing extends Webcam {
	private ImageProcessor proc;	// handles image processing, from last class
	
	public WebcamProcessing() {
		proc = new ImageProcessor(null);
	}
	
	/**
	 * Use proc to change colors (try something more fun yourself)
	 */
	@Override
	public void processImage() {
		proc.setImage(image); //give current image to ImageProcessor proc
		proc.scaleColor(0.5, 0.5, 1.5); //change color, emphasize blue, de-emphasize red and green
		image = proc.getImage(); //set image from proc so WebCamProcessing object has it
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new WebcamProcessing();
			}
		});
	}
}
