import java.util.*;

public class Mapwork {

    public static void main(String[] args){
        Map<String, ArrayList<String>> courseList = new TreeMap<>();

        courseList.put("Sid", new ArrayList<String>());
        courseList.get("Sid").add("CS 10");

        System.out.println(courseList);
    }
}
