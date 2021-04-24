import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.*;

/**
 * Webcam-based drawing
 * Scaffold for PS-1, Dartmouth CS 10, Fall 2016
 *
 * @author Chris Bailey-Kellogg, Spring 2015 (based on a different webcam app from previous terms)
 */
public class CamPaintEC extends Webcam {
    private char displayMode = 'w';			// what to display: 'w': live webcam, 'r': recolored image, 'p': painting
    private RegionFinderEC finder;			// handles the finding
    private Color targetColor;          	// color of regions of interest (set by mouse press)
    private Color paintColor = Color.blue;	// the color to put into the painting from the "brush"
    private BufferedImage painting;			// the resulting masterpiece
    private boolean paused = false;         // is the painting paused


    /**
     * Initializes the region finder and the drawing
     */
    public CamPaintEC() {
        finder = new RegionFinderEC();
        clearPainting();
    }

    /**
     * Resets the painting to a blank image
     */
    protected void clearPainting() {
        painting = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
    }

    /**
     * DrawingGUI method, here drawing one of live webcam, recolored image, or painting,
     * depending on display variable ('w', 'r', or 'p')
     */
    @Override
    public void draw(Graphics g) {
        // TODO: YOUR CODE HERE
        super.draw(g);
        // draws if not paused
        if (!paused) {
            if (displayMode == 'w' && targetColor != null) {
                // Draws image from webcam and overlays painting
                g.drawImage(image, 0, 0, null);
                g.drawImage(painting, 0, 0, null);
            } else if (displayMode == 'p') {
                // Clears canvas and replaces with white background
                g.setColor(Color.white);
                g.clearRect(0, 0, image.getWidth(), image.getHeight());
                g.drawRect(0, 0, image.getWidth(), image.getHeight());
                // Overlays painting onto white background
                g.drawImage(painting, 0, 0, null);
                repaint();
            } else if (displayMode == 'r' && targetColor != null) {
                // Asks RegionFinder to mark up regions of targetColor
                finder.setImage(image);
                finder.findRegions(targetColor);
                finder.recolorImage();
                // Draws recolored image
                g.drawImage(finder.getRecoloredImage(), 0, 0, null);
                repaint();
            }
        }

    }

    /**
     * Webcam method, here finding regions and updating painting.
     */
    @Override
    public void processImage() {
        // TODO: YOUR CODE HERE
        // When the webcam mode is active, find the largest region matching the target color,
        // and mark it up in the painting so that it can be overlaid in draw
        if (image != null && targetColor != null && displayMode == 'w'){
            finder.setImage(image);
            finder.findRegions(targetColor);
            ArrayList<Point> largestRegion = finder.largestRegion();
            if (largestRegion != null && largestRegion.size() > 0){
                for (int i = 0; i<largestRegion.size(); i++){
                    painting.setRGB(largestRegion.get(i).x, largestRegion.get(i).y, paintColor.getRGB());
                }
            }
        }
        else{
            repaint();
        }
    }

    /**
     * Overrides the DrawingGUI method to set targetColor.
     */
    @Override
    public void handleMousePress(int x, int y) {

        if (image != null) { // to be safe, make sure webcam is grabbing an image
            // TODO: YOUR CODE HERE
            // Sets target color to color at pixel
            targetColor = new Color(image.getRGB(x, y));
            System.out.println(targetColor.getRGB());
        }

    }

    /**
     * DrawingGUI method, here doing various drawing commands
     */
    @Override
    public void handleKeyPress(char k) {
        if (k == 'p' || k == 'r' || k == 'w') { // display: painting, recolored image, or webcam
            displayMode = k;
        }
        else if (k == 'c') { // clear
            clearPainting();
        }
        else if (k == 'o') { // save the recolored image
            saveImage(finder.getRecoloredImage(), "pictures/recolored.png", "png");
        }
        else if (k == 's') { // save the painting
            saveImage(painting, "pictures/painting.png", "png");
        }
        else if (k == 'q') { // pauses and restarts painting
            this.paused = !this.paused;
        }
        else {
            System.out.println("unexpected key "+k);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new CamPaintEC();
            }
        });
    }
}
