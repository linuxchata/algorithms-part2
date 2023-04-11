namespace W3.MaximumFlowAndMinimumCut;

public class Program
{
    public static void Main(string[] args)
    {
        var flowNetwork = new FlowNetwork(6);
        flowNetwork.AddEdge(new FlowEdge(0, 1, 2.0));
        flowNetwork.AddEdge(new FlowEdge(0, 2, 3.0));
        flowNetwork.AddEdge(new FlowEdge(1, 3, 3.0));
        flowNetwork.AddEdge(new FlowEdge(1, 4, 1.0));
        flowNetwork.AddEdge(new FlowEdge(2, 3, 1.0));
        flowNetwork.AddEdge(new FlowEdge(2, 4, 1.0));
        flowNetwork.AddEdge(new FlowEdge(3, 5, 2.0));
        flowNetwork.AddEdge(new FlowEdge(4, 5, 3.0));

        var fordFulkerson = new FordFulkerson(flowNetwork, 0, 5);
        var value = fordFulkerson.GetValue();

        Console.ReadKey();
    }
}