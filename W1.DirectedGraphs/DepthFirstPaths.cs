namespace W1.DirectedGraphs;

/// <summary>
/// Depth-First Paths data structure
/// </summary>
public class DepthFirstPaths
{
    private readonly bool[] _marked;
    private readonly int[] _edgeTo;

    public DepthFirstPaths(Digraph g, int s)
    {
        var size = g.GetSize();
        _edgeTo = new int[size];
        _marked = new bool[size];
        Dfp(g, s);
    }

    private void Dfp(Digraph g, int v)
    {
        _edgeTo[v] = v;
        _marked[v] = true;
        foreach (var w in g.GetAdjacent(v))
        {
            if (!_marked[w])
            {
                Dfp(g, w);
                _edgeTo[w] = v;
            }
        }
    }
}
