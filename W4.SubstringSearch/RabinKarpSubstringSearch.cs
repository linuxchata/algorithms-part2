namespace W4.SubstringSearch;

/// <summary>
/// Rabin–Karp substring search
/// </summary>
public sealed class RabinKarpSubstringSearch
{
    private const short Q = 997; // Modulus
    private const short Radix = 10; // Only digit characters in the text string and pattern

    private readonly List<char> _charSet;
    private readonly long _patterHashValue;
    private readonly int _m; // Pattern length
    private readonly long _rm; // R^(M-1) % Q

    public RabinKarpSubstringSearch(string pattern)
    {
        _charSet = "0123456789".ToCharArray().ToList();

        _m = pattern.Length;
        _rm = 1;

        for (var i = 1; i <= _m - 1; i++)
        {
            _rm = (Radix * _rm) % Q;
        }

        _patterHashValue = GetHashCode(pattern, _m);
    }

    public int Search(string text)
    {
        // Monte Carlo version. Return match if hash match
        var n = text.Length;
        var textHash = GetHashCode(text, _m);
        if (_patterHashValue == textHash)
        {
            return 0;
        }

        for (var i = _m; i < n; i++)
        {
            var c = GetCharIndex(text, i - _m); // Leading character
            textHash = (textHash + Q - _rm * c % Q) % Q;

            var ci = GetCharIndex(text, i); // New trailing character
            textHash = (textHash * Radix + ci) % Q;
            if (_patterHashValue == textHash)
            {
                return i - _m + 1;
            }
        }

        return n;
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
            var c = GetCharIndex(key, j);
            hash = (Radix * hash + c) % Q;
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