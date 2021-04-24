/**
 * Geometry helper methods (extra credit)
 * 
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Spring 2015
 * @author CBK, Fall 2016, separated from quadtree, instrumented to count calls
 * 
 */
public class GeometryEC extends Geometry {

	/**
	 * Extra Credit Method determins whether point is inside some rectangle
	 * @param px	point x
	 * @param py	point y
	 * @param rx1	point top left x
	 * @param ry1	point top left y
	 * @param rx2	point bottom right x
	 * @param ry2	point bottom right y
	 * @return
	 */
	public static boolean pointInRectangle(double px, double py, double rx1, double ry1, double rx2, double ry2){
		return (px > rx1 && px < rx2) && (py > ry1 && py < ry2);
	}

}
