import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Object Class: Contains code needed to run the Kevin Bacon game given inputs found in some bacon/ directory
 */
public class BaconGame {

    // Instance variables
    private String root;
    private Scanner scanner;
    private Graph<String, Set<String>> baconGraph;
    private Graph<String, Set<String>> pathTree;

    /**
     * Constructor - instantiates new instance of the game and builds corresponding graphs
     * @param scanner
     * @param root
     * @param actorsPath
     * @param moviesPath
     * @param moviesActorsPath
     */
    public BaconGame(Scanner scanner, String root, String actorsPath, String moviesPath, String moviesActorsPath){
        this.scanner = scanner;
        this.root = root;
        this.baconGraph = new AdjacencyMapGraph<>();
        try {
            initializeGraph(actorsPath, moviesPath, moviesActorsPath);
            configurePathTree();
        }
        catch (IOException e){
            System.err.println("Unable to read file");
        }
    }

    /**
     * Sets the root node to a new vertex and rebuilds the pathTree graph
     * @param newRoot
     */
    public void setNewUniverseCenter(String newRoot){
        if (baconGraph.hasVertex(newRoot)){
            root = newRoot;
            pathTree = PS4GraphLib.bfs(baconGraph, newRoot);
            dispUniverseCenterMessage();
        }
        else{
            System.err.println("Invalid Input");
        }
    }

    /**
     * Gets the average separation from root for some possible root vertex
     * @param newRoot
     * @return
     */
    public double getAverageSeparationForRoot(String newRoot){
        Graph<String, Set<String>> tempPathTree = PS4GraphLib.bfs(baconGraph, newRoot);
        return PS4GraphLib.averageSeparation(tempPathTree, newRoot);
    }

    /**
     * Displays vertices in sorted order based on far everything else is from them
     * @param indices
     */
    public void dispSortedUniverseCenters(int indices){
        class CenterComparator implements Comparator<String>{
            @Override
            public int compare(String actor1, String actor2) {
                if (indices > 0){
                    return (int)(Math.signum(getAverageSeparationForRoot(actor1) - getAverageSeparationForRoot(actor2)));

                }else{
                    return (int)(Math.signum(getAverageSeparationForRoot(actor2) - getAverageSeparationForRoot(actor1)));
                }
            }
        }

        int indicesMagnitude = Math.abs(indices);
        CenterComparator centerComparator = new CenterComparator();
        PriorityQueue<String> sortedUniverseCenters = new PriorityQueue<String>(centerComparator);
        for (String vertex: baconGraph.vertices()){
            sortedUniverseCenters.add(vertex);
        }

        List<String> sortedCentersList = new ArrayList<String>();
        while (!sortedUniverseCenters.isEmpty() && indicesMagnitude > 0){
            sortedCentersList.add(sortedUniverseCenters.poll());
            indicesMagnitude --;
        }

        System.out.println(sortedCentersList);
    }

    /**
     * Displays vertices in order of how far they are from the current root
     * @param low
     * @param high
     */
    public void dispSortedBySeparation(int low, int high){
        class SeparationComparator implements Comparator<String>{
            public int compare(String actor1, String actor2) {
                return PS4GraphLib.getPath(pathTree, actor1).size() - PS4GraphLib.getPath(pathTree, actor2).size();
            }
        }

        SeparationComparator separationComparator = new SeparationComparator();
        PriorityQueue<String> sortedBySeparation = new PriorityQueue<String>(separationComparator);
        for (String vertex: pathTree.vertices()){
            if (PS4GraphLib.getPath(pathTree, vertex).size() > low && PS4GraphLib.getPath(pathTree, vertex).size() <= high){
                sortedBySeparation.add(vertex);
            }
        }

        List<String> sortedBySeparationList = new ArrayList<String>();
        while(!sortedBySeparation.isEmpty()){
            sortedBySeparationList.add(sortedBySeparation.poll());
        }

        System.out.println(sortedBySeparationList);
    }

    /**
     * Displays all values not connected to root
     */
    public void dispInfinitelySeparated(){
        System.out.println(PS4GraphLib.missingVertices(baconGraph, pathTree));
    }

