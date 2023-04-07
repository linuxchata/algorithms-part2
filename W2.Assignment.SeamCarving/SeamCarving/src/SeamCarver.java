/*----------------------------------------------------------------
 *  Author:        Pylyp Lebediev
 *  Written:       05/04/2023
 *  Last updated:  07/04/2023
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
    private final Picture picture;
    private final EdgeWeightedDigraph downwardDigraph;
    private final EdgeWeightedDigraph leftwardDigraph;

    /*
    Create a seam carver object based on the given picture
     */
    public SeamCarver(Picture picture) {
        if (picture == null) {
            throw new IllegalArgumentException("Picture is not specified");
        }

        this.picture = new Picture(picture);

        var count = this.picture.width() * this.picture.height();

        this.downwardDigraph = new EdgeWeightedDigraph(count);
        this.leftwardDigraph = new EdgeWeightedDigraph(count);

        for (var i = 0; i < count; i++) {
            // Downward edge-weighted digraph for vertical seaming
            var downwardCoordinates = getDownwardCoordinates(i, this.picture.width());
            var downwardEnergy = energy(downwardCoordinates[0], downwardCoordinates[1]);
            var downwardVertices = getDownwardVertices(i);
            for (var v : downwardVertices) {
                this.downwardDigraph.addEdge(new DirectedEdge(i, v, downwardEnergy));
            }

            // Leftward edge-weighted digraph for horizontal seaming
            var leftwardCoordinates = getLeftwardCoordinates(i, this.picture.height());
            var leftwardEnergy = energy(leftwardCoordinates[0], leftwardCoordinates[1]);
            var leftwardVertices = getLeftwardVertices(i);
            for (var v : leftwardVertices) {
                this.leftwardDigraph.addEdge(new DirectedEdge(i, v, leftwardEnergy));
            }
        }
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
        var mainAxisSize = this.picture.height(); // Main axis is Y
        var anotherAxisSize = this.picture.width();

        // Calculate the shortest path
        var minPath = findMinPath(this.leftwardDigraph, mainAxisSize, anotherAxisSize);

        // Calculate coordinates of the shortest path
        var result = new int[anotherAxisSize];

        var yCoordinateIndex = 1;
        var i = 0;
        for (var path : minPath) {
            if (i < minPath.size() - 1) {
                var coordinatesFrom = getLeftwardCoordinates(path.from(), mainAxisSize);
                result[i] = (coordinatesFrom[yCoordinateIndex]);
            } else if (i == minPath.size() - 1) {
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
        var minPath = findMinPath(this.downwardDigraph, mainAxisSize, anotherAxisSize);

        // Calculate coordinates of the shortest path
        var result = new int[anotherAxisSize];

        var xCoordinateIndex = 0;
        var i = 0;
        for (var path : minPath) {
            if (i < minPath.size() - 1) {
                var coordinatesFrom = getDownwardCoordinates(path.from(), mainAxisSize);
                result[i] = (coordinatesFrom[xCoordinateIndex]);
            } else if (i == minPath.size() - 1) {
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

    private ArrayList<DirectedEdge> findMinPath(EdgeWeightedDigraph g, int axisSize, int anotherAxisSize) {
        // Calculate vertices for first and last axes
        var endAxisFirstVertex = axisSize * (anotherAxisSize - 1);
        var startAxis = new int[axisSize];
        var endAxis = new int[axisSize];
        for (var i = 0; i < axisSize; i++) {
            startAxis[i] = i;
            endAxis[i] = endAxisFirstVertex + i;
        }

        // Calculate the shortest path
        var minDist = Double.POSITIVE_INFINITY;
        Iterable<DirectedEdge> minPath = new ArrayList<DirectedEdge>();
        for (var i = 0; i < axisSize; i++) {
            var acyclicSP = new AcyclicSP(g, startAxis[i]);
            for (var j = 0; j < axisSize; j++) {
                var path = acyclicSP.pathTo(endAxis[j]);
                var distTo = acyclicSP.distTo(endAxis[j]);
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

    public static void main(String[] args) {
        var pic = new Picture("6x5.png");
        var seamCarver = new SeamCarver(pic);
        seamCarver.findVerticalSeam();
        seamCarver.findHorizontalSeam();
    }
}
