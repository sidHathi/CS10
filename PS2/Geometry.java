/**
 * Geometry helper methods
 * 
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Spring 2015
 * @author CBK, Fall 2016, separated from quadtree, instrumented to count calls
 * 
 */
public class Geometry {
	private static int numInCircleTests = 0;			// keeps track of how many times pointInCircle has been called
	private static int numCircleRectangleTests = 0;		// keeps track of how many times circleIntersectsRectangle has been called
		
	public static int getNumInCircleTests() {
		return numInCircleTests;
	}

	public static void resetNumInCircleTests() {
		numInCircleTests = 0;
	}

	public static int getNumCircleRectangleTests() {
		return numCircleRectangleTests;
	}

	public static void resetNumCircleRectangleTests() {
		numCircleRectangleTests = 0;
	}

	/**
	 * Returns whether or not the point is within the circle
	 * @param px		point x coord
	 * @param py		point y coord
	 * @param cx		circle center x
	 * @param cy		circle center y
	 * @param cr		circle radius
	 */
	public static boolean pointInCircle(double px, double py, double cx, double cy, double cr) {
		numInCircleTests++;
		return (px-cx)*(px-cx) + (py-cy)*(py-cy) <= cr*cr;
	}

	/**
	 * Returns whether or not the circle intersects the rectangle
	 * Based on discussion at http://stackoverflow.com/questions/401847/circle-rectangle-collision-detection-intersection
	 * @param cx	circle center x
	 * @param cy	circle center y
	 * @param cr	circle radius
	 * @param x1 	rectangle min x
	 * @param y1  	rectangle min y
	 * @param x2  	rectangle max x
	 * @param y2  	rectangle max y
	 */
	public static boolean circleIntersectsRectangle(double cx, double cy, double cr, double x1, double y1, double x2, double y2) {
		numCircleRectangleTests++;
		double closestX = Math.min(Math.max(cx, x1), x2);
		double closestY = Math.min(Math.max(cy, y1), y2);
		return (cx-closestX)*(cx-closestX) + (cy-closestY)*(cy-closestY) <= cr*cr;
	}
}
