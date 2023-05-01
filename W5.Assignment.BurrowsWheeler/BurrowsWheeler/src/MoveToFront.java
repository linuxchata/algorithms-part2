/*----------------------------------------------------------------
 *  Author:        Pylyp Lebediev
 *  Written:       30/04/2023
 *  Last updated:  30/04/2023
 *
 *  Compilation:   javac MoveToFront.java
 *  Execution:     java MoveToFront
 *
 *  Move-to-front encoding and decoding
 *
 *----------------------------------------------------------------*/

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
    private static final int R = 256;
    private static final int LG_R = 8;

    /*
    Apply move-to-front encoding, reading from standard input and writing to standard output
     */
    public static void encode() {
        var table = getTable();

        while (!BinaryStdIn.isEmpty()) {
            var c = BinaryStdIn.readChar();
            for (var j = 0; j < R; j++) {
                if (table[j] == c) {
                    BinaryStdOut.write(j, LG_R);
                    table = getShuffledTable(table, c, j);
                    break;
                }
            }
        }
        BinaryStdOut.close();
    }

    /*
    Apply move-to-front decoding, reading from standard input and writing to standard output
     */
    public static void decode() {
        var table = getTable();

        while (!BinaryStdIn.isEmpty()) {
            var j = BinaryStdIn.readInt(LG_R);
            var c = table[j];
            BinaryStdOut.write(c);
            table = getShuffledTable(table, c, j);
        }
        BinaryStdOut.close();
    }

    private static char[] getTable() {
        var table = new char[R];
        for (var i = 0; i < R; i++) {
            table[i] = (char) (i);
        }
        return table;
    }

    private static char[] getShuffledTable(char[] table, char c, int j) {
        var newTable = new char[R];
        newTable[0] = c;
        System.arraycopy(table, 0, newTable, 1, j);
        System.arraycopy(table, j + 1, newTable, j + 1, R - j - 1);
        return newTable;
    }

    /*
    If args[0] is "-", apply move-to-front encoding
    If args[0] is "+", apply move-to-front decoding
     */
    public static void main(String[] args) {
        var op = args[0];
        if (op.equals("-")) {
            MoveToFront.encode();
        } else if (op.equals("+")) {
            MoveToFront.decode();
        }
    }
}
