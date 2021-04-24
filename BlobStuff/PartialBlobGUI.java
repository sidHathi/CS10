import java.awt.*;
import javax.swing.*;

public class PartialBlobGUI extends DrawingGUI {
  private static final int width=800, height=600;		// setup: size of the world
	
  public PartialBlobGUI() {
    super("Animated Blob", width, height);
  }
	
  public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
       public void run() {
         new PartialBlobGUI();
       }
    });
  }
}
