import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.imageio.*;
import javax.swing.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * A GUI to search Flickr for images and step through 10 of them
 *
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2012; revised Winter 2014
 * @author Tim Pierson, Dartmouth CS 10, Fall 2018; updated to use JSON instead of XML
 * 
 * First download json-simple-1.1.1.jar from the course web page.
 * Next add json-simple-1.1.1 to your classpath as you did for the opencv jars
 * IntelliJ: File | Project Structure | SDKs | + ...
 */
public class FlickrSearchJSON extends JFrame {
    private static final int imageWidth = 640, imageHeight = 640;    // medium 640 on flickr
    private static String api_key = "Key here";						 // copy the key from lecture notes, agreeing to use it appropriately for limited testing of this application

    private JComponent canvas;                                        // drawing component
    private JTextField queryF;                                        // GUI text field for query
    private String sort = "relevance";                                // how to sort when search
    private ArrayList<BufferedImage> images;                        // loaded images, using Java type
    private int curr = 0;                                            // index of currently-displayed image

    public FlickrSearchJSON() {
        super("Flickr Search");

        // Initially no images
        images = new ArrayList<BufferedImage>();

        // Create our graphics-handling component, sized to hold the images
        canvas = new JComponent() {
            public void paintComponent(Graphics g) { //called on repaint()
                super.paintComponent(g);
                if (images.size() > 0) {
                    g.drawImage(images.get(curr), 0, 0, null);
                }
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
     *
     * @return the panel holding the components
     */
    private JPanel setupGUI() {
        // prev button steps backward through images
        JButton prevB = new JButton("prev");
        prevB.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (images.size() > 0) {
                    curr--;
                    if (curr < 0) curr = images.size() - 1;
                    repaint();
                }
            }
        });

        // prev button steps forward through images
        JButton nextB = new JButton("next");
        nextB.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (images.size() > 0) {
                    curr = (curr + 1) % images.size();
                    repaint();
                }
            }
        });

        // sort dropdown (combobox) lists possible ways to sort
        String[] sortOrders = {"relevance", "interestingness-desc", "interestingness-asc",
                "date-taken-desc", "date-taken-asc"};
        JComboBox sortB = new JComboBox(sortOrders);
        sortB.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                sort = (String) ((JComboBox) e.getSource()).getSelectedItem();
                System.out.println(sort);
            }
        });

        // text field for the search query
        queryF = new JTextField(20);

        // search button fires off the search
        JButton search = new JButton("search");
        search.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("searching for '" + queryF.getText() + "' by " + sort);
                try {
                    loadImages(queryF.getText());
                    curr = 0;
                    repaint();
                } catch (Exception ex) {
                    System.err.println("search failed");
                }
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
     * Performs a Flickr search for the query and loads all the images returned
     */
    private void loadImages(String query) throws Exception {
        // Get rid of existing images
        images.clear();

        // Build the REST query as specified in the Flickr API
        String request = "https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=" + api_key +
                "&text=" + URLEncoder.encode(query, "UTF-8") + "&sort=" + sort + "&per_page=10&format=json";
        System.out.println("search:" + request);

        // Read JSON response from Flickr and store in String str
        BufferedReader in = new BufferedReader(new InputStreamReader(new URL(request).openStream()));
        String str = "", line;
        while ((line = in.readLine()) != null) str += line;
		
		/* response from Flickr looks like:
		jsonFlickrApi({
		"photos": {
			"page": 1,
			"pages": 266788,
			"perpage": 10,
			"total": "2667876",
			"photo": [{"id": "5340131446", "secret": "3b7c380bea","server": "5244","farm": 6, …}
			  	      {"id": "5338762379", "secret": "59f7435b93","server": "5284","farm": 6,	…},
		*/

        //strip out Flickr's annoying extra text "jsonFlickrApi(" and closing ")" so that we have valid json
        str = str.substring("jsonFlickrApi(".length(), str.length() - 1);
        System.out.println(str);

        try {
            //parse flickr's response as JSON
            JSONParser parser = new JSONParser();
            JSONObject jsonFlickrResponse = (JSONObject) parser.parse(str); //parse in the string returned by Flickr as json

            //get photo array from photos object
            JSONObject photosJsonObject = (JSONObject) jsonFlickrResponse.get("photos"); //get photos json object from Flickr response
            JSONArray photosList = (JSONArray) photosJsonObject.get("photo"); //now we have a List with information about photos

            //loop over each photo in photo array
            for (int i = 0; i < photosList.size(); i++) {
                //get each photo
                JSONObject photoDetails = (JSONObject) photosList.get(i);
                try {
                    // Build the image URL as specified in the Flickr API
                    String url = "https://farm" + photoDetails.get("farm") + ".staticflickr.com/" +
                            photoDetails.get("server") + "/" + photoDetails.get("id") + "_" +
                            photoDetails.get("secret") + "_z.jpg"; //_z means size=640
                    System.out.println(photoDetails.get("title") + " - " + url);

                    //fetch image at URL and add to images
                    BufferedImage img = ImageIO.read(new URL(url));
                    images.add(img);

                } catch (IOException e) {
                    System.out.println("couldn't load image");
                }
            }
        } catch (Exception e) {
            System.out.println("JSON parse error");
            System.out.println(e);
        }
    }

    /**
     * Main method for the application
     *
     * @param args command-line arguments (ignored)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new FlickrSearchJSON();
            }
        });
    }
}
