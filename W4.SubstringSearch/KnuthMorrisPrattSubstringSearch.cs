namespace W4.SubstringSearch;

/// <summary>
/// Knuth–Morris–Pratt substring search
/// </summary>
public sealed class KnuthMorrisPrattSubstringSearch
{
    private const short Radix = 3; // Only A, B and C characters in the text string and pattern

    private readonly List<char> _charSet;

    /// <summary>
    /// Deterministic finite state automaton (DFA)
    /// DFA is abstract string-searching machine
    /// </summary>
    private int[,] _dfa = null!;

    public KnuthMorrisPrattSubstringSearch()
    {
        _charSet = "ABC".ToCharArray().ToList();
    }

    public int Search(string text, string pattern)
    {
        int i;
        int j;
        var n = text.Length;
        var m = pattern.Length;

        BuildDfa(pattern, m);

        for (i = 0, j = 0; i < n && j < m; i++) // Text pointer i never decrements
        {
            var k = GetCharIndex(text, i);
            j = _dfa[k, j];
        }

        if (j == m)
        {
            return i - m;
        }
        else
        {
            return n;
        }
    }

    private void BuildDfa(string pattern, int m)
    {
        _dfa = new int[Radix, m];
        _dfa[GetCharIndex(pattern, 0), 0] = 1;
        for (int x = 0, j = 1; j < m; j++)
        {
            for (var c = 0; c < Radix; c++)
            {
                _dfa[c, j] = _dfa[c, x]; // Copy mismatch cases
            }
            var k = GetCharIndex(pattern, j);
            _dfa[k, j] = j + 1; // Set match cases
            x = _dfa[k, x]; // Update restart state
        }
    }

    private int GetCharIndex(string @string, int i)
    {
        var index = _charSet.IndexOf(@string[i]);
        return index;
    }
}