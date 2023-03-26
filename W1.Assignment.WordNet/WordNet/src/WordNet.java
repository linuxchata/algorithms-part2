/*----------------------------------------------------------------
 *  Author:        Pylyp Lebediev
 *  Written:       23/03/2023
 *  Last updated:  26/03/2023
 *
 *  Compilation:   javac WordNet.java
 *  Execution:     java WordNet
 *
 *  Immutable data type WordNet
 *
 *----------------------------------------------------------------*/

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class WordNet {

    private final String[] nouns;
    private final Digraph digraph;
    private final SAP sap;

    /**
     * Constructor takes the name of the two input files
     */
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null) {
            throw new IllegalArgumentException("synsets file is not specified");
        }
        if (hypernyms == null) {
            throw new IllegalArgumentException("hypernyms file is not specified");
        }

        // Handle synsets
        var inSynsets = new In(synsets);
        var synsetsString = inSynsets.readAllLines();

        var count = synsetsString.length;

        this.digraph = new Digraph(count);
        this.nouns = new String[count];

        for (var synset : synsetsString) {
            var result = synset.split(",");
            var id = Integer.parseInt(result[0]);
            this.nouns[id] = result[1];
        }

        // Handle hypernyms
        var inHypernyms = new In(hypernyms);
        var hypernymsString = inHypernyms.readAllLines();

        var root = -1;
        for (var hypernym : hypernymsString) {
            var result = hypernym.split(",");
            var v = Integer.parseInt(result[0]);
            if (result.length == 1) {
                root = v;
            } else {
                for (int i = 1; i < result.length; i++) {
                    var w = Integer.parseInt(result[i]);
                    digraph.addEdge(v, w);
                }
            }
        }

        if (root == -1) {
            throw new IllegalArgumentException("The input does not correspond to a rooted DAG.");
        }

        this.sap = new SAP(this.digraph);
    }

    /**
     * Returns all WordNet nouns
     */
    public Iterable<String> nouns() {
        var arrayList = new ArrayList<String>();
        for (var n : nouns) {
            String[] split = n.split("\\s+");
            arrayList.addAll(Arrays.asList(split));
        }

        return arrayList;
    }

    /**
     * Is the word a WordNet noun?
     */
    public boolean isNoun(String word) {
        for (var n : nouns) {
            if (n != null && isMatchWord(n, word)) {
                return true;
            }
        }
        return false;
    }

    private boolean isMatchWord(String synset, String word) {
        var split = synset.split("\\s+");
        for (var s : split) {
            if (word.equals(s)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Distance between nounA and nounB
     */
    public int distance(String nounA, String nounB) {
        var ids = getNounsIdentifiers(nounA, nounB);
        var minDistance = this.sap.length(ids[0], ids[1]);
        return minDistance;
    }

    /**
     * A synset (second field of synsets.txt) that is the common ancestor
     * of nounA and nounB in a shortest ancestral path
     */
    public String sap(String nounA, String nounB) {
        var ids = getNounsIdentifiers(nounA, nounB);
        var commonAncestorId = this.sap.ancestor(ids[0], ids[1]);
        return nouns[commonAncestorId];
    }

    private ArrayList<Integer>[] getNounsIdentifiers(String nounA, String nounB) {
        // Validate input
        if (nounA == null) {
            throw new IllegalArgumentException("Noun A is not a noun");
        }

        if (nounB == null) {
            throw new IllegalArgumentException("Noun B is not a noun");
        }

        // Get nouns identifiers
        var nounAIds = new ArrayList<Integer>();
        var nounBIds = new ArrayList<Integer>();
        for (var i = 0; i < this.digraph.V(); i++) {
            var n = nouns[i];
            if (isMatchWord(n, nounA)) {
                nounAIds.add(i);
            }
            if (isMatchWord(n, nounB)) {
                nounBIds.add(i);
            }
        }

        // Validate nouns identifiers
        if (nounAIds.isEmpty()) {
            throw new IllegalArgumentException(String.format("%s is not a noun (ids validation)", nounA));
        }

        if (nounBIds.isEmpty()) {
            throw new IllegalArgumentException(String.format("%s is not a noun (ids validation)", nounB));
        }

        return new ArrayList[]{nounAIds, nounBIds};
    }

    public static void main(String[] args) {
        var wordnet = new WordNet("synsets100-subgraph.txt", "hypernyms100-subgraph.txt");
        var nounA = "gamma_globulin";
        var nounB = "thing";
        StdOut.println("Noun A is " + nounA + ". Noun B is " + nounB);
        StdOut.println("nounA is noun - " + wordnet.isNoun(nounA) + ". nounB is noun - " + wordnet.isNoun(nounB));
        StdOut.println("Shortest ancestral path is " + wordnet.distance(nounA, nounB));
        StdOut.println("Shortest common ancestor is " + wordnet.sap(nounA, nounB));
    }
}