import java.io.*;
import java.util.*;

/**
 * Contains methods that encode and decode files using Huffman lossless compression
 *
 * @author Siddharth Hathi
 */
public class Huffman {

    // COMPRESSION METHODS:

    /**
     * Reads a text file and returns an ArrayList of all the characters in the file
     * @param pathName
     * @return
     * @throws IOException
     */
    public static ArrayList<Character> getCharacters(String pathName) throws IOException {

        ArrayList<Character> fileCharacters = new ArrayList<Character>();
        BufferedReader input = new BufferedReader(new FileReader(pathName));

        try{
            int cInt = input.read(); // Read next character's integer representation
            while (cInt != -1) {
                char c = (char)cInt;
                fileCharacters.add(c);
                cInt = input.read(); // Read next character's integer representation
            }
        }
        catch(IOException e){
            System.err.println("Unable to read file. Error: " + e.getMessage());
        }
        finally {
            input.close();
        }

        return fileCharacters;

    }

    /**
     * Takes in an ArrayList of characters and builds a map where each unique character is a key,
     * and the number of times it appears in the ArrayList is its correpsonding value.
     * @param characters
     * @return
     */
    public static Map<Character, Integer> getFrequencyMap(ArrayList<Character> characters){

        Map<Character, Integer> frequencyMap = new TreeMap<>();

        for (Character character:  characters){
            if (frequencyMap.containsKey(character)){
                frequencyMap.put(character, frequencyMap.get(character) + 1);
            }
            else{
                frequencyMap.put(character, 1);
            }
        }

        return frequencyMap;
    }


    /**
     * Takes in a frequency map of characters and converts each entry into a Binary Tree.
     * Adds the trees to a priority queue which it returns
     * @param frequencyMap
     * @return
     */
    public static PriorityQueue<BinaryTree<CharacterData>> buildInitialTrees(Map<Character, Integer> frequencyMap){

        // Comparator class for the Trees - sorts by frequency of the tree's data
        class TreeComparator implements Comparator<BinaryTree<CharacterData>> {
            @Override
            public int compare(BinaryTree<CharacterData> o1, BinaryTree<CharacterData> o2) {
                return o1.data.compareTo(o2.data);
            }
        }

        // Initializes the queue
        Comparator<BinaryTree<CharacterData>> treeComparator = new TreeComparator();
        PriorityQueue<BinaryTree<CharacterData>> initialTrees = new PriorityQueue<BinaryTree<CharacterData>>(treeComparator);

        // Adds the entries to the queue
        for (Character key: frequencyMap.keySet()){
            CharacterData frequencyData = new CharacterData(key, frequencyMap.get(key));

            BinaryTree<CharacterData> initialTree = new BinaryTree<CharacterData>(frequencyData);
            initialTrees.add(initialTree);
        }

        return initialTrees;
    }


    /**
     * Converts the priority queue of trees generated in buildInitialTrees into the overarching
     * Huffman encoding tree.
     * @param initialTrees
     * @return
     */
    public static BinaryTree<CharacterData> buildEncodingTree(PriorityQueue<BinaryTree<CharacterData>> initialTrees){

        while(initialTrees.size() > 1){
            // Dequeues two smallest trees by frequency
            BinaryTree<CharacterData> t1 = initialTrees.poll();
            BinaryTree<CharacterData> t2 = initialTrees.poll();

            // Adds them to a new tree whose frequency is the sum of t1 and t2
            CharacterData rootNode = new CharacterData(null, t1.data.getFrequency()+t2.data.getFrequency());
            BinaryTree<CharacterData> t = new BinaryTree<CharacterData>(rootNode, t1, t2);

            // Adds the new tree to the queue
            initialTrees.add(t);
        }

        return initialTrees.poll();
    }

    /**
     * Builds the encoding map for the tree using helper method codeMapHelper.
     * @param encodingData
     * @return
     */
    public static Map<Character, String> getCodeMap(BinaryTree<CharacterData> encodingData){

        // Parameters for codeMapHelper
        Map<Character, String> codeMap = new TreeMap<>();
        String pathSoFar = "";

        codeMapHelper(codeMap, pathSoFar, encodingData);
        return codeMap;
    }

    /**
     * Helper method for getCodeMap. Recursively travels down Hoffman encoding tree and determines
     * the codes for each leaf. Adds the codes to their corresponding key-value entry in codeMap
     * @param codeMap
     * @param pathSoFar
     * @param currentTree
     */
    public static void codeMapHelper(Map<Character, String> codeMap, String pathSoFar, BinaryTree<CharacterData> currentTree){

        if (currentTree.isLeaf()){
            codeMap.put(currentTree.data.getCharacter(), pathSoFar);
        }
        else{
            if (currentTree.hasLeft()){
                codeMapHelper(codeMap, pathSoFar + "0", currentTree.getLeft());
            }
            if (currentTree.hasRight()){
                codeMapHelper(codeMap, pathSoFar + "1", currentTree.getRight());
            }
        }

    }

    /**
     * Encodes the list of characters extracted from the input file given an encoding map
     * @param initialCharacters
     * @param codeMap
     * @param outputFilename
     * @throws IOException
     */
    public static void writeOutputFile(ArrayList<Character> initialCharacters, Map<Character, String> codeMap, String outputFilename) throws IOException{

        BufferedBitWriter writer = new BufferedBitWriter(outputFilename);

        for (int i = 0; i < initialCharacters.size(); i ++){
            String encodedChars = codeMap.get(initialCharacters.get(i));

            char[] chars = encodedChars.toCharArray();
            for (Character character: chars){
                if (character == '0'){
                    writer.writeBit(false);
                }
                else if (character == '1'){
                    writer.writeBit(true);
                }
            }
        }

        writer.close();

    }


