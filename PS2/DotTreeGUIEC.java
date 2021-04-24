import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DotTreeGUIEC extends DotTreeGUI{

    /**
     * Extra credit class. Same as DotTreeGUI, but it uses extra methods in PointQuadTree
     * to mark every node searched in grey, even it isn't the node being clicked on.
     * The actual search results are still marked in black.
     */

    private static final int width=800, height=600;		// size of the universe
    private static final int dotRadius = 5;				// to draw dot, so it's visible
    private static final Color[] rainbow = {Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.BLUE, Color.MAGENTA};
    // to color different levels differently

    private PointQuadtree<Dot> tree = null;			// holds the dots
    private char mode = 'a';						// 'a': adding; 'q': querying with the mouse
    private int mouseX, mouseY;						// current mouse location, when querying
    private int mouseRadius = 10;					// circle around mouse location, for querying
    private boolean trackMouse = false;				// if true, then print out where the mouse is as it moves
    private List<Dot> found = null;					// who was found near mouse, when querying

    private List<Dot> searched = null; // additional instance List of searched nodes

    public DotTreeGUIEC() {
        super();
    }

    /**
     * DrawingGUI method, here keeping track of the location and redrawing to show it
     */
    @Override
    public void handleMouseMotion(int x, int y) {
        if (mode == 'q') {
            mouseX = x; mouseY = y;
            repaint();
        }
        if (trackMouse) {
            System.out.println("@ ("+x+","+y+")");
        }
    }

    /**
     * DrawingGUI method, here toggling the mode between 'a' and 'q'
     * and increasing/decresing mouseRadius via +/-
     */
    @Override
    public void handleKeyPress(char key) {
        if (key=='a' || key=='q') mode = key;
        else if (key=='+') {
            mouseRadius += 10;
        }
        else if (key=='-') {
            mouseRadius -= 10;
            if (mouseRadius < 0) mouseRadius=0;
        }
        else if (key=='m') {
            trackMouse = !trackMouse;
        }

        repaint();
    }

    /**
     * DrawingGUI method, here either adding a new point or querying near the mouse
     */
    @Override
    public void handleMousePress(int x, int y) {
        if (mode == 'a') {
            // Add a new dot at the point
            // TODO: YOUR CODE HERE

            // Adds dot if tree exists, initializes new tree using dot if tree is null
            if (tree != null) {
                tree.insert(new Dot(x, y));
            }
            else{
                tree = new PointQuadtree<Dot>(new Dot(x, y), 0, 0, width, height);
            }
        }
        else if (mode == 'q') {
            // Set "found" to what tree says is near the mouse press
            // TODO: YOUR CODE HERE

            // Initializes found
            if (found == null){
                found = new ArrayList<Dot>();
            }
            if (searched == null){
                searched = new ArrayList<Dot>();
            }

            // Adds corresponding points in tree to found
            if (tree != null) {
                List<Dot> foundList = tree.findInCircle(x, y, mouseRadius);
                for (Dot foundDot: foundList){
                    found.add(foundDot);
                }
                List<Dot> searchedLIst = tree.returnSearchPoints(x, y, mouseRadius);
                for (Dot searchedDot: searchedLIst){
                    searched.add(searchedDot);
                }
            }
        }
        else {
            System.out.println("clicked at ("+x+","+y+")");
        }
        repaint();
    }

    /**
     * DrawingGUI method, here drawing the quadtree
     * and if in query mode, the mouse location and any found dots
     */
    @Override
    public void draw(Graphics g) {
        if (tree != null) drawTree(g, tree, 0);
        if (mode == 'q') {
            g.setColor(Color.BLACK);
            g.drawOval(mouseX-mouseRadius, mouseY-mouseRadius, 2*mouseRadius, 2*mouseRadius);
            if (found != null) {
                g.setColor(Color.gray);
                for (Dot d: searched){
                    g.fillOval((int)d.getX()-dotRadius, (int)d.getY()-dotRadius, 2*dotRadius, 2*dotRadius);
                }
                g.setColor(Color.BLACK);
                for (Dot d : found) {
                    g.fillOval((int)d.getX()-dotRadius, (int)d.getY()-dotRadius, 2*dotRadius, 2*dotRadius);
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new DotTreeGUIEC();
            }
        });
    }

}
