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

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashMap;

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

        var count = nouns.length;

        // Calculate number of unique permutations
        var numberOfPermutations = 0;
        for (var n = 1; n < count; n++) {
            numberOfPermutations += n;
        }

        // Get unique permutations (get matrix and take all elements from the below of diagonal)
        var permutations = new String[numberOfPermutations][2];
        var maxIndex = count - 1;
        var k = 0;
        for (var j = count - 1; j > 0; j--) {
            for (var i = 0; i < maxIndex; i++) {
                permutations[k][0] = nouns[i];
                permutations[k++][1] = nouns[j];
            }
            maxIndex--;
        }

        // Calculate distances for unique permutations
        var dict = new HashMap<String, Integer>();
        for (var p : permutations) {
            var distance = this.wordnet.distance(p[0], p[1]);
            dict.putIfAbsent(p[0] + p[1], distance);
            dict.putIfAbsent(p[1] + p[0], distance);
        }

        // Find outcast
        var maxDistance = -1;
        var outcast = "";
        for (var n1 : nouns) {
            var distanceSum = 0;
            for (var n2 : nouns) {
                if (!n1.equals(n2)) {
                    distanceSum += dict.get(n1 + n2);
                }
            }
            if (maxDistance == -1 || distanceSum > maxDistance) {
                maxDistance = distanceSum;
                outcast = n1;
            }
        }

        return outcast;
    }

    public static void main(String[] args) {
        var arguments = new String[]{"synsets.txt", "hypernyms.txt ", "outcast5.txt"};
        WordNet wordnet = new WordNet(arguments[0], arguments[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < arguments.length; t++) {
            In in = new In(arguments[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(arguments[t] + ": " + outcast.outcast(nouns));
        }
    }
}