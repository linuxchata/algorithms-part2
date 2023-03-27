/*----------------------------------------------------------------
 *  Author:        Pylyp Lebediev
 *  Written:       23/03/2023
 *  Last updated:  28/03/2023
 *
 *  Compilation:   javac WordNet.java
 *  Execution:     java WordNet
 *
 *  Immutable data type WordNet
 *
 *----------------------------------------------------------------*/

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WordNet {

    private final String[] nouns;
    private final Map<String, ArrayList<Integer>> nounsMap;
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

        this.nouns = new String[synsetsString.length];
        this.nounsMap = new HashMap<String, ArrayList<Integer>>(synsetsString.length);

        for (var synset : synsetsString) {
            var line = synset.split(",");
            var id = Integer.parseInt(line[0]);
            this.nouns[id] = line[1];
            var split = line[1].split("\\s+");
            for (var n : split) {
                if (this.nounsMap.containsKey(n)) {
                    var item = this.nounsMap.get(n);
                    item.add(id);
                } else {
                    var ids = new ArrayList<Integer>();
                    ids.add(id);
                    this.nounsMap.put(n, ids);
                }
            }
        }

        // Handle hypernyms
        var inHypernyms = new In(hypernyms);
        var hypernymsString = inHypernyms.readAllLines();

        var root = -1;
        var digraph = new Digraph(synsetsString.length);
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

        this.sap = new SAP(digraph);
    }

    /**
     * Returns all WordNet nouns
     */
    public Iterable<String> nouns() {
        return this.nounsMap.keySet();
    }

    /**
     * Is the word a WordNet noun?
     */
    public boolean isNoun(String word) {
        if (word == null) {
            throw new IllegalArgumentException("Word is null");
        }
        return this.nounsMap.containsKey(word);
    }

    /**
     * Distance between nounA and nounB
     */
    public int distance(String nounA, String nounB) {
        if (nounA == null) {
            throw new IllegalArgumentException("Noun A is null");
        }
        if (nounB == null) {
            throw new IllegalArgumentException("Noun B is null");
        }
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
        var nounAIds = this.nounsMap.get(nounA);
        var nounBIds = this.nounsMap.get(nounB);

        // Validate nouns identifiers
        if (nounAIds == null || nounAIds.isEmpty()) {
            throw new IllegalArgumentException(String.format("%s is not a noun (ids validation)", nounA));
        }

        if (nounBIds == null || nounBIds.isEmpty()) {
            throw new IllegalArgumentException(String.format("%s is not a noun (ids validation)", nounB));
        }

        return new ArrayList[]{nounAIds, nounBIds};
    }
}