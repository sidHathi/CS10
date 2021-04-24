import java.awt.*;
import java.awt.image.*;
import java.lang.reflect.Array;
import java.util.*;

/**
 * Region growing algorithm: finds and holds regions in an image.
 * Each region is a list of contiguous points with colors similar to a target color.
 * Scaffold for PS-1, Dartmouth CS 10, Fall 2016
 *
 * @author Chris Bailey-Kellogg, Winter 2014 (based on a very different structure from Fall 2012)
 * @author Travis W. Peters, Dartmouth CS 10, Updated Winter 2015
 * @author CBK, Spring 2015, updated for CamPaint
 */
public class RegionFinderEC {
    private static final int maxColorDiff = 100;				// how similar a pixel color must be to the target color, to belong to a region
    // suitable value for maxColorDiff depends on your implementation of colorMatch() and how much difference in color you want to allow
    private static final int minRegion = 50; 				// how many points in a region to be worth considering

    private BufferedImage image;                            // the image in which to find regions
    private BufferedImage recoloredImage;                   // the image with identified regions recolored

    private ArrayList<ArrayList<Point>> regions;			// a region is a list of points
    // so the identified regions are in a list of lists of points

    public RegionFinderEC() {
        this.image = null;
    }

    public RegionFinderEC(BufferedImage image) {
        this.image = image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public BufferedImage getImage() {
        return image;
    }

    public BufferedImage getRecoloredImage() {
        return recoloredImage;
    }

    // Instance variable that stores the last-used brush;
    private ArrayList<Point> prevBrush;

    // Instance variable that store the position of the brush during the last frame
    public Point prevBrushPosition;

    // instance variable that stores the brush's velocity
    public Point prevBrushVelocity;

    // Instance variable that store the standard deviation of the points int the last brush
    public double prevShapeCost;


    /**
     * Loops through regions to ensure that they roughly match the size of the previous brush.
     * Removes those that diverge too much
     */
    public void cullRegionsBySize(){
        // This variable controls the acceptable divergence between object sizes;
        double sizeTolerance = image.getWidth()/5;
        if (regions.size() > 0) {
            if (prevBrush != null) {
                // Removes all regions whose size exceeds sizeTolerance
                for (int i = 0; i < this.regions.size(); i++) {
                    if (Math.abs(prevBrush.size() - regions.get(i).size()) > sizeTolerance) {
                        regions.remove(i);
                    }
                }
            }
        }
    }

    /**
     * Computes a region's position
     * @param region
     * @return avg position of points in the region
     */
    public Point computeRegionPosition(ArrayList<Point> region){
        double avgX = 0;
        double avgY = 0;
        for (int i = 0; i < region.size(); i++){
            avgX += region.get(i).x;
            avgY += region.get(i).y;
        }
        avgX /= region.size();
        avgY /= region.size();

        Point point = new Point((int)avgX, (int)avgY);
        return point;
    }

    /**
     * Computes the trajectory of the brush based on its previous and current positions.
     * stores in instance variable
     * @param currentBrush currently used brush
     */
    public void computeBrushVelocity(ArrayList<Point> currentBrush){
        if (prevBrushPosition == null){
            prevBrushPosition = computeRegionPosition(currentBrush);
        }
        else {
            int dx = computeRegionPosition(currentBrush).x - prevBrushPosition.x;
            int dy = computeRegionPosition(currentBrush).y - prevBrushPosition.y;
            prevBrushVelocity = new Point(dx, dy);
        }
    }

    /**
     * Computes the expected position of the brush based on its velocity and prevoius position.
     * Culls regions whose positions are too divergent.
     */
    public void cullRegionsByTrajectory(){
        // Maximum divergence of velocity from previous frame
        double velocityTolerance = image.getWidth()/2;
        if (regions.size() > 0){
            if (prevBrushVelocity != null){
                for (int i = 0; i < this.regions.size(); i++){
                    int expectedX = prevBrushVelocity.x + prevBrushPosition.x;
                    int expectedY = prevBrushVelocity.y + prevBrushPosition.y;

                    Point regionPosition = computeRegionPosition(regions.get(i));
                    double distance = Math.pow((expectedX-regionPosition.x), 2) + Math.pow((expectedY-regionPosition.y), 2);
                    if (distance > velocityTolerance){
                        regions.remove(i);
                    }
                }
            }
        }
    }

    /**
     * A good way to measure if the brush is of the right shape is to compare
     * the standard deviation of the points in the brush to that of the previous brush.
     * This function computes the standard deviation of the points in a regions
     * @param region
     * @return standard deviation
     */
    public double computeShapeCost(ArrayList<Point> region){

        double meanX = 0;
        double meanY = 0;

        for (int i = 0; i < region.size(); i++){
            meanX += region.get(i).x;
            meanY += region.get(i).y;
        }
        meanX /= region.size();
        meanY /= region.size();

        double deviationX = 0;
        double deviationY = 0;

        for (int i = 0; i < region.size(); i++){
            deviationX += Math.abs(region.get(i).x - meanX);
            deviationY += Math.abs(region.get(i).y - meanY);
        }

        double standardDeviationX = Math.pow(deviationX/region.size(), 1/2);
        double standardDeviationY = Math.pow(deviationY/region.size(), 1/2);

        return standardDeviationX + standardDeviationY;

    }

    /**
     * Measures how similar the shape of the current brush is to that of the previous ones.
     * Culls those which are too divergent.
     */
    public void cullRegionsByShape(){

        double deviationTolerance = image.getWidth()/2;

        if (regions.size() > 0){
            if (prevShapeCost != 0){
                for (int i = 0; i < regions.size(); i++) {
                    if (Math.abs(computeShapeCost(regions.get(i)) - prevShapeCost) > deviationTolerance) {
                        regions.remove(i);
                    }
                }
            }
        }
    }


    /**
     * Creates a black BufferedImage of the same size as this.image
     * @return	Black BufferedImage
     */
    public BufferedImage blackImage(){

        BufferedImage duplicateImage = new BufferedImage(image.getColorModel(),image.copyData(null), image.getColorModel().isAlphaPremultiplied(),null);

        for (int y = 0; y < duplicateImage.getHeight(); y++){
            for (int x = 0; x < duplicateImage.getWidth(); x++){
                duplicateImage.setRGB(x, y, 0);
            }
        }
        return duplicateImage;
    }

    /**
     * Returns a point's neighbors
     * @param center The point whose neighbor's are being returned
     * @param width The width of the image
     * @param height The height of the image
     * @return Closest points to center inside image
     */
    public ArrayList<Point> getNeighbors(Point center, int width, int height){
        ArrayList<Point> neighbors = new ArrayList<Point>();

        int x = center.x;
        int y = center.y;

        // Loops through the 9 neighbors in a 3x3 square around Point center
        // excluding the point itself and anything outside the image
        for (int nY = Math.max(0, y-1); nY <= Math.min(y+1, height-1); nY++){
            for (int nX = Math.max(0, x-1); nX <= Math.min(x+1, width-1); nX++){
                if (!(nX == x && nY == y)){
                    neighbors.add(new Point(nX, nY));
                }
            }
        }

        return neighbors;
    }

    /**
     * Checks whether a color is black
     * @param color The color
     * @return Whether it is black
     */
    public boolean isBlack(Color color){
        if (color.getRed() == 0 && color.getGreen() == 0 && color.getBlue() == 0){
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * Sets regions to the flood fill regions in the image, similar enough to the targetColor.
     */
    public void findRegions(Color targetColor) {
        // TODO: YOUR CODE HERE
        // Initialize regions ArrayList
        regions = new ArrayList<ArrayList<Point>>();
        // Initialize image that stores visited pixels - when the pixel's aren't black, they've been visited
        BufferedImage visited = blackImage();
        // Loop over every pixel
        for (int y = 0; y < image.getHeight(); y++){
            for (int x = 0; x < image.getWidth(); x++){
                // Check color
                if (colorMatch(new Color(image.getRGB(x, y)), targetColor)){
                    // Initialize list to store points in region
                    ArrayList<Point> region = new ArrayList<Point>();
                    // Initialize list to store points to visit
                    ArrayList<Point> toVisit = new ArrayList<Point>();
                    // Add first point to toVisit
                    toVisit.add(new Point(x, y));
                    while (toVisit.size() > 0){
                        // Get first point in toVisit
                        Point firstPointInList = toVisit.get(0);
                        // Check that the points in toVisit haven't already been visited
                        if (isBlack(new Color(visited.getRGB(firstPointInList.x, firstPointInList.y)))){
                            // Add point to region
                            region.add(firstPointInList);
                            // Get point's neighbors
                            ArrayList<Point> neighbors = getNeighbors(firstPointInList, image.getWidth(), image.getHeight());

                            // Loop through neighbors, adding to toVisit if correct collor
                            for (int i = 0; i < neighbors.size(); i++){
                                Point neighbor = neighbors.get(i);
                                if (colorMatch((new Color(image.getRGB(neighbor.x, neighbor.y))), targetColor)){
                                    toVisit.add(neighbor);
                                }
                            }

                            // mark point as visited
                            visited.setRGB(firstPointInList.x, firstPointInList.y, 1);
                            toVisit.remove(0);
                        }
                        else{
                            // Remove repeats
                            toVisit.remove(0);
                        }
                    }
                    //System.out.println(regions.size());
                    if (region.size() >= minRegion){
                        regions.add(region);
                    }
                }
            }
        }
    }

    /**
     * Tests whether the two colors are "similar enough" (your definition, subject to the maxColorDiff threshold, which you can vary)
     */
    private static boolean colorMatch(Color c1, Color c2) {
        // TODO: YOUR CODE HERE
        if (c1 != null && c2 != null) {
            // Compute euclidean distance between the colors' rgb values
            double euclideanColorDistance = Math.pow((c1.getRed() - c1.getRed()), 2) +
                    Math.pow((c1.getBlue() - c2.getBlue()), 2) + Math.pow((c1.getGreen() - c2.getGreen()), 2);
            // return true if the distance is small enough
            if (euclideanColorDistance < maxColorDiff) {
                return true;
            } else {
                return false;
            }
        }
        else{
            return false;
        }
    }

    /**
     * Returns the largest region detected (if any region has been detected)
     */
    public ArrayList<Point> largestRegion() {
        // TODO: YOUR CODE HERE
        cullRegionsBySize();
        cullRegionsByShape();
        cullRegionsByTrajectory();
        if (regions.size() > 0) {

            // Loop through regions, keeping track of largest region and its index
            int largestRegionSize = 0;
            int largestRegionIndex = 0;
            for (int i = 0; i < regions.size(); i++) {
                if (regions.get(i).size() > largestRegionSize) {
                    largestRegionSize = regions.get(i).size();
                    largestRegionIndex = i;
                }
            }
            computeBrushVelocity(regions.get(largestRegionIndex));
            prevBrush = regions.get(largestRegionIndex);
            return regions.get(largestRegionIndex);
        }
        else{
            return null;
        }
    }

    /**
     * Sets recoloredImage to be a copy of image,
     * but with each region a uniform random color,
     * so we can see where they are
     */
    public void recolorImage() {
        // First copy the original
        recoloredImage = new BufferedImage(image.getColorModel(),image.copyData(null), image.getColorModel().isAlphaPremultiplied(),null);
        // Now recolor the regions in it
        // TODO: YOUR CODE HERE
        Color randColor = new Color((float)Math.random(), (float)Math.random(), (float)Math.random());
        if (regions.size() > 0) {
            for (int region = 0; region < regions.size(); region++) {
                for (int i = 0; i < regions.get(region).size(); i++) {
                    recoloredImage.setRGB(regions.get(region).get(i).x, regions.get(region).get(i).y, randColor.getRGB());
                }
            }
        }
    }

}
