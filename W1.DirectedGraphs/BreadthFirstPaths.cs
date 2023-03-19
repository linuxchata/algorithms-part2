using System.Security.Cryptography;

namespace W1.DirectedGraphs;

/// <summary>
/// Breadth-First Paths data structure
/// </summary>
public class BreadthFirstPaths
{
    private readonly bool[] _marked;
    private readonly int[] _edgeTo;

    public BreadthFirstPaths(Digraph g, int s)
    {
        var size = g.GetSize();
        _edgeTo= new int[size];
        _marked= new bool[size];

        Bfp(g, s);
    }

    private void Bfp(Digraph g, int s)
    {
        var queue = new Queue<int>();
        queue.Enqueue(s);
        _marked[s] = true;

        while (queue.Any())
        {
            var v = queue.Dequeue();
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