namespace W2.ShortestPaths;

public class Program
{
    public static void Main(string[] args)
    {
        // Indexed minimum priority queue
        var indexedMinPriorityQueue = new IndexedMinPriorityQueue<string>(6);
        indexedMinPriorityQueue.Insert(0, "P");
        indexedMinPriorityQueue.Insert(1, "Q");
        indexedMinPriorityQueue.Insert(2, "E");
        indexedMinPriorityQueue.Insert(3, "X");
        indexedMinPriorityQueue.Insert(4, "A");
        indexedMinPriorityQueue.Insert(5, "M");
        var min = indexedMinPriorityQueue.DeleteMin();

        // Build edge-weighted digraph
        var edgeWeightedDigraph = new EdgeWeightedDigraph(9);
        edgeWeightedDigraph.AddEdge(new DirectedEdge(0, 1, 5.0));
        edgeWeightedDigraph.AddEdge(new DirectedEdge(0, 4, 9.0));
        edgeWeightedDigraph.AddEdge(new DirectedEdge(0, 7, 8.0));
        edgeWeightedDigraph.AddEdge(new DirectedEdge(1, 2, 12.0));
        edgeWeightedDigraph.AddEdge(new DirectedEdge(1, 3, 15.0));
        edgeWeightedDigraph.AddEdge(new DirectedEdge(1, 7, 4.0));
        edgeWeightedDigraph.AddEdge(new DirectedEdge(2, 3, 3.0));
        edgeWeightedDigraph.AddEdge(new DirectedEdge(2, 6, 11.0));
        edgeWeightedDigraph.AddEdge(new DirectedEdge(3, 6, 9.0));
        edgeWeightedDigraph.AddEdge(new DirectedEdge(4, 5, 4.0));
        edgeWeightedDigraph.AddEdge(new DirectedEdge(4, 6, 20.0));
        edgeWeightedDigraph.AddEdge(new DirectedEdge(4, 7, 5.0));
        edgeWeightedDigraph.AddEdge(new DirectedEdge(5, 2, 1.0));
        edgeWeightedDigraph.AddEdge(new DirectedEdge(5, 6, 13.0));
        edgeWeightedDigraph.AddEdge(new DirectedEdge(7, 5, 6.0));
        edgeWeightedDigraph.AddEdge(new DirectedEdge(7, 2, 7.0));

        // Single-source shortest paths
        var shortestPath = new ShortestPath(edgeWeightedDigraph, 0);

        // Dijkstra's single-source shortest paths
        var dijkstraShortestPath = new DijkstraShortestPath(edgeWeightedDigraph, 0);
        var path = dijkstraShortestPath.GetPathTo(6);

        Console.ReadKey();
    }
}