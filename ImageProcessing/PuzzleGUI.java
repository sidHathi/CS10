import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

/**
 * Simple puzzle of rectangular fragments from an image. Click on a pair of pieces to swap.
 * 
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2012
 * @author CBK, Winter 2014, rewritten for BufferedImage
 * @author CBK, Spring 2015, from array to ArrayList, refactored GUI
 */
public class PuzzleGUI extends DrawingGUI {
	private static final int numRows = 3, numCols = 4;	// puzzle setup: number of pieces per row and column

	private Puzzle puzzle;								// holds the puzzle state
	private int selectedR = -1, selectedC = -1; 		// selected piece; -1 for none

	public PuzzleGUI(Puzzle puzzle) {
		this.puzzle = puzzle;
		initWindow(puzzle.getNumCols()*puzzle.getPieceWidth(),
					puzzle.getNumRows()*puzzle.getPieceHeight());
	}

	/**
	 * Handles clicking on a piece, by selecting/deselecting/swapping
	 */
	@Override
	public void handleMousePress(int x, int y) {
		// Determine which piece it was.
		int c = x / puzzle.getPieceWidth();
		int r = y / puzzle.getPieceHeight();
		if (selectedC == -1) {
			// First piece to be selected -> remember
			selectedC = c;
			selectedR = r;
		}
		else if (selectedC == c && selectedR == r) {
			// Same piece -> deselect
			selectedC = -1;
		}
		else {
			// Second piece -> swap
			puzzle.swapPieces(selectedR, selectedC, r, c);
			selectedC = -1;
		}
		repaint();
	}

	/**
	 * Lays out the pieces of the puzzle
	 */
	@Override
	public void draw(Graphics g) {
		((Graphics2D) g).setStroke(new BasicStroke(4)); // thick border
		int h = puzzle.getPieceHeight(), w = puzzle.getPieceWidth();
		// Lay out the pieces in a matrix.
		for (int r = 0; r < puzzle.getNumRows(); r++) {
			for (int c = 0; c < puzzle.getNumCols(); c++) {
				g.drawImage(puzzle.getPiece(r,c), c * w, r * h, null);
				// Border -- green for selected, black for others
				if (c == selectedC && r == selectedR) {
					g.setColor(Color.green);
				}
				else {
					g.setColor(Color.black);
				}
				g.drawRect(c * w, r * h, w - 2, h - 2);
			}
		}
	}

	public static void main(String[] args) { 
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				BufferedImage image = loadImage("pictures/baker.jpg");
				new PuzzleGUI(new Puzzle(image, numRows,numCols));
			}
		});
	}
}
