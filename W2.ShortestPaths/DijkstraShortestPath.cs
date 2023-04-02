namespace W2.ShortestPaths;

/// <summary>
/// Dijkstra's single-source shortest paths
/// </summary>
public sealed class DijkstraShortestPath
{
    private readonly IndexedMinPriorityQueue<double> _priorityQueue;
    private readonly DirectedEdge[] _edgeTo;
    private readonly double[] _distTo;

    public DijkstraShortestPath(EdgeWeightedDigraph g, int s)
    {
        _edgeTo = new DirectedEdge[g.V()];

        _distTo = new double[g.V()];
        for (int i = 0; i < g.V(); i++)
        {
            _distTo[i] = double.PositiveInfinity;
        }
        _distTo[s] = 0.0;

        _priorityQueue = new IndexedMinPriorityQueue<double>(g.V());
        _priorityQueue.Insert(s, 0.0);

        while (_priorityQueue.Count() > 0)
        {
            var v = _priorityQueue.DeleteMin();
            foreach (var e in g.GetAdjacent(v))
            {
                Relax(e);
            }
        }
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

            if (_priorityQueue.Contains(w))
            {
                _priorityQueue.DecreaseKey(w, _distTo[w]);
            }
            else
            {
                _priorityQueue.Insert(w, _distTo[w]);
            }
        }
    }
}