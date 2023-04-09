/*----------------------------------------------------------------
 *  Author:        Pylyp Lebediev
 *  Written:       05/04/2023
 *  Last updated:  08/04/2023
 *
 *  Compilation:   javac SeamCarver.java
 *  Execution:     java SeamCarver
 *
 *  Seam carver datatype
 *
 *----------------------------------------------------------------*/

import edu.princeton.cs.algs4.AcyclicSP;
import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;
import edu.princeton.cs.algs4.Picture;

import java.util.ArrayList;

public class SeamCarverUnoptimized {
    private int width;
    private int height;
    private int[][] pictureColors;

    /*
    Create a seam carver object based on the given picture
     */
    public SeamCarverUnoptimized(Picture picture) {
        if (picture == null) {
            throw new IllegalArgumentException("Picture is not specified");
        }

        this.width = picture.width();
        this.height = picture.height();
        this.pictureColors = new int[this.width][this.height]; // Only picture colors are stored for optimization

        for (var col = 0; col < this.width; col++) {
            for (var row = 0; row < this.height; row++) {
                this.pictureColors[col][row] = picture.getRGB(col, row);
            }
        }
    }

    /*
    Current picture
     */
    public Picture picture() {
        var picture = new Picture(this.width, this.height);
        for (var col = 0; col < this.width; col++) {
            for (var row = 0; row < this.height; row++) {
                picture.setRGB(col, row, this.pictureColors[col][row]);
            }
        }

        return picture;
    }

    /*
    Width of current picture
     */
    public int width() {
        return this.width;
    }

    /*
    Height of current picture
     */
    public int height() {
        return this.height;
    }

    /*
    Energy of pixel at column x and row y
     */
    public double energy(int x, int y) {
        if (x < 0 ||
                y < 0 ||
                x > (this.width - 1) ||
                y > (this.height - 1)) {
            throw new IllegalArgumentException("Invalid coordinates");
        }

        if (x == 0 ||
                y == 0 ||
                x == (this.width - 1) ||
                y == (this.height - 1)) {
            return 1000.0;
        }

        var rgbNextX = this.pictureColors[x + 1][y];
        var rgbPrevX = this.pictureColors[x - 1][y];
        var rgbNextY = this.pictureColors[x][y + 1];
        var rgbPrevY = this.pictureColors[x][y - 1];

        var yieldingX = Math.pow((getRed(rgbNextX) - getRed(rgbPrevX)), 2)
                + Math.pow((getGreen(rgbNextX) - getGreen(rgbPrevX)), 2)
                + Math.pow((getBlue(rgbNextX) - getBlue(rgbPrevX)), 2);
        var yieldingY = Math.pow((getRed(rgbNextY) - getRed(rgbPrevY)), 2)
                + Math.pow((getGreen(rgbNextY) - getGreen(rgbPrevY)), 2)
                + Math.pow((getBlue(rgbNextY) - getBlue(rgbPrevY)), 2);

        return Math.sqrt(yieldingX + yieldingY);
    }

    /*
    Sequence of indices for horizontal seam
     */
    public int[] findHorizontalSeam() {
        var mainAxisSize = this.height; // Main axis is Y
        var anotherAxisSize = this.width;

        // Calculate the shortest path
        var leftwardDigraph = buildLeftwardDigraph();
        var minPath = findMinPath(leftwardDigraph, mainAxisSize, anotherAxisSize);

        // Calculate coordinates of the shortest path
        var result = new int[anotherAxisSize];

        var yCoordinateIndex = 1;
        var i = 0;
        for (var path : minPath) {
            if (i < this.width - 2) {
                var coordinatesFrom = getLeftwardCoordinates(path.from(), mainAxisSize);
                result[i] = (coordinatesFrom[yCoordinateIndex]);
            } else if (i == this.width - 2) { // Both vertices from last edge are needed
                var coordinatesFrom = getLeftwardCoordinates(path.from(), mainAxisSize);
                var coordinatesTo = getLeftwardCoordinates(path.to(), mainAxisSize);
                result[i] = coordinatesFrom[yCoordinateIndex];
                result[++i] = coordinatesTo[yCoordinateIndex];
                break;
            }
            i++;
        }

        return result;
    }

