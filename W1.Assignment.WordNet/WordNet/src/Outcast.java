/*----------------------------------------------------------------
 *  Author:        Pylyp Lebediev
 *  Written:       25/03/2023
 *  Last updated:  25/03/2023
 *
 *  Compilation:   javac Outcast.java
 *  Execution:     java Outcast
 *
 *  Outcast detection
 *
 *----------------------------------------------------------------*/

public class Outcast {

    /**
     * Constructor takes a WordNet object
     */
    public Outcast(WordNet wordnet) {
        if (wordnet == null) {
            throw new IllegalArgumentException("WordNet is null");
        }
    }

    /**
     * Given an array of WordNet nouns, return an outcast
     */
    public String outcast(String[] nouns) {
        if (nouns == null) {
            throw new IllegalArgumentException("nouns is null");
        }

        return "";
    }

    public static void main(String[] args) {
    }
}