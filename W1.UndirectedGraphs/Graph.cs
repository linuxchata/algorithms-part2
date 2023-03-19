﻿namespace W1.UndirectedGraphs;

/// <summary>
/// Graph data structure
/// </summary>
public class Graph
{
    private readonly int _v;
    private readonly List<int>[] _adjacencyList;

    public Graph(int v)
    {
        _v = v;
        _adjacencyList = new List<int>[_v];
        for (int i = 0; i < _v; i++)
        {
            _adjacencyList[i] = new List<int>();
        }
    }

    public List<int> GetAdjacent(int v)
    {
        return _adjacencyList[v];
    }

    public int GetSize()
    {
        return _v;
    }

    public void AddEdge(int v, int w)
    {
        _adjacencyList[v].Add(w);
        _adjacencyList[w].Add(v);
    }
}