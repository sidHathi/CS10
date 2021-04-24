import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Writes bits to a file.  Accumulates bits until gets a byte, 
 * then writes it.  On closing writes an additional byte holding
 * the number of valid bits in the final byte written.
 * 
 * @author Scot Drysdale
 * @author Chris Bailey-Kellogg, Spring 2016, bits are now boolean
 * @author CBK, Fall 2016, max to write (to avoid filling filesystem when have infinite loop)
 */
public class BufferedBitWriter {
	private byte currentByte;     	// The byte that is being filled
	private byte numBitsWritten;  	// Number of bits written to the current byte
	public static int maxBytes = 1000000000;  // So can bail out if file gets too big
	private int totalBytes;			// Exception when exceeds max
	private BufferedOutputStream output; // The output byte stream

	/**
	 * Constructor
	 * @param pathName the path name of the file to be written
	 * @throws FileNotFoundException
	 */
	public BufferedBitWriter(String pathName) throws FileNotFoundException {
		currentByte = 0;
		numBitsWritten = 0;
		totalBytes = 0;
		output = new BufferedOutputStream(new FileOutputStream(pathName));
	}

	/**
	 * writes a bit to the file (virtually)
	 * @param bit the bit to be written
	 * @throws IOException
	 */
	public void writeBit(boolean bit) throws IOException {
		numBitsWritten++;
		currentByte |= (bit?1:0) << (8 - numBitsWritten);
		if(numBitsWritten == 8) {  // Have we got a full byte?
			output.write(currentByte);
			numBitsWritten = 0;
			currentByte = 0;
			totalBytes++;
			if (totalBytes >= maxBytes) throw new IOException("file overflow -- do you have an infinite loop?");
		}
	}

	/**
	 * Closes this bitstream.  Writes any partial byte, followed by 
	 * the number of valid bits in the final byte.
	 * The file will always have at least 2 bytes.  An file representing
	 * no bits will have two zero bytes.
	 * If this is not called the file will not be correctly read by 
	 *   a BufferedBitReader
	 *   
	 * @throws IOException
	 */
	public void close() throws IOException {
		output.write(currentByte);
		output.write(numBitsWritten);

		output.close();
	}
}
