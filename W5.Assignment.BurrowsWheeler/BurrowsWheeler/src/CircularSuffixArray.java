/*----------------------------------------------------------------
 *  Author:        Pylyp Lebediev
 *  Written:       30/04/2023
 *  Last updated:  30/04/2023
 *
 *  Compilation:   javac CircularSuffixArray.java
 *  Execution:     java CircularSuffixArray
 *
 *  Circular suffix array data structure
 *
 *----------------------------------------------------------------*/

import edu.princeton.cs.algs4.MSD;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class CircularSuffixArray {
    private final int n;
    private final int[] index;

    /*
    Circular suffix array of s
     */
    public CircularSuffixArray(String s) {
        if (s == null) {
            throw new IllegalArgumentException();
        }

        n = s.length();

        var shift = 0;
        var originalStringAsCharArray = s.toCharArray();
        var originalArray = new String[n];
        for (var i = 0; i < n; i++, shift++) {
            var charArray = new char[n];
            var shiftIndex = n - shift;
            System.arraycopy(originalStringAsCharArray, shift, charArray, 0, shiftIndex);
            if (shiftIndex < n) {
                System.arraycopy(originalStringAsCharArray, 0, charArray, shiftIndex, shift);
            }
            originalArray[i] = new String(charArray);
        }

        var sortedArray = new String[n];
        System.arraycopy(originalArray, 0, sortedArray, 0, originalArray.length);
        Arrays.sort(sortedArray);

        index = new int[n];
        var used = new int[n];
        for (var i = 0; i < n; i++) {
            var item = sortedArray[i];
            for (var j = 0; j < n; j++) {
                if (used[j] == 1) {
                    continue;
                }
                if (item.equals(originalArray[j])) {
                    index[i] = j;
                    used[j] = 1;
                    break;
                }
            }
        }
    }

    /*
    Length of s
     */
    public int length() {
        return n;
    }

    /*
    Returns index of ith sorted suffix
     */
    public int index(int i) {
        if (i < 0 || i >= n) {
            throw new IllegalArgumentException();
        }

        return index[i];
    }

    /*
    Unit testing (required)
     */
    public static void main(String[] args) {
        var s = "ABRACADABRA!";
        var circularSuffixArray = new CircularSuffixArray(s);
        StdOut.println("String is " + s + ". Length is " + circularSuffixArray.length());
        for (var i = 0; i < s.length(); i++) {
            StdOut.println(circularSuffixArray.index(i));
        }
    }
}
