public class Team {

    private String mascotName;
    private int currentScore;

    public Team(String mascotName){
        this.mascotName = mascotName;
        this.currentScore = 0;
    }

    public String getMascotName(){
        return this.mascotName;
    }

    public int getCurrentScore(){
        return this.currentScore;
    }

    public void score(){
        this.currentScore += 2;
    }

    public static void main(String[] args){
        Team team1 = new Team("team1");
        Team team2 = new Team("team2");
        team1.score();
        team2.score();
        team2.score();
        if (team1.getCurrentScore() > team2.getCurrentScore()){
            System.out.println(team1.getMascotName());
        }
        else if (team2.getCurrentScore() > team1.getCurrentScore()){
            System.out.println(team2.getMascotName());
        }
        else{
            System.out.println("Tie");
        }
    }

}
