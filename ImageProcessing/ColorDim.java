import java.awt.Color;
import java.awt.image.BufferedImage;
/**
 * demonstrate how to pick up a color and dim it 
 * 
 * @author Tim Pierson, Dartmouth CS 10, Spring 2019
 *
 */
public class ColorDim {

	public static void main(String[] args) {
		//load an image
		BufferedImage image = DrawingGUI.loadImage("pictures/baker.jpg");
		int x=0, y=0;
		
		//pick up color of pixel at x,y location
		Color color = new Color(image.getRGB(x,y));
		System.out.println("Original color "+ color);
		
		//extract red, green and blue components
		//divide by 2 to dim them
		int red = color.getRed()/2;
		int green = color.getGreen()/2;
		int blue = color.getBlue()/2;
		
		//write dimmed color back to image
		Color newColor = new Color(red, green, blue);
		System.out.println("New color " + newColor);
		image.setRGB(x, y, newColor.getRGB());
	}
}
