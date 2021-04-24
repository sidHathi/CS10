import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Simple puzzle of rectangular fragments from an image. Click on a pair of pieces to swap.
 * 
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2012
 * @author CBK, Winter 2014, rewritten for BufferedImage
 * @author CBK, Spring 2015, from array to ArrayList, refactored GUI
 */
public class Puzzle {
	private BufferedImage image;					// the whole original image
	private int numRows, numCols;					// how many pieces in the puzzle (specified)
	private int pieceWidth, pieceHeight; 			// size of pieces (computed)
	private ArrayList<BufferedImage> pieces;		// the small images broken out from the whole thing

	public Puzzle(BufferedImage image, int numRows, int numCols) {
		this.image = image;
		this.numRows = numRows;
		this.numCols = numCols;
		
		// Make and shuffle the pieces.
		createPieces();
		shufflePieces();
	}

	/**
	 * Returns the piece at the given row and column
	 */
	public BufferedImage getPiece(int r, int c) {
		return pieces.get(r*numCols + c);
	}
	
	public int getNumRows() {
		return numRows;
	}
	
	public int getNumCols() {
		return numCols;
	}
	
	public int getPieceWidth() {
		return pieceWidth;
	}
	
	public int getPieceHeight() {
		return pieceHeight;
	}
	
	/**
	 * Creates the pieces list, fragments from the image.
	 */
	private void createPieces() {
		// Compute piece size according to how many have to fit in image and image's size.
		pieceWidth = image.getWidth() / numCols;
		pieceHeight = image.getHeight() / numRows;
		// Create and fill up the array, iterating by piece row and piece column.
		pieces = new ArrayList<BufferedImage>();
		for (int r = 0; r < numRows; r++) {
			for (int c = 0; c < numCols; c++) {
				pieces.add(getSubimage(image, c*pieceWidth, r*pieceHeight, pieceWidth, pieceHeight));
			}
		}
	}

	/**
	 * Gets a sub-image of an image, starting at (x0,y0) and extending (dx,dy)
	 * (There's a Java method of BufferedImage that does exactly this, so this is for illustration.)
	 * @param image
	 * @param x0
	 * @param y0
	 * @param dx
	 * @param dy
	 * @return the subimage
	 */
	private static BufferedImage getSubimage(BufferedImage image, int x0, int y0, int dx, int dy) {
		// Create a new empty image of the right size and type to hold the result.
		BufferedImage result = new BufferedImage(dx, dy, image.getType());
		// The usual pattern.
		for (int y = 0; y < dy; y++) {
			for (int x = 0; x < dx; x++) {
				// The (x,y) point comes from (x+x0, y+y0)
				result.setRGB(x, y, image.getRGB(x+x0, y+y0));
			}
		}
		return result;
	}

	/**
	 * Swaps the pieces at r1,c1 with that at r2,c2
	 */
	public void swapPieces(int r1, int c1, int r2, int c2) {
		int index1 = r1*numCols + c1;
		int index2 = r2*numCols + c2;
		//swap pieces using temporary variable
		BufferedImage piece2 = pieces.get(index2); //temporary variable so we don't loose piece at index2
		pieces.set(index2, pieces.get(index1)); //set piece at index2 to be piece at index1 (would have lost i2 without temp)
		pieces.set(index1, piece2);	//set piece at index1 to be piece formerly at index2	
	}

	/**
	 * Shuffles the pieces array
	 */
	private void shufflePieces() {
		// Simple shuffle: swap each piece with some other one
		for (int r = 0; r < numRows; r++) {
			for (int c = 0; c < numCols; c++) {
				int r2 = (int)(Math.random() * numRows);
				int c2 = (int)(Math.random() * numCols);
				swapPieces(r, c, r2, c2);
			}
		}
	}

}