    /**
     * Puts together the functionality of all the preceding compression methods in this class
     * to finally compress the data and write it to the output file.
     * @param inputFilename
     * @param outputFileName
     * @return
     */
    public static BinaryTree<CharacterData> compressFile(String inputFilename, String outputFileName){

        ArrayList<Character> characters = new ArrayList<Character>();

        try{
            characters = getCharacters(inputFilename);
        }
        catch (IOException e) {
            System.err.println("Unable to read file. Error: " + e.getMessage());
            return null;
        }

        if (characters.size() > 0) {
            Map<Character, Integer> frequencyMap = getFrequencyMap(characters);
            PriorityQueue<BinaryTree<CharacterData>> initialTrees = buildInitialTrees(frequencyMap);
            BinaryTree<CharacterData> finalTree = buildEncodingTree(initialTrees);

            Map<Character, String> codeMap = getCodeMap(finalTree);

            try{
                writeOutputFile(characters, codeMap, outputFileName);
            }
            catch (IOException e) {
                System.err.println("Unable to write file. Error: " + e.getMessage());
            }

            return finalTree;
        }
        else{
            System.err.println("Empty File");
        }
        return null;

    }


    // DECOMPRESSION METHODS:

    /**
     * Reads a compressed datafile and places data into ArrayList of boolean bits.
     * @param filename
     * @return
     * @throws IOException
     */
    public static ArrayList<Boolean> getCompressedBits(String filename) throws IOException{

        ArrayList<Boolean> bitList = new ArrayList<Boolean>();
        BufferedBitReader bitReader = new BufferedBitReader(filename);

        while (bitReader.hasNext()){

            boolean bit = bitReader.readBit();
            bitList.add(bit);

        }

        bitReader.close();

        return bitList;
    }

    /**
     * Uses the Huffman binary tree used to encode the data to build an ArrayList of Characters
     * out of the boolean list from getCompressedBits
     * @param bits
     * @param codeTree
     * @return
     */
    public static ArrayList<Character> convertBitsToCharacters(ArrayList<Boolean> bits, BinaryTree<CharacterData> codeTree){

        ArrayList<Character> characters = new ArrayList<Character>();
        BinaryTree<CharacterData> currentTree = codeTree;

        // The bits act as a map that tells this loop how to traverse the tree.
        for (Boolean bit: bits){

            if (bit){
                if (currentTree.hasRight()){
                    currentTree = currentTree.getRight();
                }
                else{
                    System.err.println("No right node at: " + currentTree);
                }
            }
            else{
                if (currentTree.hasLeft()){
                    currentTree = currentTree.getLeft();
                }
                else {
                    System.err.println("No left node at: " + currentTree);
                }
            }

            // When a leaf node is reached, its contents are added to the character list
            // and the loop moves back up to the top of the tree.
            if (currentTree.isLeaf()){
                characters.add(currentTree.data.getCharacter());
                currentTree = codeTree;
            }
        }

        return characters;

    }

    /**
     * Takes in an ArrayList of characters and writes it to a specified file location
     * @param outputFilename
     * @param decompressedCharacters
     * @throws IOException
     */
    public static void writeDecompressedFile(String outputFilename, ArrayList<Character> decompressedCharacters) throws IOException{

        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilename));

        for (Character character: decompressedCharacters){
            writer.write(character);
        }

        writer.close();
    }

    /**
     * Main decompression functions. Uses the other functions in the decompression section to revert
     * a compressed Huffman file into its uncompressed form
     * @param compressedFilename
     * @param outputFilename
     * @param codeTree
     */
    public static void decompress(String compressedFilename, String outputFilename, BinaryTree<CharacterData> codeTree){

        // Reads the compressed file
        ArrayList<Boolean> compressedBits = new ArrayList<Boolean>();
        try{
            compressedBits = getCompressedBits(compressedFilename);
        }
        catch(IOException e){
            System.err.println("Unable to read file. Error: " + e.getMessage());
            return;
        }

        // Decompresses file
        ArrayList<Character> decompressedCharacters = convertBitsToCharacters(compressedBits, codeTree);

        // Writes the decompressed file
        try {
            writeDecompressedFile(outputFilename, decompressedCharacters);
        }
        catch(IOException e){
            System.err.println("Unable to write file. Error: " + e.getMessage());
        }

    }

    /**
     * Applies the compression and decompression methods to a given file
     * @param filename
     */
    public static void testCompression(String filename){

        // Compresses the file and extracts the Huffman encoding tree
        BinaryTree<CharacterData> codeTree = compressFile("inputs/"+filename+".txt", "inputs/"+filename+"_compressed.txt");
        // Decompresses the file if a tree was generated
        if(codeTree != null) {
            decompress("inputs/" + filename + "_compressed.txt", "inputs/" + filename + "_decompressed.txt", codeTree);
        }

    }

    /**
     * main method - runs other methods in class
     * @param args
     */
    public static void main(String[] args){

        // Tests compression and decompression methods on several different files in the input folder
        testCompression("hello");
        testCompression("USConstitution");
        testCompression("WarAndPeace");
        testCompression("empty");

    }

}
