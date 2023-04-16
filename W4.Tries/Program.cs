namespace W4.Tries;

public class Program
{
    public static void Main()
    {
        var trie = new TriesSymbolTable();
        trie.Put("by", 4);
        trie.Put("sea", 6);
        trie.Put("shore", 7);
        trie.Get("shore");
        trie.Get("sell");

        Console.ReadKey();
    }
}