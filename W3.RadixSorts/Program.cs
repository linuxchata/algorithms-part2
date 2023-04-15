namespace W3.RadixSorts;

public class Program
{
    public static void Main(string[] args)
    {
        // Key-indexed counting
        var a = new char[] { 'd', 'a', 'c', 'f', 'f', 'b', 'd', 'b', 'f', 'b', 'e', 'a' };
        var keyIndexCounting = new KeyIndexCounting();
        keyIndexCounting.Sort(a);

        Console.ReadKey();
    }
}