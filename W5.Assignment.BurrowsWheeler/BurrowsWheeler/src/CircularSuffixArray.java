/*----------------------------------------------------------------
 *  Author:        Pylyp Lebediev
 *  Written:       30/04/2023
 *  Last updated:  31/04/2023
 *
 *  Compilation:   javac CircularSuffixArray.java
 *  Execution:     java CircularSuffixArray
 *
 *  Circular suffix array data structure
 *
 *----------------------------------------------------------------*/

import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class CircularSuffixArray {
    private final CircularSuffix[] circularSuffixes;

    /*
    Circular suffix array of s
     */
    public CircularSuffixArray(String s) {
        if (s == null) {
            throw new IllegalArgumentException();
        }

        var n = s.length();

        this.circularSuffixes = new CircularSuffix[n];
        for (var i = 0; i < n; i++) {
            this.circularSuffixes[i] = new CircularSuffix(s, i);
        }

        Arrays.sort(this.circularSuffixes);
    }

    /*
    Length of s
     */
    public int length() {
        return circularSuffixes.length;
    }

    /*
    Returns index of ith sorted suffix
     */
    public int index(int i) {
        if (i < 0 || i >= length()) {
            throw new IllegalArgumentException();
        }

        return circularSuffixes[i].getIndex();
    }

    private class CircularSuffix implements Comparable<CircularSuffix> {
        private final String s;
        private final int index;

        public CircularSuffix(String s, int index) {
            this.s = s;
            this.index = index;
        }

        public int getIndex() {
            return this.index;
        }

        public char charAt(int i) {
            var shiftedIndex = ((i + this.index) % s.length());
            return this.s.charAt(shiftedIndex);
        }

        @Override
        public int compareTo(CircularSuffix that) {
            if (this == that) {
                return 0;
            }

            var n = this.s.length();

            for (var i = 0; i < n; i++) {
                var c1 = this.charAt(i);
                var c2 = that.charAt(i);
                if (c1 < c2) {
                    return -1;
                } else if (c1 > c2) {
                    return 1;
                }
            }

            return 0;
        }
    }

    /*
    Unit testing (required)
     */
    public static void main(String[] args) {
        var s = "************";
        var circularSuffixArray = new CircularSuffixArray(s);
        StdOut.println("String is " + s + ". Length is " + circularSuffixArray.length());
        for (var i = 0; i < s.length(); i++) {
            StdOut.println(circularSuffixArray.index(i));
        }
    }
}
