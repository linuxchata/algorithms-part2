/*----------------------------------------------------------------
 *  Author:        Pylyp Lebediev
 *  Written:       05/04/2023
 *  Last updated:  05/04/2023
 *
 *  Compilation:   javac SeamCarver.java
 *  Execution:     java SeamCarver
 *
 *  Seam carver datatype
 *
 *----------------------------------------------------------------*/

import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    private final Picture pirture;

    /*
    Create a seam carver object based on the given picture
     */
    public SeamCarver(Picture picture) {
        if (picture == null) {
            throw new IllegalArgumentException("Picture is not specified");
        }

        this.pirture = new Picture(picture);
    }

    /*
    Current picture
     */
    public Picture picture() {
        return new Picture("");
    }

    /*
    Width of current picture
     */
    public int width() {
        return 0;
    }

    /*
    Height of current picture
     */
    public int height() {
        return 0;
    }

    /*
    Energy of pixel at column x and row y
     */
    public double energy(int x, int y) {
        return 0.0;
    }

    /*
    Sequence of indices for horizontal seam
     */
    public int[] findHorizontalSeam() {
        return new int[0];
    }

    /*
    Sequence of indices for vertical seam
     */
    public int[] findVerticalSeam() {

        return new int[0];
    }

    /*
    Remove horizontal seam from current picture
     */
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null) {
            throw new IllegalArgumentException("Seam is not specified");
        }
    }

    /*
    Remove vertical seam from current picture
     */
    public void removeVerticalSeam(int[] seam) {
        if (seam == null) {
            throw new IllegalArgumentException("Seam is not specified");
        }
    }

    public static void main(String[] args) {
    }
}
