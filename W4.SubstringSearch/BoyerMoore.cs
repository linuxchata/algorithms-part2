namespace W4.SubstringSearch;

/// <summary>
/// Boyer-Moore substring search
/// </summary>
public sealed class BoyerMooreSubstringSearch
{
    private const short Radix = 26; // Only English alphabet characters in the text string and pattern

    private readonly List<char> _charSet;

    public BoyerMooreSubstringSearch()
    {
        _charSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".ToCharArray().ToList();
    }

    public int Search(string text, string pattern)
    {
        var n = text.Length;
        var m = pattern.Length;

        // Boyer-Moore skip table computation
        var right = new int[Radix];
        for (var c = 0; c < Radix; c++)
        {
            right[c] = -1;
        }
        for (var j = 0; j < m; j++)
        {
            var k = GetCharIndex(pattern, j);
            right[k] = j;
        }

        // Boyer-Moore implementation
        int skip;
        for (var i = 0; i <= n - m; i += skip)
        {
            skip = 0;
            for (var j = m - 1; j > 0; j--)
            {
                var a = GetCharIndex(pattern, j);
                var b = GetCharIndex(text, i + j);
                if (a != b)
                {
                    skip = Math.Max(1, j - right[b]);
                    break;
                }
            }
            if (skip == 0)
            {
                return i; // Match
            }
        }

        return n;
    }

    private int GetCharIndex(string @string, int i)
    {
        var c = @string[i];
        var index = _charSet.IndexOf(c);
        return index;
    }
}