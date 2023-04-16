namespace W3.RadixSorts;

/// <summary>
/// Longest repeated substring
/// </summary>
public sealed class LongestRepeatedSubstring
{
    public string Find(string a)
    {
        var n = a.Length;

        // Create suffixes
        var suffixes = new string[n];
        for (int i = 0; i < n; i++)
        {
            suffixes[i] = a.Substring(i, n - i);
        }

        // Sort suffixes
        Array.Sort(suffixes);

        // Find longest common path between adjacent suffixes in sorted order
        var lrs = string.Empty;
        for (var i = 0; i < n - 1; i++)
        {
            var length = LongestCommonPath(suffixes[i], suffixes[i + 1]);
            if (length > lrs.Length)
            {
                lrs = suffixes[i].Substring(0, length);
            }
        }

        return lrs;
    }

    private int LongestCommonPath(string a, string b)
    {
        var i = 0;
        for (; i < a.Length && i < b.Length; i++)
        {
            if (a[i] != b[i])
            {
                break;
            }
        }

        return i;
    }
}