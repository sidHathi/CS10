import java.io.*;
import java.awt.*;
import java.net.Socket;
import java.util.*;

/**
 * Handles communication between the server and one client, for SketchServer
 *
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2012; revised Winter 2014 to separate SketchServerCommunicator
 */
public class SketchServerCommunicator extends Thread {
	private Socket sock;					// to talk with client
	private BufferedReader in;				// from client
	private PrintWriter out;				// to client
	private SketchServer server;			// handling communication for

	public SketchServerCommunicator(Socket sock, SketchServer server) {
		this.sock = sock;
		this.server = server;
	}

	/**
	 * Sends a message to the client
	 * @param msg
	 */
	public void send(String msg) {
		out.println(msg);
	}
	
	/**
	 * Keeps listening for and handling (your code) messages from the client
	 */
	public void run() {
		try {
			System.out.println("someone connected");
			
			// Communication channel
			in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			out = new PrintWriter(sock.getOutputStream(), true);

			// Tell the client the current state of the world
			// TODO: YOUR CODE HERE
			send(server.getSketch().toString());

			// Keep getting and handling messages from the client
			// TODO: YOUR CODE HERE
			String msg;
			while ((msg = in.readLine()) != null) {
				System.out.println("got msg "+msg);
				// Update the server according to the message
				handleClientMessage(msg);
			}

			// Clean up -- note that also remove self from server's list so it doesn't broadcast here
			server.removeCommunicator(this);
			out.close();
			in.close();
			sock.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Takes in a message from some client and calls the appropriate function depending
	 * on the message's type. Synchronized to ensure messages handled one by one
	 * @param msg
	 */
	synchronized public void handleClientMessage(String msg){
		if (msg == null){
			System.err.println("Invalid message");
		}
		String[] parts = msg.split(" ");
		if (parts.length < 2){
			System.err.println("Invalid message");
			return;
		}

		if (parts[0].equals("add")){
			handleShapeAddMessage(msg);
		}
		else if (parts[0].equals("move")){
			handleShapeMoveMessage(msg);
		}
		else if (parts[0].equals("recolor")){
			handleShapeRecolorMessage(msg);
		}
		else if (parts[0].equals("delete")){
			handleShapeDeleteMessage(msg);
		}
	}

	/**
	 * Interprets an add request from the client. Adds the specified shape to the centralized
	 * Sketch and gives the new shape an id. Informs all clients to add the new shape.
	 * Synchronized to ensure shapes added one by one.
	 * @param msg
	 */
	synchronized public void handleShapeAddMessage(String msg){
		String[] parts = msg.split(" ");
		if (parts.length < 7){
			return;
		}

		Shape newShape = null;
		if (parts[1].equals("ellipse")){
			newShape = new Ellipse(Integer.parseInt(parts[2]), Integer.parseInt(parts[3]),
					Integer.parseInt(parts[4]), Integer.parseInt(parts[5]), new Color(Integer.parseInt(parts[6])));
		}
		else if (parts[1].equals("rectangle")){
			newShape = new Rectangle(Integer.parseInt(parts[2]), Integer.parseInt(parts[3]),
					Integer.parseInt(parts[4]), Integer.parseInt(parts[5]), new Color(Integer.parseInt(parts[6])));
		}
		else if (parts[1].equals("segment")){
			newShape = new Segment(Integer.parseInt(parts[2]), Integer.parseInt(parts[3]),
					Integer.parseInt(parts[4]), Integer.parseInt(parts[5]), new Color(Integer.parseInt(parts[6])));
		}

		if (newShape != null){
			setNewShapeID(newShape);
			server.getSketch().addShape(newShape);
			server.broadcast("add " + newShape.toString());
		}
	}

	/**
	 * Interprets move request from client. Moves shape in master sketch and tells
	 * all the other clients to move their version of the shape as well. Synchronized
	 * to maintain veracity of master sketch
	 * @param msg
	 */
	synchronized public void handleShapeMoveMessage(String msg){
		String[] parts = msg.split(" ");
		if (parts.length < 4){
			return;
		}

		for (Shape shape: server.getSketch().getShapeList()){
			if (shape.getID().equals(parts[1])){
				shape.moveBy(Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
				server.broadcast(msg);
			}
		}
	}

	/**
	 * Interprets recoloring message from client. Recolors specified shape
	 * in master sketch and routes message to all clients. Synchronized to maintain
	 * veracity of master sketch.
	 * @param msg
	 */
	synchronized public void handleShapeRecolorMessage(String msg){
		String[] parts = msg.split(" ");
		if (parts.length < 3){
			return;
		}

		for (Shape shape: server.getSketch().getShapeList()){
			if (shape.getID().equals(parts[1])){
				shape.setColor(new Color(Integer.parseInt(parts[2])));
				server.broadcast(msg);
			}
		}

	}

	/**
	 * Interprets delete message from client. Removes shape from master
	 * sketch if it exists and tells all clients to delete the shape.
	 * Synchronized to avoid null pointer exceptions
	 * @param msg
	 */
	synchronized public void handleShapeDeleteMessage(String msg){
		String[] parts = msg.split(" ");
		if (parts.length < 2){
			return;
		}

		Iterator shapeIterator = server.getSketch().getShapeList().iterator();
		while (shapeIterator.hasNext()){
			Shape shape = (Shape)shapeIterator.next();
			if (shape.getID().equals(parts[1])){
				shapeIterator.remove();
				server.broadcast(msg);
			}
		}
	}

	/**
	 * Sets some shape's id. Synchronized to ensure two shape never get the same id.
	 * @param shape
	 */
	synchronized public void setNewShapeID(Shape shape){
		String id = UUID.randomUUID().toString();
		shape.setID(id);
	}
}
