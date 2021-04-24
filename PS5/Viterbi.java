import java.util.*;

/**
 * Class that contains static methods for performing Viterbi traversal
 * of a Hidden Markov Model to tag words in a sentence by their parts of speech.
 */
public class Viterbi {

    /**
     * Converts a String array to lowercase
     * @param raw
     */
    public static void convertToLowercase(String[] raw){
        if(raw != null && raw.length > 0){
            for (int i = 0; i < raw.length; i++){
                raw[i] = raw[i].toLowerCase();
            }
        }
    }

    /**
     * Main Viterbi function: takes in a markov model and a predetermined penalty
     * for unseen words to tag an input sentence.
     * @param sentence
     * @param markov
     * @param unseenPenalty
     * @return
     */
    public static List<String> tagSentence(String[] sentence, HMM markov, double unseenPenalty){
        // Checks that the sentence is valid
        if (sentence == null || sentence.length < 1){
            System.err.println("Invalid input");
            return null;
        }

        // Converts sentence to lowercase
        convertToLowercase(sentence);

        // List of Maps used for backtracing. Each map in the list corresponds to some word.
        // They map each state associated with the word to a map from the preceeding state
        // to the score of the transition.
        List<Map<String, Map<String, Double>>> backTraces = new ArrayList<Map<String, Map<String, Double>>>();
        // Stores the states from which the Viterbi is currently traversing
        Map<String, Double> currentStates = new HashMap<>();
        // Adds start to current states
        currentStates.put("#", 0.0);
        // Stores the best scoring tag in the last sentence
        String highscoringTag = null;
        // Stores the tag's score
        Double bestScore = null;
        // Loops through each word
        for (String word: sentence){
            // Resets best POS tag and its score
            bestScore = null;
            highscoringTag = null;
            // Map that stores possible next POS states from current states and the word
            Map<String, Double> nextStates = new HashMap<>();
            // Map that contains this words entry in the backTrace list
            Map<String, Map<String, Double>> backTrace = new HashMap<>();
            // Loops through all the current POS states associated with the previous word
            for (String state: currentStates.keySet()){
                // Gets the possible POS states associated with this word.
                Map<String, Double> possibleTransitions = markov.getTransitions().get(state);
                // Skips words with no possible parts of speech
                if (possibleTransitions == null){
                    continue;
                }
                // Loops through all the possible POS states for the word
                for (String nextState: possibleTransitions.keySet()){
                    // Scores the word based on the previous state and the transition score
                    double score = currentStates.get(state) + possibleTransitions.get(nextState);
                    // Adds the word score if it exists in the current next state or adds the unseen penalty
                    Map<String, Double> possibleWords = markov.getWordsByPOS().get(nextState);
                    if (possibleWords.containsKey(word)){
                        score += possibleWords.get(word);
                    }
                    else{
                        score += unseenPenalty;
                    }

                    // Adds POS state to the backtrace if its score for the word is the highest seen for that state
                    if (!nextStates.containsKey(nextState) || nextStates.get(nextState) < score){
                        nextStates.put(nextState, score);
                        backTrace.put(nextState, new HashMap<>());
                        backTrace.get(nextState).put(state, score);
                    }

                    // Updates best score for word when relevant
                    if (bestScore == null || score > bestScore){
                        bestScore = score;
                        highscoringTag = nextState;
                    }
                }
            }
            // Adds to backtrace
            backTraces.add(backTrace);
            // NextStates becomes currentStates
            currentStates = nextStates;
        }

        // Checks that the backtrace was populated
        if (backTraces.size() < 1){
            return null;
        }

        // Performs the backtrace from the last highScoring tag
        String trace = highscoringTag;
        Stack<String> stateStack = new Stack<String>();
        for (int i = backTraces.size() - 1; i >= 0; i--){
            stateStack.push(trace);
            trace = getKeyforMax(backTraces.get(i).get(trace));
        }

        List<String> tagList = new ArrayList<String>();
        while (!stateStack.isEmpty()){
            tagList.add(stateStack.pop());
        }

        // Returns list of tags from backtrace
        return tagList;
    }

    /**
     * Helper function for backtrace. Finds highest scoring state for each trace.
     * @param backTrace
     * @return
     */
    public static String getKeyforMax(Map<String, Double> backTrace){
        Double maxVal = null;
        String best = null;

        if (backTrace != null) {
            for (String trace : backTrace.keySet()) {
                if (maxVal == null || backTrace.get(trace) > maxVal) {
                    maxVal = backTrace.get(trace);
                    best = trace;
                }
            }
        }
        return best;
    }

    /**
     * Tests Viterbi functions on example from PS5 description
     */
    public static void test(){
        HMM testHMM = new HMM();
        testHMM.test();

        List<String[]> tags = new ArrayList<String[]>();
        List<String[]> sentences = new ArrayList<String[]>();
        try {
            POSLib.loadTaggedSentences("inputs/cs10corpus.txt", sentences, tags);
        }
        catch(Exception e){
            System.err.println(e.getMessage());
        }

        for (String[] sentence: sentences){
            System.out.println(tagSentence(sentence, testHMM, -100));
        }
    }

    // runs basic tests
    public static void main(String[] args){
        test();
    }

}
