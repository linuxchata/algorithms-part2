namespace W3.MaximumFlowAndMinimumCut;

/// <summary>
/// Flow network data structure
/// </summary>
public sealed class FlowNetwork
{
    private readonly int _v;
    private readonly List<FlowEdge> _allEdges;
    public readonly List<FlowEdge>[] _adjacencyList;

    public FlowNetwork(int v)
    {
        _v = v;
        _allEdges = new List<FlowEdge>();
        _adjacencyList = new List<FlowEdge>[v];
        for (int i = 0; i < v; i++)
        {
            _adjacencyList[i] = new List<FlowEdge>();
        }
    }

    public void AddEdge(FlowEdge e)
    {
        _allEdges.Add(e);

        var v = e.From();
        var w = e.To();

        _adjacencyList[v].Add(e); // Add forward edge
        _adjacencyList[w].Add(e); // Add backward edge
    }

    public List<FlowEdge> GetAdjacent(int v)
    {
        return _adjacencyList[v];
    }

    public List<FlowEdge> GetEdges()
    {
        return _allEdges;
    }

    public int GetV()
    {
        return _v;
    }
}