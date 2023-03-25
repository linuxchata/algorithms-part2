/*----------------------------------------------------------------
 *  Author:        Pylyp Lebediev
 *  Written:       23/03/2023
 *  Last updated:  24/03/2023
 *
 *  Compilation:   javac WordNet.java
 *  Execution:     java WordNet
 *
 *  The WordNet digraph
 *
 *----------------------------------------------------------------*/

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class WordNet {

    private final int count;
    private final Bag<Integer>[] adj;
    private final String[] nouns;
    private int root = -1;

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

        var inHypernyms = new In(hypernyms);
        var hypernymsString = inHypernyms.readAllLines();

        for (var hypernym : hypernymsString) {
            var result = hypernym.split(",");
            if (result.length == 1) {
                this.root = Integer.parseInt(result[0]);
                continue;
            }
            var v = Integer.parseInt(result[0]);
            for (int i = 1; i < result.length; i++) {
                var w = Integer.parseInt(result[i]);
                this.adj[v].add(w);
            }
        }

        if (this.root == -1) {
            throw new IllegalArgumentException("The input does not correspond to a rooted DAG.");
        }
    }

    /**
     * Returns all WordNet nouns
     */
    public Iterable<String> nouns() {
        return Arrays.asList(nouns);
    }

    /**
     * Is the word a WordNet noun?
     */
    public boolean isNoun(String word) {
        for (var n : nouns) {
            if (n != null && n.equals(word)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Distance between nounA and nounB
     */
    public int distance(String nounA, String nounB) {
        var paths = getPaths(nounA, nounB);
        getCommonAncestorId(paths);
        var nounAPathToRoot = paths[0];
        var nounBPathToRoot = paths[1];
        return nounAPathToRoot.size() + nounBPathToRoot.size();
    }

    /**
     * A synset (second field of synsets.txt) that is the common ancestor
     * of nounA and nounB in a shortest ancestral path
     */
    public String sap(String nounA, String nounB) {
        var paths = getPaths(nounA, nounB);
        var commonAncestorId = getCommonAncestorId(paths);
        return nouns[commonAncestorId];
    }

    private Queue<Integer>[] getPaths(String nounA, String nounB) {
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
            if (nounAId == -1 && nounA.equals(n)) {
                nounAId = i;
            }
            if (nounBId == -1 && nounB.equals(n)) {
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

        // Run Breadth-First Paths;
        var nounAPathToRoot = bsp(nounAId);
        var nounBPathToRoot = bsp(nounBId);

        return new Queue[] {nounAPathToRoot, nounBPathToRoot};
    }

    private Queue<Integer> bsp(int s) {
        var edgeTo = new int[this.count];
        var marked = new boolean[this.count];

        // Create breadth-first paths
        bspInternal(s, edgeTo, marked);

        // Process path
        var resultPath = new Queue<Integer>();
        for (var i = this.root; i < this.count; i = edgeTo[i]) {
            if (marked[i]) {
                resultPath.enqueue(i);
            } else {
                break;
            }

            // Exist loop when target vertex is reached
            if (i == s) {
                break;
            }
        }

        return resultPath;
    }

    private void bspInternal(int s, int[] edgeTo, boolean[] marked) {
        var queue = new Queue<Integer>();
        queue.enqueue(s);
        marked[s] = true;

        while (!queue.isEmpty()) {
            var v = queue.dequeue();
            for (var w : getAdjacent(v)) {
                if (!marked[w]) {
                    queue.enqueue(w);
                    edgeTo[w] = v;
                    marked[w] = true;
                }
            }
        }
    }

    private Bag<Integer> getAdjacent(int v) {
        return adj[v];
    }

    private int getCommonAncestorId(Queue<Integer>[] paths) {
        var nounAPathToRoot = paths[0];
        var nounBPathToRoot = paths[1];

        var commonAncestorId = -1;
        for (int i = 0; i < this.count; i++) {
            if (nounAPathToRoot.isEmpty()) {
                break;
            }
            if (nounBPathToRoot.isEmpty()) {
                break;
            }

            var a = nounAPathToRoot.peek();
            var b = nounBPathToRoot.peek();

            if (a.equals(b)) {
                commonAncestorId = a;
                nounAPathToRoot.dequeue();
                nounBPathToRoot.dequeue();
            } else {
                break;
            }
        }

        return commonAncestorId;
    }

    public static void main(String[] args) {
        var wordnet = new WordNet("synsets6.txt", "hypernyms6TwoAncestors.txt");
        var nounA = "a"; // prolamine
        var nounB = "b"; // stinker = long; collagen = short
        StdOut.println("Noun A is " + nounA + ". Noun B is " + nounB);
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