namespace W5.RegularExpressions;

/// <summary>
/// A directed graph, implemented using an array of lists
/// </summary>
public sealed class Digraph
{
    private readonly int _v;
    private readonly List<int>[] _adjacencyList;

    public Digraph(int v)
    {
        _v = v;
        _adjacencyList = new List<int>[_v];
        for (var i = 0; i < _v; i++)
        {
            _adjacencyList[i] = new List<int>();
        }
    }

    public int V()
    {
        return _v;
    }

    public List<int> GetAdjacent(int v)
    {
        return _adjacencyList[v];
    }

    public void AddEdge(int v, int w)
    {
        _adjacencyList[v].Add(w);
    }
}