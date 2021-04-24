import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Object class whose instances store Hidden Markov Models using maps.
 * Contains functions for creating and training the model.
 */
public class HMM {

    /**
     * Transitions from state to state within the model are stored
     * as maps between each state and another map which contains the states
     * it transitions to as well as the scores associated with the transitions.
     *
     * Scores for each word are stored in a map between the state in which
     * they appear and another map which contains the words for that state and their scores
     */
    private Map<String, Map<String, Double>> transitions;
    private Map<String, Map<String, Double>> wordsByPOS;

    /**
     * Basic constructor - initializes empty maps
     */
    public HMM(){
        this.transitions = new HashMap<>();
        this.wordsByPOS = new HashMap<>();
    }

    /**
     * Testing constructor - initializes model using prebuilt model stored in some file
     * @param filename
     */
    public HMM(String filename){
        transitions = new HashMap<>();
        wordsByPOS = new HashMap<>();
        try{
            POSLib.loadData(filename, transitions, wordsByPOS);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Getter - returns transition map
     * @return
     */
    public Map<String, Map<String, Double>> getTransitions(){
        return transitions;
    }

    /**
     * Getter - returns words map
     * @return
     */
    public Map<String, Map<String, Double>> getWordsByPOS(){
        return wordsByPOS;
    }

    /**
     * Unrolls a list of string arrays into a big list of strings
     * @param sentences
     * @return
     */
    public List<String> unrollSentences(List<String[]> sentences){
        List<String> output = new ArrayList<String>();
        for (String[] sentence: sentences){
            for (String word: sentence){
                output.add(word);
            }
        }
        return output;
    }

    /**
     * A list of strings to a lowercase list
     * @param input
     * @return
     */
    public List<String> convertWordsToLowercase(List<String> input){
        List<String> output = new ArrayList<String>();
        for (String word: input){
            output.add(word.toLowerCase());
        }
        return output;
    }

    /**
     * Trains/Builds the words map for the model based on training lists of words and their corresponding tags
     * @param tags
     * @param words
     */
    public void trainWords(List<String> tags, List<String> words){
        // Converts words to lowercase
        words = convertWordsToLowercase(words);

        // Checks to make sure each word has a tag
        if (tags.size() < 1 || tags.size() != words.size()){
            System.err.println("Invalid training data");
            return;
        }

        // Maps the number of occurrences of each word in each tag
        for (int i = 0; i < tags.size(); i++){
            if (!wordsByPOS.containsKey(tags.get(i))){
                wordsByPOS.put(tags.get(i), new HashMap<>());
            }

            Map<String, Double> wordMap = wordsByPOS.get(tags.get(i));
            if (!wordMap.containsKey(words.get(i))){
                wordMap.put(words.get(i), 0.0);
            }
            wordMap.put(words.get(i), (wordMap.get(words.get(i))+1));
        }

        // Converts frequencies to log scores
        for (String partOfSpeech: wordsByPOS.keySet()){
            double total = 0;
            for (String word: wordsByPOS.get(partOfSpeech).keySet()){
                total += wordsByPOS.get(partOfSpeech).get(word);
            }

            for (String word: wordsByPOS.get(partOfSpeech).keySet()){
                wordsByPOS.get(partOfSpeech).put(word, Math.log(wordsByPOS.get(partOfSpeech).get(word)/total));
            }
        }
    }


    /**
     * Trains the transitions map for the model based on training tags broken down into sentences
     * @param tags
     */
    public void trainTransitions(List<String[]> tags){
        // Populates transitions map based on the frequency of each transition
        for (String[] sentenceTags: tags){
            String previous = "#";
            for (String tag: sentenceTags){
                if (!transitions.containsKey(previous)){
                    transitions.put(previous, new HashMap<>());
                }

                Map<String, Double> transition = transitions.get(previous);
                if (!transition.containsKey(tag)){
                    transition.put(tag, 0.0);
                }
                transition.put(tag, transition.get(tag)+1);
                previous = tag;
            }
        }

        // Converts frequencies to log scores
        for (String tag: transitions.keySet()){
            double total = 0;
            for (String key : transitions.get(tag).keySet()) {
                total += transitions.get(tag).get(key);
            }

            for (String key : transitions.get(tag).keySet()) {
                transitions.get(tag).put(key, Math.log(transitions.get(tag).get(key) / total));
            }
        }
    }

    /**
     * Trains the model from a list of sentences and their corresponding POS tags
     * using the trainWords and trainTransitions
     * @param tags
     * @param sentences
     */
    public void trainModel(List<String[]> tags, List<String[]> sentences){
        List<String> unrolledTags = unrollSentences(tags);
        List<String> words = unrollSentences(sentences);
        trainWords(unrolledTags, words);
        trainTransitions(tags);
    }

    /**
     * Prints model trained using introductory data from problem set description
     */
    public void test(){
        List<String[]> tags = new ArrayList<String[]>();
        List<String[]> sentences = new ArrayList<String[]>();
        try {
            POSLib.loadTaggedSentences("inputs/cs10corpus.txt", sentences, tags);
        }
        catch(Exception e){
            System.err.println(e.getMessage());
        }

        trainModel(tags, sentences);

        System.out.println(this.transitions);
        System.out.println(this.wordsByPOS);
    }

    // Runs tests on a sample model
    public static void main(String[] args){
        HMM markovModel = new HMM();
        markovModel.test();
    }

}
