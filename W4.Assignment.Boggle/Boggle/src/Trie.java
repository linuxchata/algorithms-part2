public class Trie {
    private static final int R = 26;
    private static final int CHARS_SHIFT = 65;
    private Trie.Node root; // Root of trie

    // R-way trie node
    private static class Node {
        private final Trie.Node[] next = new Trie.Node[R];
        private boolean isString;
    }

    public boolean contains(StringBuilder key) {
        if (key == null) {
            throw new IllegalArgumentException("argument to contains() is null");
        }

        var x = get(root, key, 0);
        if (x == null) {
            return false;
        }

        return x.isString;
    }

    private Trie.Node get(Trie.Node x, StringBuilder key, int d) {
        if (x == null) {
            return null;
        }

        if (d == key.length()) {
            return x;
        }

        return get(x.next[(key.charAt(d) - CHARS_SHIFT)], key, d + 1);
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

    public boolean containsKeysWithPrefix(StringBuilder prefix) {
        return get(root, prefix, 0) != null;
    }
}
