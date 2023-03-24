import edu.princeton.cs.algs4.Digraph;

public class SAP {
    /**
     * Constructor takes a digraph (not necessarily a DAG)
     */
    public SAP(Digraph G) {

    }

    /**
     * Length of shortest ancestral path between v and w; -1 if no such path
     */
    public int length(int v, int w) {
        return -1;
    }

    /**
     * A common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
     */
    public int ancestor(int v, int w) {
        return -1;
    }

    /**
     * Length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
     */
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        return -1;
    }

    /**
     * A common ancestor that participates in shortest ancestral path; -1 if no such path
     */
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        return -1;
    }

    public static void main(String[] args) {
    }
}
