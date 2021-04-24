import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Quilter {
    private int width, height;                      // size of the quilt
    private BufferedImage quilt;                    // the quilt put together by a call to makeNewQuilt
    private ArrayList<BufferedImage> fabrics;       // possible fabrics to incorporate in the quilt
    private ArrayList<ArrayList<Point>> shapes;     // possible shapes of the pieces to use in the quilt

    public Quilter(int width, int height,
                   ArrayList<BufferedImage> fabrics, ArrayList<ArrayList<Point>> shapes) {
        this.width = width;
        this.height = height;
        this.fabrics = fabrics;
        this.shapes = shapes;
    }

    /**
     * Apply a shape-sized piece of the fabric to the quilt,
     * taking the colors from fabric according to the points in shape shifted by (fx,fy)
     * and putting them in quilt according to those points shifted by (qx,qy)
     */
    public void applyPatch(BufferedImage fabric, ArrayList<Point> shape, int fx, int fy, int qx, int qy) {
        // TODO: your code here

        // iterates over points in shape, assigning colors for each of them
        for (int i = 0; i < shape.size(); i++){
            if (shape.get(i).x+fx < fabric.getWidth() && shape.get(i).y+fy < fabric.getHeight() &&
                    shape.get(i).x+qx < quilt.getWidth() && shape.get(i).y+qy < quilt.getHeight()) {
                int rgbFromShape = fabric.getRGB(shape.get(i).x + fx, shape.get(i).y + fy);
                quilt.setRGB(shape.get(i).x, shape.get(i).y, rgbFromShape);
            }
        }
    }

    public void makeNewQuilt(int numPatches) {
        quilt = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        // TODO: your code here
        for (int i = 0; i < numPatches; i++){
            BufferedImage randFabric = fabrics.get((int) Math.floor(Math.random()*(fabrics.size())));
            ArrayList<Point> randShape = shapes.get((int) Math.floor(Math.random()*(shapes.size())));
            // gets furtest points in x and y direction
            int maxShapeX = 0;
            int maxShapeY = 0;
            for (int j = 0; j < randShape.size(); j++){
                if (randShape.get(j).x > maxShapeX){
                    maxShapeX = randShape.get(j).x;
                }
                if (randShape.get(j).y > maxShapeY){
                    maxShapeY = randShape.get(j).y;
                }
            }
            // Gets fx, fy, qx, qy values that won't exceed sizes of fabric/quilt
            int fx = (int)Math.random()*(randFabric.getWidth() - maxShapeX);
            int fy = (int)Math.random()*(randFabric.getHeight() - maxShapeY);
            int qx = (int)Math.random()*(quilt.getWidth() - maxShapeX);
            int qy = (int)Math.random()*(quilt.getHeight() - maxShapeY);

            applyPatch(randFabric, randShape, fx, fy, qx, qy);
        }

    }
}
