namespace W4.SubstringSearch;

public class Program
{
    public static void Main(string[] args)
    {
        // Brute-force substring search
        var bruteForceSubstringSearch = new BruteForceSubstringSearch();
        var index = bruteForceSubstringSearch.Search("ABACADABRAC", "ABRA");

        // Brute-force substring search with backup
        var bruteForceSubstringSearchBackup = new BruteForceSubstringSearchBackup();
        var index2 = bruteForceSubstringSearchBackup.Search("ABACADABRAC", "ABRA");

        // Knuth–Morris–Pratt substring search
        var knuthMorrisPrattSubstringSearch = new KnuthMorrisPrattSubstringSearch();
        var index3 = knuthMorrisPrattSubstringSearch.Search("AABACAABABACAA", "ABABAC");

        var boyerMooreSubstringSearch = new BoyerMooreSubstringSearch();
        var index4 = boyerMooreSubstringSearch.Search("FINDINAHAYSTACKNEEDLEINA", "NEEDLE");

        Console.ReadKey();
    }
}