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
    private final Picture picture;

    /*
    Create a seam carver object based on the given picture
     */
    public SeamCarver(Picture picture) {
        if (picture == null) {
            throw new IllegalArgumentException("Picture is not specified");
        }

        this.picture = new Picture(picture);
    }

    /*
    Current picture
     */
    public Picture picture() {
        return this.picture;
    }

    /*
    Width of current picture
     */
    public int width() {
        return this.picture.width();
    }

    /*
    Height of current picture
     */
    public int height() {
        return this.picture.height();
    }

    /*
    Energy of pixel at column x and row y
     */
    public double energy(int x, int y) {
        if (x < 0 || y < 0) {
            throw new IllegalArgumentException();
        }

        if (x == 0 || y == 0 || x == (this.picture.width() - 1) || y == (this.picture.height() - 1)) {
            return 1000;
        }

        var rgbNextX = this.picture.get(x + 1, y);
        var rgbPrevX = this.picture.get(x - 1, y);
        var rgbNextY = this.picture.get(x, y + 1);
        var rgbPrevY = this.picture.get(x, y - 1);
        var yieldingX = Math.pow((rgbNextX.getRed() - rgbPrevX.getRed()), 2) + Math.pow((rgbNextX.getGreen() - rgbPrevX.getGreen()), 2) + Math.pow((rgbNextX.getBlue() - rgbPrevX.getBlue()), 2);
        var yieldingY = Math.pow((rgbNextY.getRed() - rgbPrevY.getRed()), 2) + Math.pow((rgbNextY.getGreen() - rgbPrevY.getGreen()), 2) + Math.pow((rgbNextY.getBlue() - rgbPrevY.getBlue()), 2);

        return Math.sqrt(yieldingX + yieldingY);
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
        var pic = new Picture("3x4.png");
        var seamCarver = new SeamCarver(pic);
    }
}
