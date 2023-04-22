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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;

public class BoggleSolver {
    private final Trie dictionaryTrie = new Trie();
    private int width;
    private int height;
    private char[] board;
    private Map<Integer, ArrayList<Integer>> adjacentCache;
    private Map<Integer, int[]> coordinatesCache;
    private HashSet<String> words;

    /*
    Initializes the data structure using the given array of strings as the dictionary.
    (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
     */
    public BoggleSolver(String[] dictionary) {
        if (dictionary == null || dictionary.length == 0) {
            throw new IllegalArgumentException();
        }

        for (var word : dictionary) {
            if (word.length() > 2) {
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

        var size = this.width * this.height;

        this.board = new char[size];
        var k = 0;
        for (var r = 0; r < this.height; r++) {
            for (var c = 0; c < this.width; c++) {
                this.board[k++] = board.getLetter(r, c);
            }
        }

        this.adjacentCache = new HashMap<Integer, ArrayList<Integer>>();
        this.coordinatesCache = new HashMap<Integer, int[]>();
        this.words = new HashSet<String>();

        for (var i = 0; i < size; i++) {
            dfs(i, size);
        }

        return this.words;
    }

    /*
    Returns the score of the given word if it is in the dictionary, zero otherwise.
    (You can assume the word contains only the uppercase letters A through Z.)
     */
    public int scoreOf(String word) {
        if (word == null) {
            throw new IllegalArgumentException();
        }

        var length = word.length();

        if (length > 0 && !this.dictionaryTrie.contains(word)) {
            return 0;
        }

        int points;
        if (length < 3) {
            points = 0;
        } else if (length == 3 || length == 4) {
            points = 1;
        } else if (length == 5) {
            points = 2;
        } else if (length == 6) {
            points = 3;
        } else if (length == 7) {
            points = 5;
        } else {
            points = 11;
        }

        return points;
    }

    /*
    Method based on depth-first search to enumerate all strings that can be composed
    by following sequences of adjacent dice
     */
    private void dfs(int item, int size) {
        var marked = new boolean[size];
        var path = new LinkedList<Integer>();
        path.add(item);
        dfsInternal(item, marked, path);
    }

    private void dfsInternal(int v, boolean[] marked, LinkedList<Integer> path) {
        marked[v] = true;
        for (var w : getAdjacent(v)) {
            if (!marked[w]) {
                path.add(w);
                if (!doesPrefixExist(path)) {
                    // When the current path corresponds to a string that is not a prefix of
                    // any word in the dictionary, there is no need to expand the path further
                    path.removeLast();
                    marked[w] = false;
                    continue;
                }
                dfsInternal(w, marked, path);
                path.removeLast();
                marked[w] = false;
            }
        }
    }

    private ArrayList<Integer> getAdjacent(int w) {
        var value = this.adjacentCache.get(w);
        if (value != null) {
            return value;
        }

        var coordinates = toCoordinates(w);
        var r = coordinates[0];
        var c = coordinates[1];

        var result = new ArrayList<Integer>();
        var maxH = Math.min(2, this.height);
        var maxV = Math.min(2, this.width);
        for (var h = -1; h < maxH; h++) {
            var newRowIndex = r + h;
            if (newRowIndex >= 0 && newRowIndex < this.height) {
                for (var v = -1; v < maxV; v++) {
                    var newColumnIndex = c + v;
                    if (newColumnIndex >= 0 && newColumnIndex < this.width) {
                        if (newRowIndex == r && newColumnIndex == c) {
                            continue;
                        }
                        result.add(fromCoordinates(newRowIndex, newColumnIndex));
                    }
                }
            }
        }

        this.adjacentCache.put(w, result);

        return result;
    }

    private boolean doesPrefixExist(LinkedList<Integer> queue) {
        var prefix = getPrefix(queue);
        var doesPrefixExist = this.dictionaryTrie.containsKeysWithPrefix(prefix);
        if (doesPrefixExist && this.dictionaryTrie.contains(prefix)) {
            this.words.add(prefix);
        }

        return doesPrefixExist;
    }

    private String getPrefix(LinkedList<Integer> queue) {
        var stringBuilder = new StringBuilder();
        for (var item : queue) {
            var letter = this.board[item];
            stringBuilder.append(letter == 'Q' ? "QU" : letter); // 'Q' is representing the two-letter sequence "Qu"
        }

        return stringBuilder.toString();
    }

    private int fromCoordinates(int r, int c) {
        return r * this.width + c;
    }

    private int[] toCoordinates(int v) {
        var value = this.coordinatesCache.get(v);
        if (value != null) {
            return value;
        }

        var result = new int[2];
        result[0] = v / width; // Row
        result[1] = v % width; // Column

        this.coordinatesCache.put(v, result);

        return result;
    }

    public static void main(String[] args) {
        var in = new In("dictionary-algs4.txt");
        var dictionary = in.readAllStrings();
        var solver = new BoggleSolver(dictionary);
        var board = new BoggleBoard("board4x4.txt");
        var score = 0;
        var validWords = new ArrayList<String>();
        solver.getAllValidWords(board).forEach(validWords::add);
        Collections.sort(validWords);
        var size = 0;
        for (var word : validWords) {
            StdOut.println(word);
            score += solver.scoreOf(word);
            size++;
        }
        StdOut.println("Score = " + score);
        StdOut.println("Size = " + size);
    }
}
