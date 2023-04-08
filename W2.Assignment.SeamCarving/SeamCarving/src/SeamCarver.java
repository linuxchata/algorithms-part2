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

public class SeamCarver {
    private Picture picture;

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
        return new Picture(this.picture);
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
        if (x < 0 ||
                y < 0 ||
                x > (this.picture.width() - 1) ||
                y > (this.picture.height() - 1)) {
            throw new IllegalArgumentException("Invalid coordinates");
        }

        if (x == 0 ||
                y == 0 ||
                x == (this.picture.width() - 1) ||
                y == (this.picture.height() - 1)) {
            return 1000.0;
        }

        var rgbNextX = this.picture.get(x + 1, y);
        var rgbPrevX = this.picture.get(x - 1, y);
        var rgbNextY = this.picture.get(x, y + 1);
        var rgbPrevY = this.picture.get(x, y - 1);

        var yieldingX = Math.pow((rgbNextX.getRed() - rgbPrevX.getRed()), 2)
                + Math.pow((rgbNextX.getGreen() - rgbPrevX.getGreen()), 2)
                + Math.pow((rgbNextX.getBlue() - rgbPrevX.getBlue()), 2);
        var yieldingY = Math.pow((rgbNextY.getRed() - rgbPrevY.getRed()), 2)
                + Math.pow((rgbNextY.getGreen() - rgbPrevY.getGreen()), 2)
                + Math.pow((rgbNextY.getBlue() - rgbPrevY.getBlue()), 2);

