import java.util.*;

/**
 * Object Class used to store a consistent list of shapes across multiple server clients.
 */
public class Sketch {
    // the list of shapes
    private List<Shape> shapeList;

    /**
     * Constructor - initializes empty list
     */
    public Sketch(){
        this.shapeList = new ArrayList<Shape>();
    }

    /**
     * Adds a shape
     * @param shape
     */
    public void addShape(Shape shape){
        this.shapeList.add(shape);
    }

    /**
     * Removes a shape by comparing ids
     * @param toDelete
     */
    public void deleteShape(Shape toDelete){
        if (toDelete.getID() == null){
            System.err.println("Shape has no id");
        }
        Iterator itr = shapeList.iterator();
        while (itr.hasNext()){
            Shape temp = (Shape)itr.next();
            if (temp.getID().equals(toDelete.getID())){
                itr.remove();
            }
        }
    }

    /**
     * returns shapes
     * @return
     */
    public List<Shape> getShapeList(){
        return this.shapeList;
    }

    /**
     * Encodes shape list so it can be transmitted from client to client.
     * @return
     */
    @Override
    public String toString(){
        String output = "sketch {";
        for (Shape shape: shapeList){
            output += shape.toString();
            output += ", ";
        }
        output += "}";
        return output;
    }
}
