import java.util.*;

public class Splitter {
    public List<Thing> things;          // the Things being split
    public String property;             // one of the properties of the Things, used here to split them into those with and those without
    public Splitter with, without;      // Splitters for the subsets of Things with and without the property

    public Splitter(List<Thing> things) {
        this.things = things;
        chooseProperty();
        split();
    }

    public void chooseProperty() {
        // TODO: your code here

        // Map that stores will store the frequency of all the properties in all things
        Map<String, Integer> propertyMap = new HashMap<>();

        // Populates property map
        for (Thing thing: things){
            for (String property: thing.properties){
                if (propertyMap.containsKey(property)){
                    propertyMap.put(property, propertyMap.get(property) + 1);
                }
                else{
                    propertyMap.put(property, 1);
                }
            }
        }

        // Variables to store the closest property to a fifty fifty split and the euclidean distance
        // between the proportion of things that have the property and 0.5
        double bestEuclideanDistance = Double.POSITIVE_INFINITY;
        String bestProperty = "";

        // Loops through property map and updates bestEuclideanDistance and bestProperty if a given property has
        // a split closer to 50/50
        for (String property: propertyMap.keySet()){
            double euclideanDistance = Math.pow(((propertyMap.get(property) / things.size()) - 0.5), 2);
            if (euclideanDistance < bestEuclideanDistance){
                bestProperty = property;
                bestEuclideanDistance = euclideanDistance;
            }
        }

       // The property with the split closest to 50/50 is stored in the instance variable
        this.property = bestProperty;

    }

    public void split() {
        // TODO: your code here

        // Checks that the list has more than one item and a valid property
        if (this.property != null && this.things.size() > 1){
            // Initializes lists that will store the things with the property and those without it respectively
            List<Thing> withList = new ArrayList<Thing>();
            List<Thing> withoutList = new ArrayList<Thing>();

            // Loops through the things, sorting them into the lists based on whether they have the property or not
            for (Thing thing: this.things){
                if (thing.properties.contains(property)){
                    withList.add(thing);
                }
                else{
                    withoutList.add(thing);
                }
            }

            // Creates new splitters out of the populated lists and stores them in instance variables
            // there is no need to call split() on the new Splitters because split is called automatically in the constructor.
            this.with = new Splitter(withList);
            this.without = new Splitter(withoutList);
        }

    }
}
