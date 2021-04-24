import javax.swing.*;
import java.awt.event.ActionEvent;

public class Game extends DrawingGUI {
    private int[] scores;           // each player's score

    // TODO: your code here

    private int[] scoreStreaks; // each player's current streak.

    private int numTicks;		// maximum number of timer ticks before game ends
    private int currentTicks;   // tracks number of times timer has gone off
    private boolean gameOver;      // stores whether game has ended

    public Game(int numPlayers, int numTicks) {
        scores = new int[numPlayers];
        // TODO: your code here
        scoreStreaks = new int[numPlayers];
        this.numTicks = numTicks;

        startTimer();
    }

    boolean goodPosition(double x, double y, double z) {
        // Assume this is implemented to actually return true if and only if (x,y,z) should score points
        return true;
    }

    public void handleController(int controller, double x, double y, double z) {
        // TODO: your code here

        // Checks controller validity and game state
        if (controller < scores.length && !gameOver){
            // Adds points and updates scorestreaks
            if (goodPosition(x, y, z)){
                for (int i = 0; i < scoreStreaks.length; i++){
                    if (i != controller){
                        scoreStreaks[controller] = 0;
                    }
                    else{
                        scoreStreaks[controller] ++;
                    }
                }
            }
            // updates score
            scores[controller] += scoreStreaks[controller];
        }
    }

    @Override
    public void handleTimer() {
        // TODO: your code here
        // this method is created in DrawingGUI, and goes off at intervals
        // here, I'm addint ticks at each of these intervals
        currentTicks ++;
        if (currentTicks > numTicks){
            gameOver = true;
        }
    }
}
