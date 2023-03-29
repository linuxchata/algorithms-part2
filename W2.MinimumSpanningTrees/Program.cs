namespace W2.MinimumSpanningTrees;

public class Program
{
    public static void Main(string[] args)
    {
        var uf = new UnionFindQuickFind(8);
        uf.Union(0, 5);
        uf.Union(3, 5);
        uf.Union(3, 2);
        uf.Union(5, 6);

        // Build edge-weighted graph
        var edgeWeightedGraph = new EdgeWeightedGraph(9);
        edgeWeightedGraph.AddEdge(new Edge(0, 1, 4.0));
        edgeWeightedGraph.AddEdge(new Edge(0, 7, 8.0));
        edgeWeightedGraph.AddEdge(new Edge(1, 2, 8.2));
        edgeWeightedGraph.AddEdge(new Edge(1, 7, 11.0));
        edgeWeightedGraph.AddEdge(new Edge(2, 3, 7.0));
        edgeWeightedGraph.AddEdge(new Edge(2, 8, 2.0));
        edgeWeightedGraph.AddEdge(new Edge(2, 5, 4.5));
        edgeWeightedGraph.AddEdge(new Edge(3, 4, 9.0));
        edgeWeightedGraph.AddEdge(new Edge(3, 5, 14.0));
        edgeWeightedGraph.AddEdge(new Edge(4, 5, 10.0));
        edgeWeightedGraph.AddEdge(new Edge(5, 6, 2.7));
        edgeWeightedGraph.AddEdge(new Edge(6, 7, 1.0));
        edgeWeightedGraph.AddEdge(new Edge(6, 8, 6.0));
        edgeWeightedGraph.AddEdge(new Edge(7, 8, 7.0));

        // Kruskal minimum spanning tree
        var kruskalMinimumSpanningTree = new KruskalMinimumSpanningTree(edgeWeightedGraph);
        var mst = kruskalMinimumSpanningTree.GetEdges();

        Console.ReadKey();
    }
}