import java.io.*;
import java.util.*;

/**
 * Testing Library for Viterbi. Contains functions for testing Viterbi functionality
 * On both console input and example files.
 */
public class ViterbiTestLib {

    /**
     * Returns lines as String arrays from some input file containing sentences
     * @param filename
     * @return
     * @throws IOException
     */
    public static List<String[]> getSentencesFromFile(String filename) throws IOException{
        ArrayList<String[]> sentences = new ArrayList<String[]>();

        BufferedReader in = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = in.readLine()) != null) {
            if (line.length()==0 || line.charAt(0)=='!') continue;
            String[] sentence = line.split(" ");
            sentences.add(sentence);
        }

        return sentences;
    }

    /**
     * Trains Markov Model from input files and evaluates performance of Viterbi functions
     * on testing files
     * @param sentenceFileNameTrain
     * @param tagFileNameTrain
     * @param sentenceFileNameTest
     * @param tagFileNameTest
     * @throws IOException
     */
    public static void evaluateViterbiFromFiles(String sentenceFileNameTrain, String tagFileNameTrain,
                                                String sentenceFileNameTest, String tagFileNameTest) throws IOException{
        // Gets data from files
        List<String[]> trainSentences = getSentencesFromFile(sentenceFileNameTrain);
        List<String[]> trainTags = getSentencesFromFile(tagFileNameTrain);
        List<String[]> testSentences = getSentencesFromFile(sentenceFileNameTest);
        List<String[]> testTags = getSentencesFromFile(tagFileNameTest);

        // Trains model
        HMM markovModel = new HMM();
        markovModel.trainModel(trainTags, trainSentences);

        // Stores failed and total tests
        int incorrect = 0;
        int total = 0;

        // Checks file validity
        if (testSentences.size() != testTags.size()){
            System.err.println("Input files don't match");
            return;
        }

        // Loops through sentences and tags them. If the tags created by Viterbi
        // Don't match the tags provided in the testing data, marks test as incorrect
        for (int i = 0; i < testSentences.size(); i++){
            String[] sentence = testSentences.get(i);
            String[] expectedTags = testTags.get(i);

            List<String> viterbiTags = Viterbi.tagSentence(sentence, markovModel, -100);
            if (viterbiTags.size() != expectedTags.length){
                System.err.println("Viterbi model failed: invalid output");
                return;
            }
            for (int j = 0; j < viterbiTags.size(); j++){
                if (!expectedTags[j].equalsIgnoreCase(viterbiTags.get(j))){
                    incorrect++;
                }
                total ++;
            }
        }

        // Outputs error message for failed tests
        if (incorrect > 0){
            System.err.println(incorrect + " tests failed out of " + total + " total tests");
        }
        else{
            System.out.println("All " + total + " tests passed");
        }
    }

    /**
     * Gets input from console
     * @param scanner
     * @param prompt
     * @return
     */
    public static String[] getInputLine(Scanner scanner, String prompt){
        System.out.println(prompt);
        String input = scanner.nextLine();

        return input.split(" ");
    }

    /**
     * Tags a sentence inputted by user from the console and prints the tags
     * @param trainingSentencesFile
     * @param trainingTagsFile
     * @param scanner
     * @throws IOException
     */
    public static void testUsingConsole(String trainingSentencesFile, String trainingTagsFile, Scanner scanner) throws IOException {
        List<String[]> trainSentences = getSentencesFromFile(trainingSentencesFile);
        List<String[]> trainTags = getSentencesFromFile(trainingTagsFile);

        HMM markov = new HMM();
        markov.trainModel(trainTags, trainSentences);

        String [] inputLine = getInputLine(scanner, "Enter sentence to tag (type empty string to quit): ");
        while (inputLine.length > 1){
            System.out.println(Viterbi.tagSentence(inputLine, markov, -10));
            inputLine = getInputLine(scanner, "Enter sentence to tag (type empty string to quit): ");
        }
    }

    // Runs the tests
    public static void main(String[] args){
        try {
            System.out.println("Testing on simple example file:");
            evaluateViterbiFromFiles("inputs/simple-train-sentences.txt", "inputs/simple-train-tags.txt", "" +
                    "inputs/simple-test-sentences.txt", "inputs/simple-test-tags.txt");
            System.out.println("Testing on complex example file:");
            evaluateViterbiFromFiles("inputs/brown-train-sentences.txt", "inputs/brown-train-tags.txt", "" +
                    "inputs/brown-test-sentences.txt", "inputs/brown-test-tags.txt");
            testUsingConsole("inputs/brown-train-sentences.txt", "inputs/brown-train-tags.txt", new Scanner(System.in));
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
