/*----------------------------------------------------------------
 *  Author:        Pylyp Lebediev
 *  Written:       05/04/2023
 *  Last updated:  09/04/2023
 *
 *  Compilation:   javac SeamCarver.java
 *  Execution:     java SeamCarver
 *
 *  Seam carver datatype
 *
 *----------------------------------------------------------------*/

import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;
import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.Topological;

public class SeamCarver {
    private static final double EDGE_ENERGY = 1000.0;
    private static final int RED = 16;
    private static final int GREEN = 8;
    private static final int BLUE = 0;
    private int width;
    private int height;
    private int[][] pictureColors;
    private boolean isTransposed;

    /*
    Create a seam carver object based on the given picture
     */
    public SeamCarver(Picture picture) {
        if (picture == null) {
            throw new IllegalArgumentException("Picture is not specified");
        }

        this.width = picture.width();
        this.height = picture.height();
        this.pictureColors = new int[this.height][this.width]; // Only picture colors are stored for optimization

        for (var col = 0; col < this.width; col++) {
            for (var row = 0; row < this.height; row++) {
                this.pictureColors[row][col] = picture.getRGB(col, row);
            }
        }

        this.isTransposed = false;
    }

    /*
    Current picture
     */
    public Picture picture() {
        if (this.isTransposed) {
            transpose();
        }

        var picture = new Picture(this.width, this.height);
        for (var col = 0; col < this.width; col++) {
            for (var row = 0; row < this.height; row++) {
                picture.setRGB(col, row, this.pictureColors[row][col]);
            }
        }

        return picture;
    }

    /*
    Width of current picture
     */
    public int width() {
        if (this.isTransposed) {
            return this.height;
        }

        return this.width;
    }

    /*
    Height of current picture
     */
    public int height() {
        if (this.isTransposed) {
            return this.width;
        }

        return this.height;
    }

    /*
    Energy of pixel at column x and row y
     */
    public double energy(int x, int y) {
        if (this.isTransposed) {
            transpose();
        }

        return energyInternal(x, y);
    }

    /*
    Sequence of indices for horizontal seam
     */
    public int[] findHorizontalSeam() {
        if (!this.isTransposed) {
            transpose();
        }

        return findSeam();
    }

    /*
    Sequence of indices for vertical seam
     */
    public int[] findVerticalSeam() {
        if (this.isTransposed) {
            transpose();
        }

        return findSeam();
    }

    /*
    Remove horizontal seam from the current picture
     */
    public void removeHorizontalSeam(int[] seam) {
        if (!this.isTransposed) {
            transpose();
        }

        removeSeam(seam);
    }

    /*
    Remove vertical seam from the current picture
     */
    public void removeVerticalSeam(int[] seam) {
        if (this.isTransposed) {
            transpose();
        }

        removeSeam(seam);
    }

    private double energyInternal(int x, int y) {
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
            return EDGE_ENERGY;
        }

        var rgbNextX = this.pictureColors[y][x + 1];
        var rgbPrevX = this.pictureColors[y][x - 1];
        var rgbNextY = this.pictureColors[y + 1][x];
        var rgbPrevY = this.pictureColors[y - 1][x];

        var yieldingX = Math.pow((getRed(rgbNextX) - getRed(rgbPrevX)), 2)
                + Math.pow((getGreen(rgbNextX) - getGreen(rgbPrevX)), 2)
                + Math.pow((getBlue(rgbNextX) - getBlue(rgbPrevX)), 2);
        var yieldingY = Math.pow((getRed(rgbNextY) - getRed(rgbPrevY)), 2)
                + Math.pow((getGreen(rgbNextY) - getGreen(rgbPrevY)), 2)
                + Math.pow((getBlue(rgbNextY) - getBlue(rgbPrevY)), 2);

