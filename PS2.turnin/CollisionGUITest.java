import javax.swing.*;

public class CollisionGUITest {

    /**
     * Tests to make sure colliders are detected: creates two blobs
     * in the same locations and makes sure they are added to colliders
     * @param testingCollisions
     */
    public static void collidingBlobsTest(CollisionGUI testingCollisions) {
        Blob testBlob1 = new Wanderer(10, 10);
        Blob testBlob2 = new Wanderer(10, 10);
        testingCollisions.addTestBLob(testBlob1);
        testingCollisions.addTestBLob(testBlob2);

        // In an asynchronous thread, the code waits for the blobs to register as collided
        // and then checks to ensure they actually colided.
        new Thread( new Runnable() {
            public void run()  {
                try {
                    Thread.sleep(1000);

                    if (testingCollisions.getTestBlobs().size() > 1) {
                        if (testingCollisions.getTestColliders().size() > 0) {
                            System.out.println("Collisions Test: Success");
                        } else {
                            System.out.println("Test failed: No Collisions found");
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } ).start();

    }

    /**
     * Test to make sure that two blobs created on opposite ends of the map haven't collided
     * after 10 milliseconds
     * @param testingCollisions
     */
    public static void noCollidingBlobsTest(CollisionGUI testingCollisions){

        Blob testBlob1 = new Wanderer(10, 10);
        Blob testBlob2 = new Wanderer(testingCollisions.getWidth()-20, 10);
        testingCollisions.addTestBLob(testBlob1);
        testingCollisions.addTestBLob(testBlob2);


        // In an asynchronous thread, check to make sure the blobs haven't collided immediately
        new Thread( new Runnable() {
            public void run()  {
                try {
                    Thread.sleep(10);

                    if (testingCollisions.getTestBlobs().size() > 1) {
                        if (testingCollisions.getTestColliders().size() == 0) {
                            System.out.println("No Collisions Test: Success");
                        } else {
                            System.out.println("Test failed: No Collisions found");
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } ).start();

    }

    public static void main(String[] args){
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                CollisionGUI testingCollisions = new CollisionGUI();
                CollisionGUITest.collidingBlobsTest(testingCollisions);
                CollisionGUITest.noCollidingBlobsTest(testingCollisions);
            }
        });
    }

}
