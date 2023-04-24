namespace W5.RegularExpressions;

public class Program
{
    public static void Main(string[] args)
    {
        var nondeterministicFiniteAutomaton = new NondeterministicFiniteAutomaton("((A*B|AC)D)");
        nondeterministicFiniteAutomaton.Recognize("AABD");

        Console.ReadKey();
    }
}