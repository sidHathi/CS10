import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Handles communication to/from the server for the editor, PD version
 * 
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Winter 2021, based on PS version
 */
public class EditorCommunicatorPD extends Thread {
	private PrintWriter out;		// to server
	private BufferedReader in;		// from server
	protected EditorPD editor;		// handling communication for

	/**
	 * Establishes connection and in/out pair
	 */
	public EditorCommunicatorPD(String serverIP, EditorPD editor) {
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

	public void handleMsg(String msg) {
		// TODO: parse the message
		// If there is no ellipse, make it so
		// If there is an ellipse, create it
		// assume message is correctly formatted, since it's under our control -- no error checking

		String[] tokens = msg.split(" ");
		if (tokens.length != 6 || tokens[0] != "ellipse"){
			System.err.println("Invalid message");
			return;
		}

		Ellipse ellipse = new Ellipse(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3]), Integer.parseInt(tokens[4]), new Color(Integer.parseInt(tokens [5])));
	}

	/**
	 * Tells the server about our ellipse
	 */
	public void updateEllipse(Ellipse ellipse) {
		// TODO: generate a message describing the ellipse

		send(ellipse.toString());
	}

	/**
	 * Requests the server to delete the ellipse
	 */
	public void deleteEllipse() {
		// TODO: generate a message saying that there is no ellipse now

		send("Delete elipse: ");
	}
}
