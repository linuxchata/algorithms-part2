namespace W2.ShortestPaths;

/// <summary>
/// Single-source shortest paths
/// </summary>
public sealed class ShortestPath
{
    private readonly EdgeWeightedDigraph _g;
    private readonly int _s;
    private readonly DirectedEdge[] _edgeTo;
    private readonly double[] _distTo;

    public ShortestPath(EdgeWeightedDigraph g, int s)
    {
        _g = g;
        _s = s;

        _edgeTo = new DirectedEdge[g.V()];
        _distTo = new double[g.V()];
    }

    /// <summary>
    /// Length of shortest path from s to v
    /// </summary>
    /// <param name="v"></param>
    /// <returns></returns>
    public double GetDistanceTo(int v)
    {
        return _distTo[v];
    }

    /// <summary>
    /// Shortest path from s to v
    /// </summary>
    /// <param name="v"></param>
    /// <returns></returns>
    public List<DirectedEdge> GetPathTo(int v)
    {
        var path = new Stack<DirectedEdge>();
        for (var e = _edgeTo[v]; e != null; e = _edgeTo[e.From()])
        {
            path.Push(e);
        }

        return path.ToList();
    }

    private void Relax(DirectedEdge e)
    {
        var v = e.From();
        var w = e.To();
        if (_distTo[w] > _distTo[v] + e.GetWeight())
        {
            _distTo[w] = _distTo[v] + e.GetWeight();
            _edgeTo[w] = e;
        }
    }
}