    /**
     * Displays actors sorted by how many other actors they're connected with
     * @param low
     * @param high
     */
    public void dispActorsSortedByDegree(int low, int high){
        class VertextComparator implements Comparator<String>{
            public int compare(String actor1, String actor2) {
                return (baconGraph.outDegree(actor1)) - (baconGraph.outDegree(actor2));
            }
        }

        VertextComparator vComparator = new VertextComparator();
        PriorityQueue<String> sortedByDegree = new PriorityQueue<String>(vComparator);
        for (String vertex: baconGraph.vertices()){
            if (baconGraph.outDegree(vertex)  >= low && baconGraph.outDegree(vertex) <= high){
                sortedByDegree.add(vertex);
            }
        }

        List<String> sortedList = new ArrayList<String>();
        while (!sortedByDegree.isEmpty()){
            sortedList.add(sortedByDegree.poll());
        }

        System.out.println(sortedList);
    }

    /**
     * Displays information for a current actor and their path to the root actor
     * @param actor
     */
    public void dispActorInfo(String actor){
        if (baconGraph.hasVertex(actor) && pathTree.hasVertex(actor)){
            List<String> shortestPath = PS4GraphLib.getPath(pathTree, actor);

            System.out.println(actor + "'s number is " + (shortestPath.size()-1));
            String currentActor = actor;
            for (String actor2: shortestPath){
                if(actor != actor2) {
                    System.out.println(currentActor + " appeared in " + baconGraph.getLabel(currentActor, actor2) + " with " + actor2);
                    currentActor = actor2;
                }
            }
        }
        else{
            System.err.println(actor + "'s number is infinity");
        }
    }

    /**
     * Displays message detailing the actor currently at the center of the universe
     */
    public void dispUniverseCenterMessage(){
        System.out.println(root + " is now the center of the acting universe, connected to " + pathTree.numVertices() + "/" + baconGraph.numVertices() +
                " actors with average separation " + PS4GraphLib.averageSeparation(pathTree, root));
    }

    /**
     * Builds the path tree using PS4 GraphLib functions
     */
    public void configurePathTree(){
        pathTree = PS4GraphLib.bfs(baconGraph, root);
    }

    /**
     * Initializes overarching baconGraph from input files
     * @param actorsPath
     * @param moviesPath
     * @param moviesActorsPath
     * @throws IOException
     */
    public void initializeGraph(String actorsPath, String moviesPath, String moviesActorsPath) throws IOException {
        Map<Integer, String> actorMap = new HashMap<>();
        Map<Integer, String> movieMap = new HashMap<>();
        Map<Integer, List<Integer>> movieActorMap = new HashMap<>();

        BufferedReader actorsInput = new BufferedReader(new FileReader(actorsPath));
        BufferedReader moviesInput = new BufferedReader(new FileReader(moviesPath));
        BufferedReader actorsMoviesInput = new BufferedReader(new FileReader(moviesActorsPath));

        String actorLine = actorsInput.readLine();
        while (actorLine != null){
            String[] twoLine = actorLine.split("\\|");
            if (twoLine.length > 0){
                actorMap.put(Integer.parseInt(twoLine[0]), twoLine[1]);
            }
            actorLine = actorsInput.readLine();
        }
        actorsInput.close();

        String movieLine = moviesInput.readLine();
        while (movieLine != null){
            String[] twoLine = movieLine.split("\\|");
            if (twoLine.length > 0){
                movieMap.put(Integer.parseInt(twoLine[0]), twoLine[1]);
            }
            movieLine = moviesInput.readLine();
        }
        moviesInput.close();

        String actorMovieLine = actorsMoviesInput.readLine();
        while (actorMovieLine != null){
            String[] twoLine = actorMovieLine.split("\\|");
            if (twoLine.length > 0){
                if(!movieActorMap.containsKey(Integer.valueOf(twoLine[0]))){
                    movieActorMap.put(Integer.valueOf(twoLine[0]), new ArrayList<Integer>());
                }
                movieActorMap.get(Integer.valueOf(twoLine[0])).add(Integer.valueOf(twoLine[1]));
            }
            actorMovieLine = actorsMoviesInput.readLine();
        }
        actorsMoviesInput.close();

        for (Integer actorKey: actorMap.keySet()){
            if (actorMap.get(actorKey) != null){
                baconGraph.insertVertex(actorMap.get(actorKey));
            }
        }

        for (Integer movieKey: movieActorMap.keySet()){
            String movie = movieMap.get(movieKey);
            for (Integer a1: movieActorMap.get(movieKey)){
                for (Integer a2: movieActorMap.get(movieKey)){
                    String actor1 = actorMap.get(a1);
                    String actor2 = actorMap.get(a2);
                    if (a1 != a2 && !baconGraph.hasEdge(actor1, actor2)){
                        Set<String> edgeSet = new HashSet<String>();
                        edgeSet.add(movie);
                        baconGraph.insertUndirected(actor1, actor2, edgeSet);
                    }
                    else if(baconGraph.hasEdge(actor1, actor2) && !baconGraph.getLabel(actor1, actor2).contains(movie)){
                        baconGraph.getLabel(actor1, actor2).add(movie);
                    }
                }
            }
        }
    }

