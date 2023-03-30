namespace W2.MinimumSpanningTrees;

public class LazyPrimMinimumSpanningTree
{
    private readonly Queue<Edge> _mst;
    private readonly bool[] _marked;
    private readonly PriorityQueue<Edge, double> _priorityQueue;

    public LazyPrimMinimumSpanningTree(EdgeWeightedGraph g)
    {
        _mst = new Queue<Edge>();
        _marked = new bool[g.V()];
        _priorityQueue = new PriorityQueue<Edge, double>();
        Visit(g, 0);

        while (_priorityQueue.Count != 0)
        {
            var edge = _priorityQueue.Dequeue();
            var v = edge.Either();
            var w = edge.Other(v);

            if (_marked[v] && _marked[w])
            {
                continue;
            }

            _mst.Enqueue(edge);

            if (!_marked[v])
            {
                Visit(g, v);
            }
            if (!_marked[w])
            {
                Visit(g, w);
            }
        }
    }

    private void Visit(EdgeWeightedGraph g, int v)
    {
        _marked[v] = true;
        foreach (var edge in g.GetAdjacent(v))
        {
            if (!_marked[edge.Other(v)])
            {
                _priorityQueue.Enqueue(edge, edge.GetWeight());
            }
        }
    }

    public List<Edge> GetEdges()
    {
        return _mst.ToList();
    }
}