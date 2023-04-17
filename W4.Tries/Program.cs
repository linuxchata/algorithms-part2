using System.Threading;

namespace W4.Tries;

public class Program
{
    public static void Main()
    {
        // R-way Tries
        var triesSymbolTable = new TriesSymbolTable();
        triesSymbolTable.Put("by", 4);
        triesSymbolTable.Put("sea", 6);
        triesSymbolTable.Put("sells", 1);
        triesSymbolTable.Put("she", 0);
        triesSymbolTable.Put("shells", 3);
        triesSymbolTable.Put("shore", 7);
        triesSymbolTable.Put("the", 5);
        triesSymbolTable.Get("shore");
        triesSymbolTable.Get("sell");
        triesSymbolTable.GetKeys();
        triesSymbolTable.GetKeysWithPrefix("sh");
        var longestPrefix = triesSymbolTable.GetLongestPrefixOf("shellsshells");

        // Ternary (composed of three parts) Search Tries
        var ternarySearchTries = new TernarySearchTries();
        ternarySearchTries.Put("she", 0);
        ternarySearchTries.Put("the", 5);
        ternarySearchTries.Get("sell");
        ternarySearchTries.Get("the");

        Console.ReadKey();
    }
}