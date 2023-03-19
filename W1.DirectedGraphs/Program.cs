namespace W1.DirectedGraphs;

public class Program
{
    public static async Task Main(string[] args)
    {
        // Digraph
        var digraph = new Digraph(6);
        digraph.AddEdge(5, 0);
        digraph.AddEdge(2, 4);
        digraph.AddEdge(3, 2);
        digraph.AddEdge(1, 2);
        digraph.AddEdge(0, 1);
        digraph.AddEdge(4, 3);
        digraph.AddEdge(3, 5);
        digraph.AddEdge(0, 2);

        var s = 0;

        // Depth-First Paths
        var dps = new DepthFirstPaths(digraph, s);

        // Breadth-First Paths
        var bfp = new BreadthFirstPaths(digraph, s);

        // Web Crawler
        var webCrawler = new WebCrawler();
        // await webCrawler.Crawl();

        // Topoligical search
        var digraph2 = new Digraph(7);
        digraph2.AddEdge(0, 5);
        digraph2.AddEdge(0, 1);
        digraph2.AddEdge(3, 5);
        digraph2.AddEdge(5, 2);
        digraph2.AddEdge(6, 0);
        digraph2.AddEdge(1, 4);
        digraph2.AddEdge(0, 2);
        digraph2.AddEdge(3, 6);
        digraph2.AddEdge(3, 4);
        digraph2.AddEdge(6, 4);
        digraph2.AddEdge(3, 2);

        var dfo = new DepthFirstOrder(digraph2);
        var order = dfo.ReserverPost();
        Console.WriteLine("Topoligical search");
        Console.WriteLine(string.Join(",", order));
        Console.WriteLine();

        // Kosaraju-Sharir Strongly Connected Components
        var scc = new KosarajuSharirStronglyConnectedComponents(digraph2);
        scc.AreStronglyConnected(0, 2);

        Console.ReadKey();
    }
}