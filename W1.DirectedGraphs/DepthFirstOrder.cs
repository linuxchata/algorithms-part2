namespace W1.DirectedGraphs;

/// <summary>
/// Depth-First Order data structure
/// </summary>
public class DepthFirstOrder
{
    private readonly Stack<int> _reversePost;
    private readonly bool[] _marked;

    public DepthFirstOrder(Digraph g)
    {
        _reversePost = new Stack<int>();

        var size = g.GetSize();
        _marked = new bool[size];
        for (int v = 0; v < g.GetSize(); v++)
        {
            if (!_marked[v])
            {
                Dfo(g, v);
            }
        }
    }

    private void Dfo(Digraph g, int v)
    {
        _marked[v] = true;
        foreach (var w in g.GetAdjacent(v))
        {
            if (!_marked[w])
            {
                Dfo(g, w);
            }
        }
        _reversePost.Push(v);
    }

    public List<int> ReserverPost()
    {
        return _reversePost.ToList();
    }
}