    /**
     * Prints pre-game information
     */
    public void printCommandInfo(){
        System.out.println("Commands:");
        System.out.println("c <#>: list top (positive number) or bottom (negative) <#> centers of the universe, sorted by average separation");
        System.out.println("d <low> <high>: list actors sorted by degree, with degree between low and high");
        System.out.println("i: list actors with infinite separation from the current center");
        System.out.println("p <name>: find path from <name> to current center of the universe");
        System.out.println("s <low> <high>: list actors sorted by non-infinite separation from the current center, with separation between low and high");
        System.out.println("u <name>: make <name> the center of the universe");
        System.out.println("q: quit game");
        System.out.println();
        dispUniverseCenterMessage();
    }

    /**
     * Gets user input and returns it as a string
     * @param prompt
     * @return
     */
    public String getInput(String prompt){
        System.out.println(prompt);
        String input = scanner.nextLine();

        return input;
    }

    /**
     * Uses game rules to call other functions in class depending on user's input
     * @param input
     * @return
     */
    public boolean handleInput(String input){
        if (input == null || input.length() < 1){
            System.out.println("Invalid input");
            return true;
        }

        String[] splitString = input.split(" ");
        if (splitString.length < 1){
            System.out.println("Invalid input");
            return true;
        }

        if (splitString[0].equalsIgnoreCase("q")){
            return false;
        }
        else if (splitString[0].equalsIgnoreCase("i")){
            dispInfinitelySeparated();
            return true;
        }
        else if (splitString.length < 2){
            System.out.println("Invalid input");
            return true;
        }


        String[] inputArray = Arrays.copyOfRange(splitString, 1, splitString.length);
        String proccessedInput = String.join(" ", inputArray);
        if (splitString[0].equalsIgnoreCase("u")){
            if (baconGraph.hasVertex(proccessedInput)){
                setNewUniverseCenter(proccessedInput);
            }
            else{
                System.out.println("Invalid input");
            }
        }
        else if (splitString[0].equalsIgnoreCase("p")){
            if (baconGraph.hasVertex(proccessedInput)){
                dispActorInfo(proccessedInput);
            }
            else{
                System.out.println("Invalid input");
            }
        }
        else if (splitString[0].equalsIgnoreCase("c")){
            try{
                Integer integerInput = Integer.parseInt(splitString[1]);
                dispSortedUniverseCenters(integerInput);
            }
            catch (Exception e){
                System.out.println("Invalid input");
            }
        }
        else if (splitString[0].equalsIgnoreCase("d")){
            if (splitString.length < 3){
                System.out.println("Invalid input");
                return true;
            }
            try{
                Integer low = Integer.parseInt(splitString[1]);
                Integer high = Integer.parseInt(splitString[2]);
                dispActorsSortedByDegree(low ,high);
            }
            catch(Exception e){
                System.out.println("Invalid input");
            }
        }
        else if (splitString[0].equalsIgnoreCase("s")){
            if (splitString.length < 3){
                System.out.println("Invalid input");
                return true;
            }
            try{
                Integer low = Integer.parseInt(splitString[1]);
                Integer high = Integer.parseInt(splitString[2]);
                dispSortedBySeparation(low, high);
            }
            catch(Exception e){
                System.out.println("Invalid input");
            }
        }

        return true;
    }

    /**
     * Starts the game
     */
    public void initializeGame(){
        printCommandInfo();
        String input = getInput("Kevin Bacon game >");
        boolean validInput = handleInput(input);
        while (validInput){
            input = getInput("Kevin Bacon game >");
            validInput = handleInput(input);
        }
    }


    /**
     * Main method - runs the code
     * @param args
     */
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);

        String actorPathTest = "bacon/actorsTest.txt";
        String moviesPathTest = "bacon/moviesTest.txt";
        String movieActorPathTest = "bacon/movie-actorsTest.txt";

        String actorPath = "bacon/actors.txt";
        String moviesPath = "bacon/movies.txt";
        String movieActorPath = "bacon/movie-actors.txt";

        //BaconGame game = new BaconGame(scanner, "Kevin Bacon", actorPath, moviesPath, movieActorPath);
        BaconGame game = new BaconGame(scanner, "Kevin Bacon", actorPathTest, moviesPathTest, movieActorPathTest);

        game.initializeGame();

    }

}
