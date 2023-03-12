namespace W1.UndirectedGraphs;

/// <summary>
/// DepthFirstPaths data structure
/// </summary>
public sealed class DepthFirstPaths
{
    private readonly bool[] _marked;
    private readonly int[] _edgeTo;
    private readonly int _s;

    public DepthFirstPaths(Graph g, int s)
    {
        _s = s;

        var size = g.GetSize();
        _edgeTo = new int[size];
        _marked = new bool[size];
        Dfp(g, s);
    }

    private void Dfp(Graph g, int v)
    {
        _edgeTo[v] = v;
        _marked[v] = true;
        foreach (int w in g.GetAdjacent(v))
        {
            if (!_marked[w])
            {
                Dfp(g, w);
                _edgeTo[w] = v;
            }
        }
    }

    public bool HasPathTo(int v)
    {
        return _marked[v];
    }

    public List<int> PathTo(int v)
    {
        if (!HasPathTo(v))
        {
            return null!;
        }

        var result = new Stack<int>();

        for (int x = v; x != _s; x = _edgeTo[x])
        {
            result.Push(x);
        }

        result.Push(_s);

        return result.ToList();
    }
}