namespace W4.SubstringSearch;

/// <summary>
/// Brute-force substring search
/// </summary>
public sealed class BruteForceSubstringSearch
{
    public int Search(string text, string pattern)
    {
        Console.WriteLine(nameof(BruteForceSubstringSearch));

        var n = text.Length;
        var m = pattern.Length;

        for (var i = 0; i <= n - m; i++)
        {
            int j;
            for (j = 0; j < m; j++)
            {
                Console.WriteLine($"Text character: {text[i + j]}, i: {i}; pattern character: {pattern[j]}, j: {j}");

                if (text[i + j] != pattern[j])
                {
                    break;
                }
            }

            if (j == m)
            {
                return i; // Index in text where pattern starts
            }
        }

        return n; // Not found
    }
}