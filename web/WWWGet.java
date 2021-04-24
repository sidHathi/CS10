import java.net.*;
import java.io.*;

/**
 * Demonstrates getting a webpage from the internet
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2012
 * @author Tim Pierson, Dartmouth CS 10, Fall 2018 -- small tweaks
 * @author Tim Pierson, Dartmouth CS 10, Winter 2020 -- update to point to html file
 * @author CBK, Winter 2021 -- index page
 */
public class WWWGet {
	public static void main(String[] args) throws Exception {
		// Open a stream reader for processing the response from the URL
		URL url = new URL("https://www.cs.dartmouth.edu/~tjp/cs10/index.html");
		System.out.println("*** getting " + url);
		BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
		// Read lines from the stream, just like reading a file
		String line;
		while ((line = in.readLine()) != null) {
			System.out.println(line);
		}
		in.close();
		System.out.println("*** done");
	}
}
