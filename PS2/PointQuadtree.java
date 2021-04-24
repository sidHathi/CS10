import java.util.ArrayList;
import java.util.List;

/**
 * A point quadtree: stores an element at a 2D position, 
 * with children at the subdivided quadrants
 * 
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Spring 2015
 * @author CBK, Spring 2016, explicit rectangle
 * @author CBK, Fall 2016, generic with Point2D interface
 * 
 */
public class PointQuadtree<E extends Point2D> {
	private E point;							// the point anchoring this node
	private int x1, y1;							// upper-left corner of the region
	private int x2, y2;							// lower-right corner of the region
	private PointQuadtree<E> c1, c2, c3, c4;	// children

	/**
	 * Initializes a leaf quadtree, holding the point in the rectangle
	 */
	public PointQuadtree(E point, int x1, int y1, int x2, int y2) {
		this.point = point;
		this.x1 = x1; this.y1 = y1; this.x2 = x2; this.y2 = y2;
	}

	// Getters
	
	public E getPoint() {
		return point;
	}

	public int getX1() {
		return x1;
	}

	public int getY1() {
		return y1;
	}

	public int getX2() {
		return x2;
	}

	public int getY2() {
		return y2;
	}

	/**
	 * Returns the child (if any) at the given quadrant, 1-4
	 * @param quadrant	1 through 4
	 */
	public PointQuadtree<E> getChild(int quadrant) {
		if (quadrant==1) return c1;
		if (quadrant==2) return c2;
		if (quadrant==3) return c3;
		if (quadrant==4) return c4;
		return null;
	}

	/**
	 * Returns whether or not there is a child at the given quadrant, 1-4
	 * @param quadrant	1 through 4
	 */
	public boolean hasChild(int quadrant) {
		return (quadrant==1 && c1!=null) || (quadrant==2 && c2!=null) || (quadrant==3 && c3!=null) || (quadrant==4 && c4!=null);
	}

	/**
	 * Inserts the point into the tree
	 */
	public void insert(E p2) {
		// TODO: YOUR CODE HERE

		// Determines which quadrant the new point should be placed into
		boolean quad1 = p2.getX() > this.point.getX() && p2.getY() < this.point.getY();
		boolean quad2 = p2.getX() < this.point.getX() && p2.getY() < this.point.getY();
		boolean quad3 = p2.getX() < this.point.getX() && p2.getY() > this.point.getY();
		boolean quad4 = p2.getX() > this.point.getX() && p2.getY() > this.point.getY();
		boolean replace = p2.getX() == this.point.getX() && p2.getY() == this.point.getY();

		// Inserts into the appropriate quadrant, or into the appropriate child
		if (quad1){
			if (this.hasChild(1)){
				this.c1.insert(p2);
			}
			else{
				this.c1 = new PointQuadtree<E>(p2, (int)this.point.getX(), this.y1, this.x2, (int)this.point.getY());
			}
		}
		else if (quad2){
			if (this.hasChild(2)){
				this.c2.insert(p2);
			}
			else{
				this.c2 = new PointQuadtree<E>(p2, this.x1, this.y1, (int)this.point.getX(), (int)this.point.getY());
			}
		}
		else if (quad3){
			if (this.hasChild(3)){
				this.c3.insert(p2);
			}
			else{
				this.c3 = new PointQuadtree<E>(p2, this.x1, (int)this.point.getY(), (int)this.point.getX(), this.y2);
			}
		}
		else if (quad4){
			if (this.hasChild(4)){
				this.c4.insert(p2);
			}
			else{
				this.c4 = new PointQuadtree<E>(p2, (int)this.point.getX(), (int)this.point.getY(), this.x2, this.y2);
			}
		}
		else if (replace){
			// if the point is the same as the current one, replace the current one.
			this.point = p2;
		}
	}
	
