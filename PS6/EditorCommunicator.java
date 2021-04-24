import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.Iterator;

/**
 * Handles communication to/from the server for the editor
 * 
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2012
 * @author Chris Bailey-Kellogg; overall structure substantially revised Winter 2014
 * @author Travis Peters, Dartmouth CS 10, Winter 2015; remove EditorCommunicatorStandalone (use echo server for testing)
 */
public class EditorCommunicator extends Thread {
	private PrintWriter out;		// to server
	private BufferedReader in;		// from server
	protected Editor editor;		// handling communication for

	/**
	 * Establishes connection and in/out pair
	 */
	public EditorCommunicator(String serverIP, Editor editor) {
		this.editor = editor;
		System.out.println("connecting to " + serverIP + "...");
		try {
			Socket sock = new Socket(serverIP, 4242);
			out = new PrintWriter(sock.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			System.out.println("...connected");
		}
		catch (IOException e) {
			System.err.println("couldn't connect");
			System.exit(-1);
		}
	}

	/**
	 * Sends message to the server
	 */
	public void send(String msg) {
		out.println(msg);
	}

	/**
	 * Keeps listening for and handling (your code) messages from the server
	 */
	public void run() {
		try {
			// Handle messages
			// TODO: YOUR CODE HERE
			String msg;
			while ((msg = in.readLine()) != null) {
				System.out.println("got msg "+msg);
				handleMsg(msg); // your code, below
				editor.repaint();
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			System.out.println("server hung up");
		}
	}

	/**
	 * Centralized function that calls appropriate handling functions based on message contents
	 * @param msg
	 */
	public void handleMsg(String msg){
		if (msg == null){
			return;
		}

		String[] parts = msg.split(" ");
		if (parts.length < 2){
			System.err.println("Invalid message from server");
		}

		if (parts[0].equals("add")){
			handleAdd(msg);
		}
		else if (parts[0].equals("recolor")){
			handleRecolor(msg);
		}
		else if (parts[0].equals("move")){
			handleMove(msg);
		}
		else if (parts[0].equals("delete")){
			handleDelete(msg);
		}
		else if (parts[0].equals("sketch")){
			initializeSketch(msg);
		}
	}

	/**
	 * Interprets an add message from the server to append the appropriate
	 * shape to the local editor's sketch. Synchronized to ensure same shape
	 * isn't added twice
	 * @param msg
	 */
	synchronized public void handleAdd(String msg){
		String[] parts = msg.split(" ");
		if (parts.length < 8){
			return;
		}

		Shape newShape = null;
		if (parts[1].equals("ellipse")){
			newShape = new Ellipse(Integer.parseInt(parts[2]), Integer.parseInt(parts[3]),
					Integer.parseInt(parts[4]), Integer.parseInt(parts[5]), new Color(Integer.parseInt(parts[6])), parts[7]);
		}
		else if (parts[1].equals("rectangle")){
			newShape = new Rectangle(Integer.parseInt(parts[2]), Integer.parseInt(parts[3]),
					Integer.parseInt(parts[4]), Integer.parseInt(parts[5]), new Color(Integer.parseInt(parts[6])), parts[7]);
		}
		else if (parts[1].equals("segment")){
			newShape = new Segment(Integer.parseInt(parts[2]), Integer.parseInt(parts[3]),
					Integer.parseInt(parts[4]), Integer.parseInt(parts[5]), new Color(Integer.parseInt(parts[6])), parts[7]);
		}

		if (newShape != null){
			editor.getSketch().addShape(newShape);
			editor.repaint();
		}
	}

	/**
	 * Interprets move message from the server to
	 * move one of the local editor's shapes by the specified amount.
	 * Synchronized to make sure the local move lines up with master sketch
	 * @param msg
	 */
	synchronized public void handleMove(String msg){
		String[] parts = msg.split(" ");
		if (parts.length < 4){
			return;
		}

		for (Shape shape: editor.getSketch().getShapeList()){
			if (shape.getID().equals(parts[1])){
				shape.moveBy(Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
			}
		}
	}

	/**
	 * Interprets a recolor message from the server and recolors the appropriate local shape.
	 * Synchronized to ensure correct order of recoloring for simultaneous recolor requests
	 * @param msg
	 */
	synchronized public void handleRecolor(String msg){
		String[] parts = msg.split(" ");
		if (parts.length < 3){
			return;
		}

		for (Shape shape: editor.getSketch().getShapeList()){
			if (shape.getID().equals(parts[1])){
				shape.setColor(new Color(Integer.parseInt(parts[2])));
			}
		}

	}

	/**
	 * Reads delete message from server and deletes specified shape.
	 * Synchronized to avoid null pointer exceptions
	 * @param msg
	 */
	synchronized public void handleDelete(String msg){
		String[] parts = msg.split(" ");
		if (parts.length < 2){
			return;
		}

		Iterator shapeIterator = editor.getSketch().getShapeList().iterator();
		while (shapeIterator.hasNext()){
			Shape shape = (Shape)shapeIterator.next();
			if (shape.getID().equals(parts[1])){
				shapeIterator.remove();
				editor.delete();
				editor.repaint();
			}
		}

	}

	/**
	 * Reads the sketch data provided by the server to initialize the drawing panel and adds the specified shapes.
	 * @param msg
	 */
	public void initializeSketch(String msg){
		String shapeList = msg.substring(msg.indexOf("{")+1, msg.indexOf("}"));
		String[] shapes = shapeList.split(", ");
		System.out.println(shapes[0]);

		for (String shapeStr: shapes){
			String[] parts = shapeStr.split(" ");
			if (parts.length < 7) continue;
			if (parts[0].equals("ellipse")){
				editor.getSketch().addShape(new Ellipse(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]),
						Integer.parseInt(parts[3]), Integer.parseInt(parts[4]), new Color(Integer.parseInt(parts[5])), parts[6]));
			}
			else if (parts[0].equals("rectangle")){
				editor.getSketch().addShape(new Rectangle(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]),
					Integer.parseInt(parts[3]), Integer.parseInt(parts[4]), new Color(Integer.parseInt(parts[5])), parts[6]));
			}
			else if (parts[0].equals("segment")){
				editor.getSketch().addShape(new Segment(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]),
						Integer.parseInt(parts[3]), Integer.parseInt(parts[4]), new Color(Integer.parseInt(parts[5])), parts[6]));
			}
		}
	}
	
}
