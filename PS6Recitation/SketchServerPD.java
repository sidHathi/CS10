import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

/**
 * A server to handle sketches: getting requests from the clients,
 * updating the overall state, and passing them on to the clients
 * PD version
 *
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Winter 2021, based on PS version
 */
public class SketchServerPD {
	private ServerSocket listen;						// for accepting connections
	private ArrayList<SketchServerCommunicatorPD> comms;	// all the connections with clients

	// *** Much simplified from PS -- just a message instead of a whole sketch
	private String ellipseMsg;		// the state of the world --
									// the latest message describing the ellipse
									// or "none" if it was deleted

	public SketchServerPD(ServerSocket listen) {
		this.listen = listen;
		ellipseMsg = "none";
		comms = new ArrayList<SketchServerCommunicatorPD>();
	}

	// *** Much simplified from PS -- the current state of the sketch is just stored as the last message received,
	// instead of as a whole sketch

	public synchronized void setEllipseMsg(String ellipseMsg) {
		this.ellipseMsg = ellipseMsg;
	}

	public synchronized String getEllipseMsg() {
		return ellipseMsg;
	}
	
	/**
	 * The usual loop of accepting connections and firing off new threads to handle them
	 */
	public void getConnections() throws IOException {
		System.out.println("server ready for connections");
		while (true) {
			SketchServerCommunicatorPD comm = new SketchServerCommunicatorPD(listen.accept(), this);
			comm.setDaemon(true);
			comm.start();
			addCommunicator(comm);
		}
	}

	/**
	 * Adds the communicator to the list of current communicators
	 */
	public synchronized void addCommunicator(SketchServerCommunicatorPD comm) {
		comms.add(comm);
	}

	/**
	 * Removes the communicator from the list of current communicators
	 */
	public synchronized void removeCommunicator(SketchServerCommunicatorPD comm) {
		comms.remove(comm);
	}

	/**
	 * Sends the message from the one communicator to all (including the originator)
	 */
	public synchronized void broadcast(String msg) {
		for (SketchServerCommunicatorPD comm : comms) {
			comm.send(msg);
		}
	}
	
	public static void main(String[] args) throws Exception {
		new SketchServerPD(new ServerSocket(4242)).getConnections();
	}
}