        return Math.sqrt(yieldingX + yieldingY);
    }

    private int getRed(int rgb) {
        return getColor(rgb, RED);
    }

    private int getGreen(int rgb) {
        return getColor(rgb, GREEN);
    }

    private int getBlue(int rgb) {
        return getColor(rgb, BLUE);
    }

    private int getColor(int rgb, int shift) {
        return (rgb >> shift) & 0xFF;
    }

    private void transpose() {
        var transposed = new int[this.width][this.height];
        for (var i = 0; i < this.height; i++) {
            for (var j = 0; j < this.width; j++) {
                transposed[j][i] = this.pictureColors[i][j];
            }
        }

        var temp = this.height;
        this.height = this.width;
        this.width = temp;
        this.pictureColors = transposed;
        this.isTransposed = !this.isTransposed;
    }

    private int[] findSeam() {
        var mainAxisSize = this.width; // Main axis is X
        var anotherAxisSize = this.height;

        // Calculate the shortest path
        var downwardDigraph = buildDigraph();
        var minPath = findMinPath(downwardDigraph, mainAxisSize, anotherAxisSize);

        // Calculate coordinates of the shortest path
        return getMinPathCoordinates(minPath, mainAxisSize, anotherAxisSize);
    }

    private EdgeWeightedDigraph buildDigraph() {
        var count = this.width * this.height;
        var downwardDigraph = new EdgeWeightedDigraph(count);

        for (var i = 0; i < count; i++) {
            var coordinates = getCoordinates(i, this.width);
            var energy = energyInternal(coordinates[0], coordinates[1]);
            var vertices = getVertices(i);
            for (var v : vertices) {
                downwardDigraph.addEdge(new DirectedEdge(i, v, energy));
            }
        }

        return downwardDigraph;
    }

    private int[] getCoordinates(int v, int axisSize) {
        var x = v % axisSize;
        var y = v / axisSize;
        return new int[]{x, y};
    }

    private int[] getVertices(int v) {
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

    private Iterable<DirectedEdge> findMinPath(EdgeWeightedDigraph g, int axisSize, int anotherAxisSize) {
        // Calculate vertices to check for the first and last axes
        var endAxisFirstVertex = axisSize * (anotherAxisSize - 1);
        var verticesToCheckCount = axisSize / 3 + (axisSize % 3 != 0 ? 1 : 0); // Check every third pixel
        var startAxisVertices = new int[verticesToCheckCount];
        var endAxisVertices = new int[verticesToCheckCount];
        var vi = 0;
        for (var v = 0; v < axisSize; v = v + 3) {
            startAxisVertices[vi] = v;
            endAxisVertices[vi++] = endAxisFirstVertex + v;
        }

        // Calculate the shortest path
        var topological = getTopological(g);
        var minDist = Double.POSITIVE_INFINITY;
        var minPathIIndex = -1;
        var minPathJIndex = -1;

        for (var i = 0; i < verticesToCheckCount; i++) {
            var acyclicShortestPath = new AcyclicShortestPath(g, topological, startAxisVertices[i]);
            for (var j = 0; j < verticesToCheckCount; j++) {
                var distTo = acyclicShortestPath.distTo(endAxisVertices[j]);
                if (minDist > distTo) {
                    minDist = distTo;
                    minPathIIndex = i;
                    minPathJIndex = j;
                }
            }
        }

        var acyclicShortestPath = new AcyclicShortestPath(g, topological, startAxisVertices[minPathIIndex]);
        return acyclicShortestPath.pathTo(endAxisVertices[minPathJIndex]);
    }

    private Topological getTopological(EdgeWeightedDigraph d) {
        return new Topological(d);
    }

    private int[] getMinPathCoordinates(Iterable<DirectedEdge> minPath, int mainAxisSize, int anotherAxisSize) {
        var result = new int[anotherAxisSize];

        var xCoordinateIndex = 0;
        var i = 0;
        for (var path : minPath) {
            if (i < this.height - 2) {
                var coordinatesFrom = getCoordinates(path.from(), mainAxisSize);
                result[i] = coordinatesFrom[xCoordinateIndex];
            } else if (i == this.height - 2) { // Both vertices from the last edge are needed
                var coordinatesFrom = getCoordinates(path.from(), mainAxisSize);
                var coordinatesTo = getCoordinates(path.to(), mainAxisSize);
                result[i] = coordinatesFrom[xCoordinateIndex];
                result[++i] = coordinatesTo[xCoordinateIndex];
                break;
            }
            i++;
        }

        return result;
    }

    private void removeSeam(int[] seam) {
        validateSeam(seam, this.height, this.width);

        var newWidth = this.width - 1;

        if (newWidth == 0) {
            return;
        }

        var resizedPicture = new int[this.height][newWidth];
        for (var row = 0; row < this.height; row++) {
            for (var col = 0; col < this.width; col++) {
                if (seam[row] == col) {
                    System.arraycopy(this.pictureColors[row], 0, resizedPicture[row], 0, col);
                    System.arraycopy(this.pictureColors[row], col + 1, resizedPicture[row], col, newWidth - col);
                    break;
                }
            }
        }

        this.pictureColors = resizedPicture;
        this.width = newWidth;
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
        seamCarver.picture();
    }
}
