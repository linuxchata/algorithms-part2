namespace W2.ShortestPaths;

/// <summary>
/// Acyclic shortest paths
/// </summary>
public sealed class AcyclicShortestPath
{
    private readonly DirectedEdge[] _edgeTo;
    private readonly double[] _distTo;

    public AcyclicShortestPath(EdgeWeightedDigraph g, int s)
    {
        _edgeTo = new DirectedEdge[g.V()];

        _distTo = new double[g.V()];
        for (int i = 0; i < g.V(); i++)
        {
            _distTo[i] = double.PositiveInfinity;
        }
        _distTo[s] = 0.0;

        var topologicalOrder = new TopologicalOrder(g);
        foreach (var v in topologicalOrder.GetOrder())
        {
            foreach (var edge in g.GetAdjacent(v))
            {
                Relax(edge);
            }
        }
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