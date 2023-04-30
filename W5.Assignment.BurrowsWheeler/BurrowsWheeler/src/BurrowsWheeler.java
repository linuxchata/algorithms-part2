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

import java.util.Arrays;

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
        int i;
        for (i = 0; i < n; i++) {
            // Get last column character
            var index = circularSuffixArray.index(i);
            var charIndex = (index - 1) < 0 ? n - 1 : (index - 1);
            var lastColumnChar = input.charAt(charIndex);
            output[i] = lastColumnChar;

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
        var firstColumn = t.toCharArray();
        Arrays.sort(firstColumn);

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
        var count = new int[R];
        var next = new int[n];

        for (var i = 0; i < n; i++) {
            count[firstColumn[i]]++;
        }

        for (var i = 0; i < R; i++) {
            var value = (char) i;
            for (var c = 0; c < count[i]; c++) {
                var nextIndex = getValueIndexFromArray(firstColumn, value) + c;
                var nextValue = getValueIndexFromArray(t, value, c + 1);
                next[nextIndex] = nextValue;
            }
        }

        return next;
    }

    private static int getValueIndexFromArray(char[] a, char value) {
        for (var i = 0; i < a.length; i++) {
            if (a[i] == value) {
                return i;
            }
        }
        return -1;
    }

    private static int getValueIndexFromArray(char[] a, char value, int currentCount) {
        var c = 0;
        for (var i = 0; i < a.length; i++) {
            if (a[i] == value) {
                c++;
                if (currentCount == c) {
                    return i;
                }
            }
        }
        return -1;
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
