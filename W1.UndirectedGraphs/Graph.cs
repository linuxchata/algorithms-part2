namespace W1.UndirectedGraphs;

/// <summary>
/// Graph data structure
/// </summary>
public class Graph
{
    private readonly int _v;
    private readonly List<int>[] adjacencyList;

    public Graph(int v)
    {
        _v = v;
        adjacencyList = new List<int>[_v];
        for (int i = 0; i < _v; i++)
        {
            adjacencyList[i] = new List<int>();
        }
    }

    public List<int> GetAdjacent(int v)
    {
        return adjacencyList[v];
    }

    public int GetSize()
    {
        return _v;
    }

    public void AddEdge(int v, int w)
    {
        adjacencyList[v].Add(w);
        adjacencyList[w].Add(v);
    }
}