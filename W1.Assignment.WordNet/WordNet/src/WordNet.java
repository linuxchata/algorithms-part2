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

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class WordNet {

    private final int count;
    private final Bag<Integer>[] adj;
    private final String[] nouns;

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

        this.count = synsetsString.length;
        this.adj = (Bag<Integer>[]) new Bag[this.count];
        this.nouns = new String[this.count];

        for (var synset : synsetsString) {
            var result = synset.split(",");
            var id = Integer.parseInt(result[0]);
            this.adj[id] = new Bag<Integer>();
            this.nouns[id] = result[1];
        }

        // Handle hypernyms
        var inHypernyms = new In(hypernyms);
        var hypernymsString = inHypernyms.readAllLines();

        var root = -1;
        for (var hypernym : hypernymsString) {
            var result = hypernym.split(",");
            if (result.length == 1) {
                root = Integer.parseInt(result[0]);
                continue;
            }
            var v = Integer.parseInt(result[0]);
            for (int i = 1; i < result.length; i++) {
                var w = Integer.parseInt(result[i]);
                this.adj[v].add(w);
            }
        }

        if (root == -1) {
            throw new IllegalArgumentException("The input does not correspond to a rooted DAG.");
        }
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

    private boolean isMatchWord(String synset, String word){
        String[] split = synset.split("\\s+");
        for (var s : split){
            if (word.equals(s)){
                return true;
            }
        }
        return false;
    }

    /**
     * Distance between nounA and nounB
     */
    public int distance(String nounA, String nounB) {
        var result = getCommonAncestorAndMinDist(nounA, nounB);
        return result[1];
    }

    /**
     * A synset (second field of synsets.txt) that is the common ancestor
     * of nounA and nounB in a shortest ancestral path
     */
    public String sap(String nounA, String nounB) {
        var result = getCommonAncestorAndMinDist(nounA, nounB);
        var commonAncestorId = result[0];
        return nouns[commonAncestorId];
    }

    private int[] getCommonAncestorAndMinDist(String nounA, String nounB) {
        var ids = getNounsIdentifiers(nounA, nounB);

        // Run breadth-first paths
        var nounABsp = bsp(ids[0]);
        var nounBBsp = bsp(ids[1]);

        // Get common ancestor and shortest distance
        var commonAncestor = -1;
        var minDist = -1;
        for (var i = 0; i < this.count; i++) {
            if (nounBBsp.marked[i] && nounABsp.marked[i]) {
                var dist = nounBBsp.distTo[i] + nounABsp.distTo[i];
                if (minDist == -1 || dist < minDist) {
                    minDist = dist;
                    commonAncestor = i;
                }
            }
        }

        return new int[]{commonAncestor, minDist};
    }

    private int[] getNounsIdentifiers(String nounA, String nounB) {
        // Validate input
        if (nounA == null) {
            throw new IllegalArgumentException("Noun A is not a noun");
        }

        if (nounB == null) {
            throw new IllegalArgumentException("Noun B is not a noun");
        }

        // Get nouns identifiers
        var nounAId = -1;
        var nounBId = -1;
        for (var i = 0; i < this.count; i++) {
            var n = nouns[i];
            if (nounAId == -1 && isMatchWord(n, nounA)) {
                nounAId = i;
            }
            if (nounBId == -1 && isMatchWord(n, nounB)) {
                nounBId = i;
            }
            if (nounAId != -1 && nounBId != -1) {
                break;
            }
        }

        // Validate nouns identifiers
        if (nounAId == -1) {
            throw new IllegalArgumentException(String.format("%s is not a noun", nounA));
        }

        if (nounBId == -1) {
            throw new IllegalArgumentException(String.format("%s is not a noun", nounB));
        }

        return new int[]{nounAId, nounBId};
    }

    private Bsp bsp(int s) {
        var distTo = new int[this.count];
        var marked = new boolean[this.count];

        var queue = new Queue<Integer>();
        queue.enqueue(s);
        marked[s] = true;

        while (!queue.isEmpty()) {
            var v = queue.dequeue();
            for (var w : getAdjacent(v)) {
                if (!marked[w]) {
                    queue.enqueue(w);
                    distTo[w] = distTo[v] + 1;
                    marked[w] = true;
                }
            }
        }

        return new Bsp(distTo, marked);
    }

    private Bag<Integer> getAdjacent(int v) {
        return adj[v];
    }

    private class Bsp {

        private final int[] distTo;
        private final boolean[] marked;

        Bsp(int[] distTo, boolean[] marked) {
            this.distTo = distTo;
            this.marked = marked;
        }
    }

    public static void main(String[] args) {
        var wordnet = new WordNet("synsets.txt", "hypernyms.txt");
        var nounA = "American_persimmon"; // prolamine
        var nounB = "b"; // stinker = long; collagen = short
        StdOut.println("Noun A is " + nounA + ". Noun B is " + nounB);
        StdOut.println("nounA is noun - " + wordnet.isNoun(nounA) + ". nounB is noun - " + wordnet.isNoun(nounB));
        StdOut.println("Shortest ancestral path is " + wordnet.distance(nounA, nounB));
        StdOut.println("Shortest common ancestor is " + wordnet.sap(nounA, nounB));

        var outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            var in = new In(args[t]);
            var nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}