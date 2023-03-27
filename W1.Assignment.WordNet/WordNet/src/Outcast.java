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

    private final WordNet wordnet;

    /**
     * Constructor takes a WordNet object
     */
    public Outcast(WordNet wordnet) {
        if (wordnet == null) {
            throw new IllegalArgumentException("WordNet is null");
        }

        this.wordnet = wordnet;
    }

    /**
     * Given an array of WordNet nouns, return an outcast
     */
    public String outcast(String[] nouns) {
        if (nouns == null) {
            throw new IllegalArgumentException("nouns is null");
        }

        // Find outcast
        var maxDistance = -1;
        var outcast = "";
        for (var n1 : nouns) {
            var distanceSum = 0;
            for (var n2 : nouns) {
                if (!n1.equals(n2)) {
                    distanceSum += this.wordnet.distance(n1, n2);
                }
            }

            if (distanceSum > maxDistance) {
                maxDistance = distanceSum;
                outcast = n1;
            }
        }

        return outcast;
    }
}