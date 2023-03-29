namespace W2.MinimumSpanningTrees;

/// <summary>
/// Edge-weighted graph data structure
/// </summary>
public sealed class EdgeWeightedGraph
{
    private readonly int _v;
    private readonly List<Edge> _allEdges;
    private readonly List<Edge>[] _adjacencyList;

    public EdgeWeightedGraph(int v)
    {
        _v = v;
        _allEdges = new List<Edge>();
        _adjacencyList = new List<Edge>[_v];
        for (int i = 0; i < _v; i++)
        {
            _adjacencyList[i] = new List<Edge>();
        }
    }

    /// <summary>
    /// Edges incident to v
    /// </summary>
    /// <param name="v"></param>
    /// <returns></returns>
    public List<Edge> GetAdjacent(int v)
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

    public List<Edge> GetEdges()
    {
        return _allEdges;
    }

    /// <summary>
    /// Add weighted edge e to this graph
    /// </summary>
    /// <param name="e"></param>
    public void AddEdge(Edge e)
    {
        _allEdges.Add(e);

        var v = e.Either();
        var w = e.Other(v);
        _adjacencyList[v].Add(e);
        _adjacencyList[w].Add(e);
    }
}