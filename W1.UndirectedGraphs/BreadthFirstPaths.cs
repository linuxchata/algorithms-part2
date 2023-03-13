namespace W1.UndirectedGraphs;

/// <summary>
/// Breadth-First Paths data structure
/// </summary>
public class BreadthFirstPaths
{
    private readonly bool[] _marked;
    private readonly int[] _edgeTo;

    public BreadthFirstPaths(Graph g, int s)
    {
        var size = g.GetSize();
        _edgeTo = new int[size];
        _marked = new bool[size];

        Bfp(g, s);
    }

    private void Bfp(Graph g, int s)
    {
        var queue = new Queue<int>();
        queue.Enqueue(s);
        _marked[s] = true;

        while (queue.Any())
        {
            int v = queue.Dequeue();
            foreach (var w in g.GetAdjacent(v))
            {
                if (!_marked[w])
                {
                    queue.Enqueue(w);
                    _marked[w] = true;
                    _edgeTo[w] = v;
                }
            }
        }
    }
}