namespace W3.MaximumFlowAndMinimumCut;

// Ford-Fulkerson with BFS (breadth-first search)
public sealed class FordFulkerson
{
    private readonly FlowNetwork _g;
    private readonly int _s;
    private readonly int _t;

    private bool[] _marked = null!; // True if s -> v path in residential network
    private FlowEdge[] _edgeTo = null!; // Last edge on s -> v path
    private double _value; // Value of flow

    public FordFulkerson(FlowNetwork g, int s, int t)
    {
        _g = g;
        _s = s;
        _t = t;

        _value = 0.0;
        while (HasAugmentingPath())
        {
            // Compute bottleneck capacity
            var bottleneck = double.PositiveInfinity;
            for (var v = t; v != s; v = _edgeTo[v].Other(v))
            {
                bottleneck = Math.Min(bottleneck, _edgeTo[v].GetResidualCapacityTo(v));
            }

            // Augment flow
            for (var v = t; v != s; v = _edgeTo[v].Other(v))
            {
                _edgeTo[v].AddResidualCapacityTo(v, bottleneck);
            }

            _value += bottleneck;
        }
    }

    /// <summary>
    /// Get maximum flow
    /// </summary>
    /// <returns></returns>
    public double GetValue()
    {
        return _value;
    }

    /// <summary>
    /// Is v reachable from s in residential network?
    /// </summary>
    /// <param name="v"></param>
    /// <returns></returns>
    public bool InCut(int v)
    {
        return _marked[v];
    }

    private bool HasAugmentingPath()
    {
        _marked = new bool[_g.GetV()];
        _edgeTo = new FlowEdge[_g.GetV()];

        var q = new Queue<int>();
        q.Enqueue(_s);

        _marked[_s] = true;

        while (q.Any())
        {
            var v = q.Dequeue();
            var adjacents = _g.GetAdjacent(v);
            foreach (var edge in adjacents)
            {
                var w = edge.Other(v);
                if (edge.GetResidualCapacityTo(w) > 0 && !_marked[w]) // Is there a path from s to w in the residual network?
                {
                    _edgeTo[w] = edge;
                    _marked[w] = true;
                    q.Enqueue(w);
                }
            }
        }

        return _marked[_t];
    }
}