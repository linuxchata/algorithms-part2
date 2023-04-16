namespace W3.RadixSorts;

public class Program
{
    public static void Main(string[] args)
    {
        // Key-indexed counting
        var a = new char[] { 'd', 'a', 'c', 'f', 'f', 'b', 'd', 'b', 'f', 'b', 'e', 'a', };
        var keyIndexCounting = new KeyIndexCounting();
        keyIndexCounting.Sort(a);

        // Least-significant-digit-first string sort
        var b = new string[] { "dab", "add", "cab", "fad", "fee", "bad", "dad", "bee", "fed", "bed", "ebb", "ace", };
        var leastSignificantDigitFirstStringSort = new LeastSignificantDigitFirstStringSort();
        leastSignificantDigitFirstStringSort.Sort(b, 3);

        var c = new string[] { "4PGC938", "2IYE230", "3CIO720", "1ICK750", "1OHV845", "4JZY524", "1ICK750", "3CIO720", "1OHV845", "1OHV845", "2RLA629", "2RLA629", "3ATW723", };
        leastSignificantDigitFirstStringSort.Sort(c, 7);

        // Most-significant-digit-first string sort
        var d = new string[] { "she", "sells", "seashells", "by", "the", "sea", "shore", "the", "shells", "she", "sells", "are", "surely", "seashells", };
        var mostSignificantDigitFirstStringSort = new MostSignificantDigitFirstStringSort();
        mostSignificantDigitFirstStringSort.Sort(d);

        // 3-way string quicksort
        var e = new string[] { "she", "sells", "seashells", "by", "the", "sea", "shore", "the", "shells", "she", "sells", "are", "surely", "seashells", };
        var threeWayStringQuicksort = new ThreeWayStringQuicksort();
        threeWayStringQuicksort.Sort(e);

        // Longest repeated substring
        var f = "aacaagtttacaagc";
        var longestRepeatedSubstring = new LongestRepeatedSubstring();
        longestRepeatedSubstring.Find(f);

        Console.ReadKey();
    }
}