	/**
	 * Finds the number of points in the quadtree (including its descendants)
	 */
	public int size() {
		// TODO: YOUR CODE HERE -- compute directly, using only numbers not lists (i.e., don't just call allPoints() and return its size)
		int c1Size, c2Size, c3Size, c4Size;		// The size of the quadtree is 1 + the sizes of its children summed up


		// Gets the sizes of each child
		if (hasChild(1)){
			c1Size = c1.size();
		}
		else{
			c1Size = 0;
		}

		if (hasChild(2)){
			c2Size = c2.size();
		}
		else{
			c2Size = 0;
		}

		if (hasChild(3)){
			c3Size = c3.size();
		}
		else{
			c3Size = 0;
		}

		if (hasChild(4)){
			c4Size = c4.size();
		}
		else{
			c4Size = 0;
		}

		return c1Size + c2Size + c3Size + c4Size + 1;
	}
	
	/**
	 * Builds a list of all the points in the quadtree (including its descendants)
	 */
	public List<E> allPoints() {
		// TODO: YOUR CODE HERE -- efficiency matters!

		// Uses helper method addPoints to add each point in the quadtree to this list
		List<E> allPointsList = new ArrayList<>();
		addPoints(allPointsList);
		return allPointsList;
	}

	/**
	 * Uses the quadtree to find all points within the circle
	 * @param cx	circle center x
	 * @param cy  	circle center y
	 * @param cr  	circle radius
	 * @return    	the points in the circle (and the qt's rectangle)
	 */
	public List<E> findInCircle(double cx, double cy, double cr) {
		// TODO: YOUR CODE HERE -- efficiency matters!

		// Uses the helper method addHits to add every point in the circle to the hits list
		List<E> hits = new ArrayList<E>();
		addHits(hits, cx, cy, cr);
		return hits;
	}

	// TODO: YOUR CODE HERE for any helper methods

	/**
	 * Adds all points in the quadtree to parameter allPointsList
	 * @param allPointsList
	 */
	public void addPoints(List<E> allPointsList){
		// Adds the point stored in this node, and all the points stored within the node's children.
		allPointsList.add(this.point);
		if (hasChild(1)){
			c1.addPoints(allPointsList);
		}
		if (hasChild(2)){
			c2.addPoints(allPointsList);
		}
		if (hasChild(3)) {
			c3.addPoints(allPointsList);
		}
		if (hasChild(4)) {
			c4.addPoints(allPointsList);
		}
	}


	/**
	 * Adds all the points within cr of (cx, cy) to hitList
	 * @param hitList
	 * @param cx
	 * @param cy
	 * @param cr
	 */
	public void addHits(List<E> hitList, double cx, double cy, double cr){
		if (Geometry.circleIntersectsRectangle(cx, cy, cr, x1, y1, x2, y2)){
			if (Geometry.pointInCircle(point.getX(), point.getY(), cx, cy ,cr)){
				hitList.add(point);
			}
			if (hasChild(1)){
				c1.addHits(hitList, cx, cy, cr);
			}
			if (hasChild(2)){
				c2.addHits(hitList, cx, cy, cr);
			}
			if (hasChild(3)){
				c3.addHits(hitList, cx, cy, cr);
			}
			if (hasChild(4)){
				c4.addHits(hitList, cx, cy, cr);
			}
		}
	}

	// EXTRA CREDIT FUNCTIONS:

	/**
	 * Finds all points searched when executing findInCircle functionality
	 * @param cx
	 * @param cy
	 * @param cr
	 * @return
	 */
	public List<E> returnSearchPoints(double cx, double cy, double cr){

		List<E> searchPoints = new ArrayList<E>();
		getSearchedPoints(searchPoints, cx, cy, cr);
		return searchPoints;
	}

	/**
	 * Helper function for returnSearchPoints
	 * @param searchPoints
	 * @param cx
	 * @param cy
	 * @param cr
	 */
	public void getSearchedPoints(List<E> searchPoints, double cx, double cy, double cr){

		if (Geometry.circleIntersectsRectangle(cx, cy, cr, x1, y1, x2, y2)){
			searchPoints.add(point);
			if (hasChild(1)){
				c1.getSearchedPoints(searchPoints, cx, cy, cr);
			}
			if (hasChild(2)){
				c2.getSearchedPoints(searchPoints, cx, cy, cr);
			}
			if (hasChild(3)){
				c3.getSearchedPoints(searchPoints, cx, cy, cr);
			}
			if (hasChild(4)){
				c4.getSearchedPoints(searchPoints, cx, cy, cr);
			}
		}
	}
}
