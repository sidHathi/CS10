import java.util.*;
public class Quest {
    // TODO: your code here
    // Instance variable that stores the current state
    private String currentState;
    // Map that stores each state's actions and the corresponding state given by those actions
    private Map<String, Map<String, String>> stateMap;
    // Stack that stores the previous states of the game
    private Stack<String> previousStates;

    public String getAction(String state) {
        // Assume this returns a valid action for the given state
        return "hello";
    }

    public void play(String start) {
        // TODO: your code here

        // Game continues so long as the current state is in the stateMap, its value isn't null, and it has actions
        while (stateMap.containsKey(currentState) && (stateMap.get(currentState) != null) && (stateMap.get(currentState).size() > 0)){
            // gets the action
            String action = getAction(currentState);

            if (action != "undo") {
                // If the action isn't undo, update the previousStates stack and update the current state based on the
                // state value stored in stateMap under the currentState's given action
                if (previousStates == null) {
                    previousStates = new Stack<String>();
                }
                previousStates.push(currentState);
                currentState = stateMap.get(currentState).get(action);
            }
            else{
                // If the action is undo, revert to the previous state if one is stored.
                if (previousStates != null && previousStates.size() > 0){
                    currentState = previousStates.pop();
                }
            }
        }

        // return when the game ends
        return;

    }
}
