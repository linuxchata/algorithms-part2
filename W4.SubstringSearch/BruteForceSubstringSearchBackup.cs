namespace W4.SubstringSearch;

/// <summary>
/// Brute-force substring search with backup
/// </summary>
public sealed class BruteForceSubstringSearchBackup
{
    public int Search(string text, string pattern)
    {
        Console.WriteLine(nameof(BruteForceSubstringSearchBackup));

        int i;
        int j;
        var n = text.Length;
        var m = pattern.Length;

        for (i = 0, j = 0; i < n && j < m; i++)
        {
            Console.WriteLine($"Text character: {text[i]}, i: {i}; pattern character: {pattern[j]}, j: {j}");

            if (text[i] == pattern[j])
            {
                j++;
            }
            else
            {
                i -= j;
                j = 0;
            }
        }

        if (j == m)
        {
            return i - m;
        }
        {
            return n; // Not found
        }
    }
}