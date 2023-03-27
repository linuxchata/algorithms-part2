/*----------------------------------------------------------------
 *  Author:        Pylyp Lebediev
 *  Written:       24/03/2023
 *  Last updated:  28/03/2023
 *
 *  Compilation:   javac SAP.java
 *  Execution:     java SAP
 *
 *  Immutable data type the shortest ancestral path
 *
 *----------------------------------------------------------------*/

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.HashSet;

public class SAP {

    private final Digraph G;

    /**
     * Constructor takes a digraph (not necessarily a DAG)
     */
    public SAP(Digraph G) {
        if (G == null) {
            throw new IllegalArgumentException("Digraph is null");
        }

        this.G = new Digraph(G);
    }

    /**
     * Length of shortest ancestral path between v and w; -1 if no such path
     */
    public int length(int v, int w) {
        var result = getCommonAncestorAndMinDist(convertToArrayList(v), convertToArrayList(w));
        return result[1];
    }

    /**
     * A common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
     */
    public int ancestor(int v, int w) {
        var result = getCommonAncestorAndMinDist(convertToArrayList(v), convertToArrayList(w));
        return result[0];
    }

    /**
     * Length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
     */
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        var result = getCommonAncestorAndMinDist(convertIterable(v), convertIterable(w));
        return result[1];
    }

    /**
     * A common ancestor that participates in shortest ancestral path; -1 if no such path
     */
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        var result = getCommonAncestorAndMinDist(convertIterable(v), convertIterable(w));
        return result[0];
    }

    private int[] getCommonAncestorAndMinDist(ArrayList<Integer> v, ArrayList<Integer> w) {
        // Run breadth-first paths
        var vBsp = bsp(v);
        var wBsp = bsp(w);

        var paths = new HashSet<Integer>();
        paths.addAll(vBsp.path);
        paths.addAll(wBsp.path);

        // Get common ancestor and shortest distance
        var commonAncestor = -1;
        var minDist = -1;
        for (var i : paths) {
            if (wBsp.marked[i] && vBsp.marked[i]) {
                var dist = wBsp.distTo[i] + vBsp.distTo[i];
                if (minDist == -1 || dist < minDist) {
                    minDist = dist;
                    commonAncestor = i;
                }
            }
        }

        return new int[]{commonAncestor, minDist};
    }

    private Bsp bsp(ArrayList<Integer> s) {
        var size = this.G.V();
        var distTo = new int[size];
        var marked = new boolean[size];
        var path = new ArrayList<Integer>();

        var queue = new Queue<Integer>();
        for (var si : s) {
            queue.enqueue(si);
            marked[si] = true;
            path.add(si);
        }

        while (!queue.isEmpty()) {
            var v = queue.dequeue();
            for (var w : this.G.adj(v)) {
                if (!marked[w]) {
                    queue.enqueue(w);
                    distTo[w] = distTo[v] + 1;
                    marked[w] = true;
                    path.add(w);
                }
            }
        }

        return new Bsp(distTo, marked, path);
    }

    private ArrayList<Integer> convertIterable(Iterable<Integer> vi) {
        if (vi == null) {
            throw new IllegalArgumentException();
        }

        var arrayList = new ArrayList<Integer>();
        for (var v : vi) {
            if (v == null || v < 0 || v > this.G.V() - 1) {
                throw new IllegalArgumentException();
            }
            arrayList.add(v);
        }
        return arrayList;
    }

    private ArrayList<Integer> convertToArrayList(int v) {
        if (v < 0 || v > this.G.V() - 1) {
            throw new IllegalArgumentException();
        }

        var arrayList = new ArrayList<Integer>();
        arrayList.add(v);
        return arrayList;
    }

    private class Bsp {

        private final int[] distTo;
        private final boolean[] marked;
        private final ArrayList<Integer> path;

        Bsp(int[] distTo, boolean[] marked, ArrayList<Integer> path) {
            this.distTo = distTo;
            this.marked = marked;
            this.path = path;
        }
    }

    public static void main(String[] args) {
        var in = new In("digraph1.txt");
        var G = new Digraph(in);
        var sap = new SAP(G);
        int v = 3;
        int w = 11;
        int length = sap.length(v, w);
        int ancestor = sap.ancestor(v, w);
        StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
    }
}