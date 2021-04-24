import java.io.*;
import java.net.*;
import java.util.*;

/**
 * This is the same structure as usual.
 * I isolated a handleMessage whose body you need to fill in, to update the channel held by the server.
 */
public class ConversationServerCommunicator extends Thread {
	private Socket sock;					// to talk with client
	private BufferedReader in;				// from client
	private PrintWriter out;				// to client
	private ConversationServer server;		// handling communication for

	public ConversationServerCommunicator(Socket sock, ConversationServer server) {
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
	 * Keeps listening for and handling messages from the client
	 */
	public void run() {
		try {
			System.out.println("someone connected");

			// Communication channel
			in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			out = new PrintWriter(sock.getOutputStream(), true);

			// Keep getting and handling messages from the client
			String msg;
			while ((msg = in.readLine()) != null) {
				System.out.println("received:" + msg);
				handleMessage(msg);
			}
			System.out.println("hung up");

			// Clean up
			out.close();
			in.close();
			sock.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void handleMessage(String msg) {
		// TODO: your code here

		// breaks down message into its identifying parts and calls the appropriate helper methods
		// depending on what kind of message it is
		String[] separatedMessage = msg.split(":", 2);
		if (separatedMessage.length < 2){
			System.err.println("Invalid input");
			return;
		}

		if (separatedMessage[0].equals("New")){
			handleNewConversation(separatedMessage[1]);
		}
		else {
			handleReply(msg);
		}
	}

	/**
	 * handles messages that need to be added to existing conversations.
	 * Calls the appropriate channel method and broadcasts the change to other clients
	 * @param msg
	 */
	public void handleReply(String msg){
		// Breaks down the message
		String[] separatedMessage = msg.split(":", 3);
		if (separatedMessage.length < 3){
			System.err.println("Invalid input");
			return;
		}

		// Updates channesl and broadcasts
		int index = Integer.parseInt(separatedMessage[0]);
		server.channel.replyToConversation(index, separatedMessage[1], separatedMessage[2]);
		server.broadcast(msg);
	}

	/**
	 * handles messages that will start new conversations
	 * Calls the appropriate channel method and broadcasts the change to other clients
	 * @param msg
	 */
	public void handleNewConversation(String msg){
		// breaks down the message
		String[] separatedMessage = msg.split(":", 3);
		if (separatedMessage.length < 3){
			System.err.println("Invalid input");
			return;
		}

		// updates channel and braodcasts
		server.channel.addNewPost(separatedMessage[1], separatedMessage[2]);
		server.broadcast(msg);
	}
}
