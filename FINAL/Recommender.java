import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Recommender {
    public Map<String, Map<String, List<String>>> messages;  // from -> to -> [ message ] -- assume already initialized

    // declare friendStrength

    public Map<String, Map<String, Double>> friendStrength;

    /**
     * Based on messages, computes friendStrength as the log of the relative frequency of messages
     */
    public void computeFriendStrength() {

        // Loops through all the users
        for (String from: messages.keySet()){
            // Adds users to friendStrength
            if (!friendStrength.containsKey(from)){
                friendStrength.put(from, new HashMap<>());
            }
            int totalMessages = 0;

            // populates friendStrength with the number of occurences of each friend in the users' messages.
            for (String to: messages.get(from).keySet()){
                Double numMessages = Double.valueOf(messages.get(from).get(to).size());
                friendStrength.get(from).put(to, numMessages);
                // keeps track of total messages sent
                totalMessages += numMessages;
            }

            // Changes the values in friendStrength to log calculations based on totalMessages
            for (String friend: friendStrength.get(from).keySet()){
                friendStrength.get(from).put(friend, Math.log(friendStrength.get(from).get(friend)/totalMessages));
            }
        }

    }

    /**
     * Based on friendStrength, returns a list of suggested friends for person,
     * such that person has not yet sent any message to them,
     * ranked in order of total of friendStrength from person -> intermediate and intermediate -> suggested
     */
    public List<String> recommendations(String person) {

        if (!friendStrength.containsKey(person)){
            System.err.println("Person does not exist");
            return null;
        }

        // Maps every possible new friend to their best score
        Map<String, Double> suggestionsUnsorted = new HashMap<>();

        // For each of the persons friends
        for (String friend: friendStrength.get(person).keySet()){
            // Loop through all of their friends
            for (String friendsFriend: friendStrength.get(friend).keySet()){
                // If the person hasn't communicated with them
                if (!messages.get(person).containsKey(friendsFriend)){
                    // Evaluate the how good of a connection exists between the person and the possible new friend
                    Double score = friendStrength.get(person).get(friend) + friendStrength.get(friend).get(friendsFriend);
                    // If the evaluated score is better than any other score through other friends, add it to the map
                    if (!suggestionsUnsorted.containsKey(friendsFriend)){
                        suggestionsUnsorted.put(friendsFriend, score);
                    }
                    else{
                        if(score > suggestionsUnsorted.get(friendsFriend)){
                            suggestionsUnsorted.put(friendsFriend, score);
                        }
                    }
                }
            }
        }

        // Converts the map into the sorted list
        List<String> suggestionsSorted = new ArrayList<String>();
        while (suggestionsUnsorted.size() > 0){
            Double bestScore = 0.0;
            String bestSuggestion = null;
            for (String suggestion: suggestionsUnsorted.keySet()){
                if (suggestionsUnsorted.get(suggestion) > bestScore){
                    bestScore = suggestionsUnsorted.get(suggestion);
                    bestSuggestion = suggestion;
                }
            }
            suggestionsSorted.add(bestSuggestion);
            suggestionsUnsorted.remove(bestSuggestion);
        }

        return suggestionsSorted;
    }
}
