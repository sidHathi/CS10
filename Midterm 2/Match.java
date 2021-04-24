import java.util.ArrayList;
import java.util.List;

public class Match {
    String team1, team2;        // the names of the teams playing in this match
    int score1, score2;         // the scores of team1 and 2, respectively
    Match prev1, prev2;         // the previous matches that team1 and 2, respectively, played to get here;
                                //    or both null if this is a leaf

    public int numWins(String team) {
        // TODO: your code here

        // If the team hasn't played any matches, it's only match is the current one -
        // so it returns 1 if the current match is a win and 0 otherwise
        // If the team has played a previous match, its numWins is the number of wins from
        //  the previous match plus either 1 or 0 depending on the outcome of the current match.
        if (team.equals(team1)){
            if (prev1 == null){
                if (score1 > score2) return 1;
                else return 0;
            }
            if (score1 > score2) return 1 + prev1.numWins(team);
            else return prev1.numWins(team);
        }
        else if (team.equals(team2)){
            if (prev2 == null){
                if (score2 > score1) return 1;
                else return 0;
            }
            if (score2 > score1) return 1 + prev2.numWins(team);
            else return prev2.numWins(team);
        }

        return 0; // returns zero if input is invalid
    }

    public List<List<String>> teamsByLevel() {
        // TODO: your code here

        List<List<String>> teamsByLLevelList = new ArrayList<List<String>>();

        // uses recursively operating helper method to populate the list
        addTeams(teamsByLLevelList, 0);

        return teamsByLLevelList;
    }

    /**
     * helper method for teamsByLevel - populates the teamsByLevel list
     * @param teamsByLevel
     * @param level
     */
    public void addTeams(List<List<String>> teamsByLevel, int level){
        // if the list has no entries for the current level, initialize a new list at the index of level
        if (teamsByLevel.size() <= level){
            teamsByLevel.add(new ArrayList<String>());
        }
        // add the teams in the current match to the list corresponding to the current level
        teamsByLevel.get(level).add(team1);
        teamsByLevel.get(level).add(team2);

        // If previous matches exist, add their teams at the next level down.
        if (prev1 != null){
            prev1.addTeams(teamsByLevel, level + 1);
        }
        if (prev2 != null){
            prev2.addTeams(teamsByLevel, level + 1);
        }
    }
}