    /*
    Sequence of indices for vertical seam
     */
    public int[] findVerticalSeam() {
        var mainAxisSize = this.width; // Main axis is X
        var anotherAxisSize = this.height;

        // Calculate the shortest path
        var downwardDigraph = buildDownwardDigraph();
        var minPath = findMinPath(downwardDigraph, mainAxisSize, anotherAxisSize);

        // Calculate coordinates of the shortest path
        var result = new int[anotherAxisSize];

        var xCoordinateIndex = 0;
        var i = 0;
        for (var path : minPath) {
            if (i < this.height - 2) {
                var coordinatesFrom = getDownwardCoordinates(path.from(), mainAxisSize);
                result[i] = (coordinatesFrom[xCoordinateIndex]);
            } else if (i == this.height - 2) { // Both vertices from last edge are needed
                var coordinatesFrom = getDownwardCoordinates(path.from(), mainAxisSize);
                var coordinatesTo = getDownwardCoordinates(path.to(), mainAxisSize);
                result[i] = coordinatesFrom[xCoordinateIndex];
                result[++i] = coordinatesTo[xCoordinateIndex];
                break;
            }
            i++;
        }

        return result;
    }

    /*
    Remove horizontal seam from current picture
     */
    public void removeHorizontalSeam(int[] seam) {
        validateSeam(seam, this.width, this.height);

        if (this.height - 1 == 0) {
            return;
        }

        var resizedPicture = new int[this.width][this.height - 1];
        for (var col = 0; col < this.width; col++) {
            var adjustedRow = 0;
            for (var row = 0; row < this.height - 1; row++) {
                if (seam[col] == row) {
                    adjustedRow++; // Skip pixel to be removed
                }
                resizedPicture[col][row] = this.pictureColors[col][adjustedRow];
                adjustedRow++;
            }
        }

        this.pictureColors = resizedPicture;
        this.height = this.height - 1;
    }

    /*
    Remove vertical seam from current picture
     */
    public void removeVerticalSeam(int[] seam) {
        validateSeam(seam, this.height, this.width);

        if (this.width - 1 == 0) {
            return;
        }

        var resizedPicture = new int[this.width - 1][this.height];

        for (var row = 0; row < this.height; row++) {
            var adjustedCol = 0;
            for (var col = 0; col < this.width - 1; col++) {
                if (seam[row] == col) {
                    adjustedCol++; // Skip pixel to be removed
                }
                resizedPicture[col][row] = this.pictureColors[adjustedCol][row];
                adjustedCol++;
            }
        }

        this.pictureColors = resizedPicture;
        this.width = this.width - 1;
    }

    private int getRed(int rgb) {
        return (rgb >> 16) & 0xFF;
    }

    private int getGreen(int rgb) {
        return (rgb >> 8) & 0xFF;
    }

    private int getBlue(int rgb) {
        return (rgb >> 0) & 0xFF;
    }

    private EdgeWeightedDigraph buildDownwardDigraph() {
        var count = this.width * this.height;
        var downwardDigraph = new EdgeWeightedDigraph(count);

        for (var i = 0; i < count; i++) {
            var downwardCoordinates = getDownwardCoordinates(i, this.width);
            var downwardEnergy = energy(downwardCoordinates[0], downwardCoordinates[1]);
            var downwardVertices = getDownwardVertices(i);
            for (var v : downwardVertices) {
                downwardDigraph.addEdge(new DirectedEdge(i, v, downwardEnergy));
            }
        }

        return downwardDigraph;
    }

    private EdgeWeightedDigraph buildLeftwardDigraph() {
        var count = this.width * this.height;
        var leftwardDigraph = new EdgeWeightedDigraph(count);

        for (var i = 0; i < count; i++) {
            var leftwardCoordinates = getLeftwardCoordinates(i, this.height);
            var leftwardEnergy = energy(leftwardCoordinates[0], leftwardCoordinates[1]);
            var leftwardVertices = getLeftwardVertices(i);
            for (var v : leftwardVertices) {
                leftwardDigraph.addEdge(new DirectedEdge(i, v, leftwardEnergy));
            }
        }

        return leftwardDigraph;
    }