        return Math.sqrt(yieldingX + yieldingY);
    }

    /*
    Sequence of indices for horizontal seam
     */
    public int[] findHorizontalSeam() {
        var mainAxisSize = this.picture.height(); // Main axis is Y
        var anotherAxisSize = this.picture.width();

        // Calculate the shortest path
        var leftwardDigraph = buildLeftwardDigraph();
        var minPath = findMinPath(leftwardDigraph, mainAxisSize, anotherAxisSize);

        // Calculate coordinates of the shortest path
        var result = new int[anotherAxisSize];

        var yCoordinateIndex = 1;
        var i = 0;
        for (var path : minPath) {
            if (i < minPath.size() - 1) {
                var coordinatesFrom = getLeftwardCoordinates(path.from(), mainAxisSize);
                result[i] = (coordinatesFrom[yCoordinateIndex]);
            } else if (i == minPath.size() - 1) { // Last edge
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
        var mainAxisSize = this.picture.width(); // Main axis is X
        var anotherAxisSize = this.picture.height();

        // Calculate the shortest path
        var downwardDigraph = buildDownwardDigraph();
        var minPath = findMinPath(downwardDigraph, mainAxisSize, anotherAxisSize);

        // Calculate coordinates of the shortest path
        var result = new int[anotherAxisSize];

        var xCoordinateIndex = 0;
        var i = 0;
        for (var path : minPath) {
            if (i < minPath.size() - 1) {
                var coordinatesFrom = getDownwardCoordinates(path.from(), mainAxisSize);
                result[i] = (coordinatesFrom[xCoordinateIndex]);
            } else if (i == minPath.size() - 1) { // Last edge
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
        validateSeam(seam, this.picture.width(), this.picture.height());

        if (this.picture.height() - 1 == 0) {
            return;
        }

        var resizedPicture = new Picture(this.picture.width(), this.picture.height() - 1);
        for (var col = 0; col < this.picture.width(); col++) {
            var adjustedRow = 0;
            for (var row = 0; row < this.picture.height() - 1; row++) {
                if (seam[col] == row) {
                    adjustedRow++; // Skip pixel to be removed
                }
                var color = this.picture.get(col, adjustedRow);
                resizedPicture.set(col, row, color);
                adjustedRow++;
            }
        }

        this.picture = resizedPicture;
    }

    /*
    Remove vertical seam from current picture
     */
    public void removeVerticalSeam(int[] seam) {
        validateSeam(seam, this.picture.height(), this.picture.width());

        if (this.picture.width() - 1 == 0) {
            return;
        }

        var resizedPicture = new Picture(this.picture.width() - 1, this.picture.height());
        for (var row = 0; row < this.picture.height(); row++) {
            var adjustedCol = 0;
            for (var col = 0; col < this.picture.width() - 1; col++) {
                if (seam[row] == col) {
                    adjustedCol++; // Skip pixel to be removed
                }
                var color = this.picture.get(adjustedCol, row);
                resizedPicture.set(col, row, color);
                adjustedCol++;
            }
        }

        this.picture = resizedPicture;
    }

    private EdgeWeightedDigraph buildDownwardDigraph() {
        var count = this.picture.width() * this.picture.height();

        var downwardDigraph = new EdgeWeightedDigraph(count);

        for (var i = 0; i < count; i++) {
            var downwardCoordinates = getDownwardCoordinates(i, this.picture.width());
            var downwardEnergy = energy(downwardCoordinates[0], downwardCoordinates[1]);
            var downwardVertices = getDownwardVertices(i);
            for (var v : downwardVertices) {
                downwardDigraph.addEdge(new DirectedEdge(i, v, downwardEnergy));
            }
        }

        return downwardDigraph;
    }

    private EdgeWeightedDigraph buildLeftwardDigraph() {
        var count = this.picture.width() * this.picture.height();
        var leftwardDigraph = new EdgeWeightedDigraph(count);

        for (var i = 0; i < count; i++) {
            var leftwardCoordinates = getLeftwardCoordinates(i, this.picture.height());
            var leftwardEnergy = energy(leftwardCoordinates[0], leftwardCoordinates[1]);
            var leftwardVertices = getLeftwardVertices(i);
            for (var v : leftwardVertices) {
                leftwardDigraph.addEdge(new DirectedEdge(i, v, leftwardEnergy));
            }
        }

        return leftwardDigraph;
    }

    private int[] getDownwardCoordinates(int v, int max) {
        var x = v % max;
        var y = v / max;
        return new int[]{x, y};
    }

    private int[] getLeftwardCoordinates(int v, int max) {
        var x = v / max;
        var y = v % max;
        return new int[]{x, y};
    }

    private int[] getDownwardVertices(int v) {
        var width = this.picture.width();
        var height = this.picture.height();

        if (v / width >= height - 1) { // Check for the last 'row' pixel
            return new int[0];
        }

        if (width == 1) {
            return new int[]{v + width};
        }

        var d = v % width;
        if (d == 0) { // Handle the first 'column' pixel
            var start = v + width;
            return new int[]{start++, start};
        } else if (d == width - 1) { // Handle the last 'column' pixel
            var start = v + width - 1;
            return new int[]{start++, start};
        } else {
            var start = v + width - 1;
            return new int[]{start++, start++, start};
        }
    }

    private int[] getLeftwardVertices(int v) {
        var width = this.picture.width();
        var height = this.picture.height();

        if (v / height >= width - 1) { // Check for the last 'column' pixel
            return new int[0];
        }

        if (height == 1) {
            return new int[]{v + height};
        }

        var d = v % height;
        if (d == 0) { // Handle the first 'row' pixel
            var start = v + height;
            return new int[]{start++, start};
        } else if (d == height - 1) { // Handle the last 'row' pixel
            var start = v + height - 1;
            return new int[]{start++, start};
        } else {
            var start = v + height - 1;
            return new int[]{start++, start++, start};
        }
    }

    private ArrayList<DirectedEdge> findMinPath(EdgeWeightedDigraph g, int axisSize, int anotherAxisSize) {
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
        for (var i = 0; i < axisSize; i = i + 1) {
            var acyclicSP = new AcyclicSP(g, startAxisVertices[i]);
            for (var j = 0; j < axisSize; j = j + 1) {
                var path = acyclicSP.pathTo(endAxisVertices[j]);
                var distTo = acyclicSP.distTo(endAxisVertices[j]);
                if (minDist > distTo) {
                    minDist = distTo;
                    minPath = path;
                }
            }
        }

        var minPathList = new ArrayList<DirectedEdge>();
        minPath.forEach(minPathList::add);

        return minPathList;
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
            if (prevItem != -1) {
                var diff = Math.abs(item - prevItem);
                if (diff > 1) {
                    throw new IllegalArgumentException("Seam's step is invalid");
                }
            }
            prevItem = item;
        }
    }

    public static void main(String[] args) {
        var pic = new Picture("8x1.png");
        var seamCarver = new SeamCarver(pic);
        var vertical = seamCarver.findVerticalSeam();
        seamCarver.removeVerticalSeam(vertical);
        var horizontal = seamCarver.findHorizontalSeam();
        seamCarver.removeHorizontalSeam(horizontal);
    }
}
