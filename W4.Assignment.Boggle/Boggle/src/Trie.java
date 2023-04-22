import edu.princeton.cs.algs4.Queue;

public class Trie {
    private static final int R = 26;
    private static final int CHARS_SHIFT = 65;
    private static final int MAX_SIZE = R + CHARS_SHIFT;
    private Trie.Node root; // Root of trie

    // R-way trie node
    private static class Node {
        private final Trie.Node[] next = new Trie.Node[R];
        private boolean isString;
    }

    public boolean contains(String key) {
        if (key == null) {
            throw new IllegalArgumentException("argument to contains() is null");
        }

        var x = get(root, key, 0);
        if (x == null) {
            return false;
        }

        return x.isString;
    }

    private Trie.Node get(Trie.Node x, String key, int d) {
        if (x == null) {
            return null;
        }

        if (d == key.length()) {
            return x;
        }

        var c = key.charAt(d) - CHARS_SHIFT;
        return get(x.next[c], key, d + 1);
    }

    public void add(String key) {
        if (key == null) {
            throw new IllegalArgumentException("argument to add() is null");
        }

        root = add(root, key, 0);
    }

    private Trie.Node add(Trie.Node x, String key, int d) {
        if (x == null) {
            x = new Trie.Node();
        }

        if (d == key.length()) {
            x.isString = true;
        } else {
            var c = key.charAt(d) - CHARS_SHIFT;
            x.next[c] = add(x.next[c], key, d + 1);
        }

        return x;
    }

    public boolean containsKeysWithPrefix(String prefix) {
        var results = new Queue<String>();
        var x = get(root, prefix, 0);
        collect(x, new StringBuilder(prefix), results);
        return !results.isEmpty();
    }

    private void collect(Trie.Node x, StringBuilder prefix, Queue<String> results) {
        if (x == null) {
            return;
        }

        if (x.isString) {
            results.enqueue(prefix.toString());
            return;
        }

        for (char c = CHARS_SHIFT; c < MAX_SIZE; c++) {
            prefix.append(c);
            collect(x.next[c - CHARS_SHIFT], prefix, results);
            if (!results.isEmpty()) {
                return;
            }
            prefix.deleteCharAt(prefix.length() - 1);
        }
    }
}
