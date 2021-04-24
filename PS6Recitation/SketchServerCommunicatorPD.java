import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Handles communication between the server and one client, for SketchServerPD (PD version)
 *
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Winter 2021, based on PS version
 */
public class SketchServerCommunicatorPD extends Thread {
	private Socket sock;					// to talk with client
	private BufferedReader in;				// from client
	private PrintWriter out;				// to client
	private SketchServerPD server;			// handling communication for

	public SketchServerCommunicatorPD(Socket sock, SketchServerPD server) {
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

			// Tell the client the current state of the world [here, maybe an ellipse]
			// *** Much simplified from PS -- the state is just a message instead of a whole sketch
			// TODO: tell the client what the state is (here, just the latest message)
			// Keep getting and handling messages from the client
			String msg;
			while ((msg = in.readLine()) != null) {
				System.out.println("got msg "+msg);
				// Update the server according to the message
				// TODO: update the current state (here, just save this message)
				out.println(msg);
				// Have the server tell everyone about the current state
				// TODO: broadcast the update (here, just the current message)
				server.notifyAll();
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
}
