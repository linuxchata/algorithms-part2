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

import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CircularSuffixArray {
    private int n;
    private Map<String, Integer> originalMap;
    private String[] sortedArray;

    /*
    Circular suffix array of s
     */
    public CircularSuffixArray(String s) {
        if (s == null) {
            throw new IllegalArgumentException();
        }

        n = s.length();

        var shift = 0;
        var intermediateArray = new char[n][n];
        var originalStringAsCharArray = s.toCharArray();
        var originalArray = new String[n];
        originalMap = new HashMap<>(n);
        for (var i = 0; i < n; i++, shift++) {
            for (var j = 0; j < n; j++) {
                var k = j + shift;
                if (k >= n) {
                    k = k - n;
                }
                intermediateArray[i][j] = originalStringAsCharArray[k];
            }
            originalArray[i] = new String(intermediateArray[i]);
            originalMap.put(originalArray[i], i);
        }

        sortedArray = new String[n];
        System.arraycopy(originalArray, 0, sortedArray, 0, originalArray.length);
        Arrays.sort(sortedArray);
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

        var searched = sortedArray[i];
        return originalMap.get(searched);
    }

    /*
    Unit testing (required)
     */
    public static void main(String[] args) {
        var s = "ABRACADABRA!";
        var circularSuffixArray = new CircularSuffixArray(s);
        StdOut.println("String is " + s);
        for (var i = 0; i < s.length(); i++) {
            StdOut.println(circularSuffixArray.index(i));
        }
    }
}
