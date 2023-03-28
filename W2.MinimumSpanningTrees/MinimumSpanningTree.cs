namespace W2.MinimumSpanningTrees;

public sealed class MinimumSpanningTree
{
    private readonly EdgeWeightedGraph _g;

    public MinimumSpanningTree(EdgeWeightedGraph g)
    {
        _g = g;
    }

    /// <summary>
    /// Edge in minimum spanning tree
    /// </summary>
    /// <returns></returns>
    public List<Edge> Edges()
    {
        return new List<Edge>();
    }
}