import java.awt.*;
import java.awt.image.BufferedImage;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Tourist extends Blob {

    private String currentDir;

    private ArrayList<Point> tour;

    private BufferedImage map;
    // TODO: your code here

    public Tourist(BufferedImage map) {
        this.map = map;
        this.tour = new ArrayList<Point>();
        // TODO: your code here
    }

    public ArrayList<String> getAvailableDirections() {
        ArrayList<String> output = new ArrayList<String>();
        if ((map.getRGB((int) this.x, (int) Math.min(this.y + 1, map.getHeight())) == 1) && (currentDir != "N")) {
            output.add("N");
        }
        if ((map.getRGB((int) this.x, (int) Math.max(this.y - 1, 0)) == 1) && (currentDir != "S")) {
            output.add("S");
        }
        if ((map.getRGB((int) Math.min(this.x + 1, map.getWidth()), (int) this.y) == 1) && (currentDir != "E")) {
            output.add("E");
        }
        if ((map.getRGB((int) Math.max(this.x - 1, 0), (int) this.y) == 1) && (currentDir != "W")) {
            output.add("W");
        }
        return output;
    }

    public void updateDirection() {
        // TODO: your code here

        ArrayList<String> allowedDirections = getAvailableDirections();
        String newDirection = "";

        if (allowedDirections.size() > 0){
            newDirection = allowedDirections.get((int) Math.floor(Math.random()*(allowedDirections.size())));
        }

        this.currentDir = newDirection;
    }

    public void step() {
        // TODO: your code here
        if (getAvailableDirections().size() > 1){
            updateDirection();
        }
        if (currentDir == "N"){
            if (this.y < map.getHeight()){
                this.y += 1;
            }
        }
        else if (currentDir == "S"){
            if (this.y > 0){
                this.y -= 1;
            }
        }
        else if (currentDir == "W"){
            if (this.x > 0){
                this.x -= 1;
            }
        }
        else if (currentDir == "E"){
            if (this.x < map.getWidth()){
                this.x += 1;
            }
        }
    }

    public void draw(Graphics g) {
        // TODO: your code here
        for (int i = 0; i < tour.size(); i++){
            g.setColor(Color.BLUE);
            g.fillOval(tour.get(i).x, tour.get(i).y, 3, 3);
            g.setColor(Color.GREEN);
            g.fillOval((int)this.x, (int)this.y, (int)this.r, (int)this.r);
        }
        this.tour.add(new Point((int)this.x, (int)this.y));
    }
}
