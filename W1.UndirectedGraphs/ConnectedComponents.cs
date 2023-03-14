namespace W1.UndirectedGraphs;

/// <summary>
/// Connected Components data structure
/// </summary>
public class ConnectedComponents
{
    private readonly bool[] _marked;
    private readonly int[] _cc;
    private int _count;

    public ConnectedComponents(Graph g)
    {
        _marked = new bool[g.GetSize()];
        _cc = new int[g.GetSize()];

        for (int v = 0; v < g.GetSize(); v++)
        {
            if (!_marked[v])
            {
                Dfp(g, v);
                _count++;
            }
        }
    }

    public int Count()
    {
        return _count;
    }

    public int Cc(int v)
    {
        return _cc[v];
    }

    private void Dfp(Graph g, int v)
    {
        _marked[v] = true;
        _cc[v] = _count;
        foreach (int w in g.GetAdjacent(v))
        {
            if (!_marked[w])
            {
                Dfp(g, w);
            }
        }
    }
}
