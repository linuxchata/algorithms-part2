namespace W2.MinimumSpanningTrees;

public class KruskalMinimumSpanningTree
{
    private Queue<Edge> _mst = new Queue<Edge>();

    public KruskalMinimumSpanningTree(EdgeWeightedGraph g)
    {
        var priorityQueue = new PriorityQueue<Edge, double>();
        foreach (var edge in g.GetEdges())
        {
            priorityQueue.Enqueue(edge, edge.GetWeight());
        }

        var uf = new UnionFindQuickFind(g.V());
        while (priorityQueue.Count != 0 && _mst.Count < g.V() - 1)
        {
            var edge = priorityQueue.Dequeue();
            var v = edge.Either();
            var w = edge.Other(v);
            if (!uf.IsConnected(v, w))
            {
                uf.Union(v, w);
                _mst.Enqueue(edge);
            }
        }
    }

    public List<Edge> GetEdges()
    {
        return _mst.ToList();
    }
}