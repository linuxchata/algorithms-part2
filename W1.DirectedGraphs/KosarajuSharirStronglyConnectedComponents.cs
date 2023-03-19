namespace W1.DirectedGraphs;

public class KosarajuSharirStronglyConnectedComponents
{
    private readonly bool[] _marked;
    private readonly int[] _scc;
    private readonly int _count;

    public KosarajuSharirStronglyConnectedComponents(Digraph g)
    {
        _marked = new bool[g.GetSize()];
        _scc = new int[g.GetSize()];

        var dfo = new DepthFirstOrder(g); // TODO: Reverse g
        foreach (var v in dfo.ReserverPost())
        {
            if (!_marked[v])
            {
                Dfp(g, v);
                _count++;
            }
        }
    }

    public bool AreStronglyConnected(int v, int w)
    {
        return _scc[v] == _scc[w];
    }

    private void Dfp(Digraph g, int v)
    {
        _marked[v] = true;
        _scc[v] = _count;
        foreach (var w in g.GetAdjacent(v))
        {
            if (!_marked[w])
            {
                Dfp(g, w);
            }
        }
    }
}