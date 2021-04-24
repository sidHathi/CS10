import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * A GUI to search Flickr for images and step through 10 of them
 * Just the core GUI code here
 *
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2012; revised Winter 2014
 */
public class FlickrSearchCore extends JFrame {
	private static final int imageWidth = 640, imageHeight = 640;	// medium 640 on flickr

	private JComponent canvas;										// drawing component
	private JTextField queryF;										// GUI text field for query
	private String sort = "relevance";								// how to sort when search

	public FlickrSearchCore() {
		super("Flickr Search");

		// Create our graphics-handling component, sized to hold the images
		canvas = new JComponent() {
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				// will add code here to draw the current image
			}
		};
		canvas.setPreferredSize(new Dimension(imageWidth, imageHeight));

		// Create the GUI components
		JPanel gui = setupGUI();

		// Put the GUI and the canvas in the panel, one at the top and one taking the rest of the space
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());
		cp.add(gui, BorderLayout.NORTH);
		cp.add(canvas, BorderLayout.CENTER);

		// Boilerplate
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}

	/**
	 * Creates all the GUI components and adds them to a panel
	 * @return	the panel holding the components
	 */
	private JPanel setupGUI() {
		// prev button steps backward through images
		JButton prevB = new JButton("prev");
		prevB.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				// will add code here to move to the previous image
				System.out.println("prev");
			}
		});

		// next button steps forward through images
		JButton nextB = new JButton("next");
		nextB.addActionListener(e -> {
			// will add code here to move to the next image
			System.out.println("next");
		});

		// sort dropdown (combobox) lists possible ways to sort
		String[] sorts = { "relevance", "interestingness-desc", "interestingness-asc", 
				"date-taken-desc", "date-taken-asc" };
		JComboBox sortB = new JComboBox(sorts);
		sortB.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				sort = (String)((JComboBox)e.getSource()).getSelectedItem();
				System.out.println(sort);
			}
		});

		// text field for the search query
		queryF = new JTextField(20);

		// search button fires off the search
		JButton search = new JButton("search");
		search.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				// will add code here to fire off the search
				System.out.println("search for '" + queryF.getText() + "' by " + sort);
			}
		});

		// Put all the components in a panel
		JPanel gui = new JPanel();
		gui.setLayout(new FlowLayout());
		gui.add(queryF);
		gui.add(sortB);
		gui.add(search);
		gui.add(new JSeparator(SwingConstants.VERTICAL));
		gui.add(prevB);
		gui.add(nextB);

		return gui;
	}

	/**
	 * Main method for the application
	 * @param args		command-line arguments (ignored)
	 */
	public static void main(String[] args) { 
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new FlickrSearchCore();
			}
		});
	}
}
