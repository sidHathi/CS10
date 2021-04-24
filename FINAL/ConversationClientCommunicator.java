import java.io.*;
import java.net.*;
import java.util.*;

/**
 * This is the same structure as usual.
 * I created a Channel instance variable to hold the state.
 * I isolated a handleMessage whose body you need to fill in, to update the channel.
 */
public class ConversationClientCommunicator extends Thread {
	private ConversationClient client;		// for which this is handling communication
	private BufferedReader in;				// from server
	private PrintWriter out;				// to server
	public Channel channel;					// *** the conversations

	/**
	 * Establishes connection and in/out pair
	 */
	public ConversationClientCommunicator(String serverIP, ConversationClient client) {
		this.client = client;
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
		channel = new Channel();		// *** initialize the instance
	}

	/**
	 * Sends message to the server
	 */
	public void send(String msg) {
		out.println(msg);
	}

	/**
	 * Keeps listening for and handling messages from the server
	 */
	public void run() {
		try {
			String msg;
			// Handle messages
			while ((msg = in.readLine()) != null) {
				System.out.println(msg);
				handleMessage(msg);
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			System.out.println("server hung up");
		}
	}

	private synchronized void handleMessage(String msg) {
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
	 * Handles messages that need to be added to an existing conversation
	 * Updates channel accordingly
	 * @param msg
	 */
	private void handleReply(String msg){
		// breaks down message into its parts
		String[] separatedMessage = msg.split(":", 3);
		if (separatedMessage.length < 3){
			System.err.println("Invalid input");
			return;
		}

		// updates channel
		int index = Integer.parseInt(separatedMessage[0]);
		channel.replyToConversation(index, separatedMessage[1], separatedMessage[2]);
	}

	/**
	 * Handles messages that start new conversations
	 * updates channel accordingly
	 * @param msg
	 */
	private void handleNewConversation(String msg){
		// breaks down message into its parts
		String[] separatedMessage = msg.split(":", 3);
		if (separatedMessage.length < 3){
			System.err.println("Invalid input");
			return;
		}

		// updates channel
		channel.addNewPost(separatedMessage[1], separatedMessage[2]);
	}
}
