import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

/**
 * Some methods that may be helpful for testing part-of-speech tagging via HMM for CS 10 PS-5
 *
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Winter 2021
 */
public class POSLib {
    public static boolean debug = true; // Set to true if you want print statements flying by

    /**
     * This method is incomplete, in that it loads the HMM data but doesn't do anything with it.
     * That's because I don't know how you've implemented your HMM, and didn't want to force a particular
     * data structure or way of dealing with the data.
     * So instead I just put some print statements.
     * If you want to use it, copy & paste this file into your own HMM code and add calls to actually create your data structure.
     *
     * File format (not robust at all to errors):
     * Observations
     * State1,Obs1a,Score1a,Obs1b,Score1b,...
     * State2,Obs2a,Score2a,Obs2b,Score2b,...
     * ...
     * Transitions
     * State1,ToState1a,Score1To1a,ToState1b,Score1To1b,...
     * State2,ToState2a,Score2To2a,ToState2b,Score2To2b,...
     */
    public static void loadData(String filename, Map<String, Map<String, Double>> transitionMap, Map<String, Map<String, Double>> wordMap) throws Exception {
        if (debug) System.out.println("loading data from "+filename);
        BufferedReader in = new BufferedReader(new FileReader(filename));
        String line;
        boolean gettingObservations = true;
        while ((line = in.readLine()) != null) {
            if (debug) System.out.println(line);
            if (line.equals("Observations")) gettingObservations = true;
            else if (line.equals("Transitions")) gettingObservations = false;
            else {
                String[] parts = line.split(",");
                if (parts.length==0) continue;
                String state = parts[0];
                for (int i=1; i<parts.length; i+=2) {
                    double score = Double.parseDouble(parts[i+1]);
                    if (gettingObservations) {
                        String word = parts[i];
                        System.out.println("observation "+state+" "+word+" "+score);
                        if (!wordMap.containsKey(state)){
                            wordMap.put(state, new HashMap<>());
                        }
                        wordMap.get(state).put(word, score);
                    }
                    else {
                        String next = parts[i];
                        System.out.println("transition "+state+" "+next+" "+score);
                        if (!transitionMap.containsKey(state)){
                            transitionMap.put(state, new HashMap<>());
                        }
                        transitionMap.get(state).put(next, score);
                    }
                }
            }
        }
        in.close();
    }

    /**
     * Loads the tagged sentences from filename into sentences and tags,
     * in parallel order (i.e., sentences.get(i) is tagged with tags.get(i)).
     * Each line is of the form word1/tag1 word2/tag2 ...
     * except that lines that are empty or start with "!" (a comment line) are ignored.
     * Not particularly robust; ignores tokens that don't take the form word/tag.
     *
     * @param filename input filename, in specified format
     * @param sentences a list into which the sentences are added
     * @param tags a list into which the tags are added
     * @throws Exception
     */
    public static void loadTaggedSentences(String filename, List<String[]> sentences, List<String[]> tags) throws Exception {
        if (debug) System.out.println("loading tagged sentences from "+filename);
        BufferedReader in = new BufferedReader(new FileReader(filename));
        String line;
        int lineNum = 0;
        while ((line = in.readLine()) != null) {
            if (debug) System.out.println(line);
            if (line.length()==0 || line.charAt(0)=='!') continue;
            String[] taggedWords = line.split(" ");
            if (taggedWords.length==0) continue;
            String[] sentence = new String[taggedWords.length];
            String[] tag = new String[taggedWords.length];
            sentences.add(sentence);
            tags.add(tag);
            int i = 0;
            for (String taggedWord : taggedWords) {
                String[] wordAndTag = taggedWord.split("/");
                if (wordAndTag.length!=2) {
                    System.err.println("bad token "+i+" on line "+lineNum+":"+line);
                }
                else {
                    sentence[i] = wordAndTag[0];
                    tag[i] = wordAndTag[1];
                    i++;
                }
            }
            lineNum++;
        }
    }

    public static void main(String[] args) throws Exception {
        ArrayList<String[]> sentences = new ArrayList<>();
        ArrayList<String[]> tags = new ArrayList<>();
        loadTaggedSentences("inputs/cs10corpus.txt",sentences,tags);
        for (int i=0; i<sentences.size(); i++) {
            System.out.println(Arrays.toString(sentences.get(i)) + " => " + Arrays.toString(tags.get(i)));
        }
    }
}
