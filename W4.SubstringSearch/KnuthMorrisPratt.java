public class KnuthMorrisPratt {
    private static final int RADIX = 26;

    public int search(String text, String pattern) {
        var n = text.length();
        var m = pattern.length();

        var dfa = new int[RADIX][m];
        dfa[getIndex(pattern, 0)][0] = 1;
        for (int x = 0, j = 1; j < m; j++) {
            for (var c = 0; c < RADIX; c++) {
                dfa[c][j] = dfa[c][x]; // Copy mismatch cases
            }
            dfa[getIndex(pattern, j)][j] = j + 1; // Set match case
            x = dfa[getIndex(pattern, j)][x]; // Update restart state
        }

        int i;
        int j;
        for (i = 0, j = 0; i < n && j < m; i++) {
            j = dfa[getIndex(text, i)][j];
        }
        if (j == m) {
            return i - m;
        } else {
            return -1;
        }
    }

    private int getIndex(String string, int index) {
        return string.charAt(index) - 65;
    }

    public static void main(String[] args) {
        var knuthMorrisPratt = new KnuthMorrisPratt();
        var index = knuthMorrisPratt.search("AABACAABABACAA", "ABA");
    }
}
