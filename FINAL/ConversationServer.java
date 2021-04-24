import java.net.*;
import java.util.*;
import java.io.*;

/**
 * This is all basically the same as what you've seen before.
 * The only addition is a Channel instance variable for the server.
 * I made it public for simplicity, so that the communicators can directly access it.
 * You don't need to fill in anything here.
 */
public class ConversationServer {
	private ServerSocket listen;						// for accepting connections
	private ArrayList<ConversationServerCommunicator> comms;	// all the connections with clients
	public Channel channel;					// *** the conversations

	public ConversationServer(ServerSocket listen) {
		this.listen = listen;
		comms = new ArrayList<>();
		channel = new Channel();		// *** initialize the instance
	}

	/**
	 * The usual loop of accepting connections and firing off new threads to handle them
	 */
	public void getConnections() throws IOException {
		System.out.println("waiting for someone to connect");

		while (true) {
			ConversationServerCommunicator comm = new ConversationServerCommunicator(listen.accept(), this);
			comm.setDaemon(true);
			comm.start();
			addCommunicator(comm);
		}
	}

	/**
	 * Adds the communicator to the list of current communicators
	 */
	public synchronized void addCommunicator(ConversationServerCommunicator comm) {
		comms.add(comm);
	}

	/**
	 * Removes the communicator from the list of current communicators
	 */
	public synchronized void removeCommunicator(ConversationServerCommunicator comm) {
		comms.remove(comm);
	}

	/**
	 * Sends the message from the one communicator to all (including the originator)
	 */
	public synchronized void broadcast(String msg) {
		for (ConversationServerCommunicator c : comms) {
			c.send(msg);
		}
	}

	public static void main(String[] args) throws IOException {
		ConversationServer server = new ConversationServer(new ServerSocket(4242));
		server.getConnections();
	}
}
