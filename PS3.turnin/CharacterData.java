/**
 * Data storage class used for Huffman lossless compression.
 * Contains framework for storing key-value pairs of Characters and Integers.
 *
 * @author Siddharth Hathi
 */
public class CharacterData implements Comparable<CharacterData> {

    // Instance variables
    private Integer frequency;
    private Character character;

    // Constructor
    public CharacterData(Character character, Integer frequency){
        this.frequency = frequency;
        this.character = character;
    }

    // Getters
    public Character getCharacter(){
        return this.character;
    }
    public Integer getFrequency(){
        return frequency;
    }

    // Compares CharacterData instances based on their frequency values
    @Override
    public int compareTo(CharacterData o) {
        return this.frequency - o.frequency;
    }

    // Returns string representation of the key-value pair
    @Override
    public String toString(){
        return character + ": " + frequency;
    }
}
