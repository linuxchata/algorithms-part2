/*----------------------------------------------------------------
 *  Author:        Pylyp Lebediev
 *  Written:       21/04/2023
 *  Last updated:  23/04/2023
 *
 *  Compilation:   javac BoggleSolver.java
 *  Execution:     java BoggleSolver
 *
 *  Immutable data type BoggleSolver
 *
 *----------------------------------------------------------------*/

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.TrieSET;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;

public class BoggleSolver {
    private int width;
    private int height;
    private Map<String, ArrayList<Integer>> adjacentCache = new HashMap<String, ArrayList<Integer>>();
    private KnuthMorrisPratt knuthMorrisPratt = new KnuthMorrisPratt();
    private TrieSET dictionaryTrie = new TrieSET();

    /*
    Initializes the data structure using the given array of strings as the dictionary.
    (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
     */
    public BoggleSolver(String[] dictionary) {
        if (dictionary == null || dictionary.length == 0) {
            throw new IllegalArgumentException();
        }

        var dictionarySet = new HashSet<String>();
        for (var word : dictionary) {
            if (word.length() > 2) {
                dictionarySet.add(word);
                this.dictionaryTrie.add(word);
            }
        }
    }

    /*
    Returns the set of all valid words in the given Boggle board, as an Iterable.
     */
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        if (board == null) {
            throw new IllegalArgumentException();
        }

        this.width = board.cols();
        this.height = board.rows();

        dfs();

        return new ArrayList<String>();
    }

    /*
    Returns the score of the given word if it is in the dictionary, zero otherwise.
    (You can assume the word contains only the uppercase letters A through Z.)
     */
    public int scoreOf(String word) {
        if (word == null || word.length() == 0) {
            throw new IllegalArgumentException();
        }

        return -1;
    }

    private void dfs() {
        var size = this.width * this.height;
        var edge = new int[size];
        Arrays.fill(edge, -1);
        var marked = new boolean[size];
        var queue = new LinkedList<Integer>();
        queue.add(0);
        dfsInternal(0, edge, marked);
    }

    private void dfsInternal(int v, int[] edge, boolean[] marked) {
        edge[v] = v;
        marked[v] = true;
        var coordinates = toCoordinates(v);
        var adj = getAdjacent(coordinates[0], coordinates[1]);
        for (var w : adj) {
            if (!marked[w]) {
                dfsInternal(w, edge, marked);
                edge[w] = v;
                marked[w] = false;
            }
        }
    }

    private ArrayList<Integer> getAdjacent(int r, int c) {
        var key = r + " " + c;
        if (adjacentCache.containsKey(key)) {
            return adjacentCache.get(key);
        }

        var result = new ArrayList<Integer>();
        for (var a = -1; a < 2; a++) {
            var newRowIndex = r + a;
            if (newRowIndex >= 0 && newRowIndex < this.width) {
                for (var b = -1; b < 2; b++) {
                    var newColumnIndex = c + b;
                    if (newColumnIndex >= 0 && newColumnIndex < this.width) {
                        if (newRowIndex == r && newColumnIndex == c) {
                            continue;
                        }
                        result.add(fromCoordinates(newRowIndex, newColumnIndex));
                    }
                }
            }
        }

        adjacentCache.put(key, result);

        return result;
    }

    private int fromCoordinates(int r, int c) {
        return r * this.width + c;
    }

    private int[] toCoordinates(int value) {
        var result = new int[2];
        result[0] = value / width; // Row
        result[1] = value % width; // Column
        return result;
    }

    public static void main(String[] args) {
        var in = new In("dictionary-yawl.txt");
        var dictionary = in.readAllStrings();
        var solver = new BoggleSolver(dictionary);
        var board = new BoggleBoard("board3x3.txt");
        int score = 0;
        for (var word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}
