namespace W1.UndirectedGraphs;

public class Program
{
    public static void Main(string[] args)
    {
        // Graph
        var graph = new Graph(12);
        graph.AddEdge(0, 6);
        graph.AddEdge(0, 1);
        graph.AddEdge(0, 2);
        graph.AddEdge(0, 5);
        graph.AddEdge(6, 4);
        graph.AddEdge(4, 3);
        graph.AddEdge(4, 5);

        var s = 0;

        // Adjacent
        var adjacent = graph.GetAdjacent(s);
        Console.WriteLine($"Adjacent for vertex {s}");
        Console.WriteLine(string.Join(",", adjacent));
        Console.WriteLine();

        // Depth-First Paths
        var v = 5;
        var dps = new DepthFirstPaths(graph, s);
        var path = dps.PathTo(v);
        Console.WriteLine("Depth-First Paths");
        Console.WriteLine($"Path from vertex {s} to vertex {v}");
        Console.WriteLine(string.Join(" => ", path));

        // Breadth-First Paths
        var bfp = new BreadthFirstPaths(graph, s);

        Console.ReadKey();
    }
}