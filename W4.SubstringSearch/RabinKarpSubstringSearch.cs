namespace W4.SubstringSearch;

/// <summary>
/// Rabin–Karp substring search
/// </summary>
public sealed class RabinKarpSubstringSearch
{
    private const short Q = 997;
    private const short Radix = 10; // Only digit characters in the text string and pattern

    private readonly List<char> _charSet;

    public RabinKarpSubstringSearch()
    {
        _charSet = "0123456789".ToCharArray().ToList();
    }

    public int Search(string text, string pattern)
    {
        return -1;
    }

    /// <summary>
    /// Compute hash for M-digit key
    /// </summary>
    /// <param name="key"></param>
    /// <param name="m"></param>
    /// <returns>Hash code</returns>
    private long GetHashCode(string key, int m)
    {
        long hash = 0;
        for (var j = 0; j < m; j++)
        {
            hash = (Radix * hash + GetCharIndex(key, j)) % Q;
        }
        return hash;
    }

    private int GetCharIndex(string @string, int i)
    {
        var c = @string[i];
        var index = _charSet.IndexOf(c);
        return index;
    }
}