    private int[] getDownwardCoordinates(int v, int axisSize) {
        var x = v % axisSize;
        var y = v / axisSize;
        return new int[]{x, y};
    }

    private int[] getLeftwardCoordinates(int v, int axisSize) {
        var x = v / axisSize;
        var y = v % axisSize;
        return new int[]{x, y};
    }

    private int[] getDownwardVertices(int v) {
        if (v / this.width >= this.height - 1) { // Check for the last 'row' pixel
            return new int[0];
        }

        if (this.width == 1) {
            return new int[]{v + this.width};
        }

        var d = v % this.width;
        if (d == 0) { // Handle the first 'column' pixel
            var start = v + this.width;
            return new int[]{start++, start};
        } else if (d == this.width - 1) { // Handle the last 'column' pixel
            var start = v + this.width - 1;
            return new int[]{start++, start};
        } else {
            var start = v + this.width - 1;
            return new int[]{start++, start++, start};
        }
    }

    private int[] getLeftwardVertices(int v) {
        if (v / this.height >= this.width - 1) { // Check for the last 'column' pixel
            return new int[0];
        }

        if (this.height == 1) {
            return new int[]{v + this.height};
        }

        var d = v % this.height;
        if (d == 0) { // Handle the first 'row' pixel
            var start = v + this.height;
            return new int[]{start++, start};
        } else if (d == this.height - 1) { // Handle the last 'row' pixel
            var start = v + this.height - 1;
            return new int[]{start++, start};
        } else {
            var start = v + this.height - 1;
            return new int[]{start++, start++, start};
        }
    }

    private Iterable<DirectedEdge> findMinPath(EdgeWeightedDigraph g, int axisSize, int anotherAxisSize) {
        // Calculate vertices for first and last axes
        var endAxisFirstVertex = axisSize * (anotherAxisSize - 1);
        var startAxisVertices = new int[axisSize];
        var endAxisVertices = new int[axisSize];
        for (var i = 0; i < axisSize; i++) {
            startAxisVertices[i] = i;
            endAxisVertices[i] = endAxisFirstVertex + i;
        }

        // Calculate the shortest path
        var minDist = Double.POSITIVE_INFINITY;
        Iterable<DirectedEdge> minPath = new ArrayList<DirectedEdge>();

        for (var i = 0; i < axisSize; i = i + 2) { // Each second vertex is skipped for performance optimizations
            var acyclicSP = new AcyclicSP(g, startAxisVertices[i]);
            for (var j = 0; j < axisSize; j = j + 2) { // Each second vertex is skipped for performance optimizations
                var distTo = acyclicSP.distTo(endAxisVertices[j]);
                if (minDist > distTo) {
                    minDist = distTo;
                    minPath = acyclicSP.pathTo(endAxisVertices[j]);
                }
            }
        }

        return minPath;
    }

    private void validateSeam(int[] seam, int axisSize, int anotherAxisSize) {
        if (seam == null) {
            throw new IllegalArgumentException("Seam is not specified");
        }
        if (seam.length != axisSize) {
            throw new IllegalArgumentException("Seam length is invalid");
        }

        var prevItem = -1;
        for (var item : seam) {
            if (item < 0 || item > anotherAxisSize - 1) {
                throw new IllegalArgumentException("Seam's item is invalid");
            }
            if (prevItem != -1 && Math.abs(item - prevItem) > 1) {
                throw new IllegalArgumentException("Seam's step is invalid");
            }
            prevItem = item;
        }
    }

    public static void main(String[] args) {
        var pic = new Picture("6x5.png");
        var seamCarver = new SeamCarver(pic);
        var vertical = seamCarver.findVerticalSeam();
        seamCarver.removeVerticalSeam(vertical);
        var horizontal = seamCarver.findHorizontalSeam();
        seamCarver.removeHorizontalSeam(horizontal);
    }
}
