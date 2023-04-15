namespace W3.RadixSorts;

public sealed class KeyIndexCounting
{
    private List<char> _charSet;

    public KeyIndexCounting()
    {
        _charSet = "abcdef".ToCharArray().ToList();
    }

    /// <summary>
    /// Sort an array a[] of N integers between 0 and R - 1
    /// </summary>
    /// <param name="a"></param>
    public void Sort(char[] a)
    {
        // 'd', 'a', 'c', 'f', 'f', 'b', 'd', 'b', 'f', 'b', 'e', 'a'
        var r = _charSet.Count; // 6
        var n = a.Length; // 12
        var aux = new char[a.Length];
        var count = new int[r + 1];

        // Count frequencies of each letter using key as index
        for (var i = 0; i < n; i++)
        {
            var j = _charSet.IndexOf(a[i]) + 1;
            count[j]++;
        }

        // count is 0 2 3 1 2 1 3; offset by 1

        // Compute frequency cumulates which specify destinations.
        for (var ri = 0; ri < r; ri++)
        {
            count[ri + 1] += count[ri];
        }

        //          0 1 2 3 4 5 6
        //          a b c d e f -
        // count is 0 2 5 6 8 9 12 - e.g. 5 letters less than 'c', 6 letters less than 'd', etc.

        // Access cumulates using key as index to move items
        for (var i = 0; i < n; i++)
        {
            var j = _charSet.IndexOf(a[i]);
            var k = count[j]++;
            aux[k] = a[i];
        }

        //          a b c d e f  -
        // count is 2 5 6 8 9 12 12

        // Copy back into original array
        for (var i = 0; i < n; i++)
        {
            a[i] = aux[i];
        }
    }
}