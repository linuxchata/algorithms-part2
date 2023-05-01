/*----------------------------------------------------------------
 *  Author:        Pylyp Lebediev
 *  Written:       30/04/2023
 *  Last updated:  30/04/2023
 *
 *  Compilation:   javac BurrowsWheeler.java
 *  Execution:     java BurrowsWheeler
 *
 *  The Burrows–Wheeler transform
 *
 *----------------------------------------------------------------*/

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {
    private static final int R = 256;

    /*
    Apply Burrows-Wheeler transform,
    reading from standard input and writing to standard output
     */
    public static void transform() {
        var input = BinaryStdIn.readString();

        var n = input.length();

        var circularSuffixArray = new CircularSuffixArray(input);
        var output = new char[n];
        for (var i = 0; i < n; i++) {
            // Get last column character
            var index = circularSuffixArray.index(i);
            var lastColumnCharIndex = (index - 1) < 0 ? n - 1 : (index - 1);
            output[i] = input.charAt(lastColumnCharIndex); // Last column character

            // Find first
            if (index == 0) {
                BinaryStdOut.write(i);
            }
        }

        BinaryStdOut.write(new String(output));
        BinaryStdOut.close();
    }

    /*
    Apply Burrows-Wheeler inverse transform,
    reading from standard input and writing to standard output
     */
    public static void inverseTransform() {
        var first = BinaryStdIn.readInt();

        // The last column of the sorted suffixes
        var t = BinaryStdIn.readString();

        var n = t.length();
        var firstColumn = new char[n]; // First column of sorted suffixes array

        // Get next array
        var next = getNext(firstColumn, t.toCharArray(), n);

        // Write output
        var i = 0;
        while (i < n) {
            BinaryStdOut.write(firstColumn[first]);
            first = next[first];
            i++;
        }
        BinaryStdOut.close();
    }

    private static int[] getNext(char[] firstColumn, char[] t, int n) {
        // Key-indexed counting algorithm with modification to get next array
        // Returns first column of sorted suffixes array (sorted t array)
        var count = new int[R + 1];
        var next = new int[n];

        for (var i = 0; i < n; i++) {
            count[t[i] + 1]++;
        }

        for (var r = 0; r < R; r++) {
            count[r + 1] += count[r];
        }

        for (var i = 0; i < n; i++) {
            var d = count[t[i]]++;
            next[d] = i;
            firstColumn[d] = t[i];
        }

        return next;
    }

    /*
    If args[0] is "-", apply Burrows-Wheeler transform
    If args[0] is "+", apply Burrows-Wheeler inverse transform
    */
    public static void main(String[] args) {
        var op = args[0];
        if (op.equals("-")) {
            BurrowsWheeler.transform();
        } else if (op.equals("+")) {
            BurrowsWheeler.inverseTransform();
        }
    }
}
