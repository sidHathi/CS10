/**
 * Class SA1 - extends Blob
 *
 * Creates Blob that moves 'numSteps' in a random direction
 * before switching to another random direction.
*/
public class SA1 extends Blob {

    protected int numSteps, stepCounter;

    public SA1(double iniX, double iniY){
        super(iniX, iniY);

        this.numSteps = (int)(Math.random()*18 + 12);
        System.out.println(numSteps);
    }

    @Override
    public void step(){
        this.x += dx;
        this.y += dy;

        stepCounter++;
        System.out.println(stepCounter);
        if (stepCounter == numSteps){
            dx = Math.random()*2 - 1;
            dy = Math.random()*2 - 1;
            stepCounter = 0;
        }
    }

}
