namespace W5.RegularExpressions;

/// <summary>
/// Directed Depth First Search datastructure
/// https://algs4.cs.princeton.edu/42digraph/DirectedDFS.java.html
/// Determine single-source or multiple-source reachability in a digraph using depth first search
/// </summary>
public sealed class DirectedDfs
{
    private readonly Digraph _g;
    private readonly bool[] _marked;

    /// <summary>
    /// Find vertices reachable from source
    /// </summary>
    /// <param name="g"></param>
    /// <param name="source"></param>
    public DirectedDfs(Digraph g, int source)
    {
        _g = g;
        _marked = new bool[g.V()];

        Dfs(source);
    }

    /// <summary>
    /// Find vertices reachable from sources
    /// </summary>
    /// <param name="g"></param>
    /// <param name="sources"></param>
    public DirectedDfs(Digraph g, List<int> sources)
    {
        _g = g;
        _marked = new bool[g.V()];

        foreach (var v in sources)
        {
            if (!_marked[v])
            {
                Dfs(v);
            }
        }
    }

    /// <summary>
    /// Is v reachable from source(s)?
    /// </summary>
    /// <param name="v"></param>
    /// <returns></returns>
    public bool IsMarked(int v)
    {
        return _marked[v];
    }

    private void Dfs(int v)
    {
        _marked[v] = true;
        foreach (var w in _g.GetAdjacent(v))
        {
            if (!_marked[w])
            {
                Dfs(w);
            }
        }
    }
}