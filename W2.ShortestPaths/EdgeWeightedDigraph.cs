namespace W2.ShortestPaths;

/// <summary>
/// Edge-weighted digraph data structure
/// </summary>
public sealed class EdgeWeightedDigraph
{
    private readonly int _v;
    private readonly List<DirectedEdge>[] _adjacencyList;

    public EdgeWeightedDigraph(int v)
    {
        _v = v;
        _adjacencyList = new List<DirectedEdge>[_v];
        for (int i = 0; i < _v; i++)
        {
            _adjacencyList[i] = new List<DirectedEdge>();
        }
    }

    /// <summary>
    /// Edges incident from v
    /// </summary>
    /// <param name="v"></param>
    /// <returns></returns>
    public List<DirectedEdge> GetAdjacent(int v)
    {
        return _adjacencyList[v];
    }

    /// <summary>
    /// Number of vertices
    /// </summary>
    /// <returns></returns>
    public int V()
    {
        return _v;
    }

    /// <summary>
    /// Add weighted directed edge e to this graph
    /// </summary>
    /// <param name="e"></param>
    public void AddEdge(DirectedEdge e)
    {
        var v = e.From();
        _adjacencyList[v].Add(e);
    }
}