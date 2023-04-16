namespace W3.RadixSorts;

/// <summary>
/// Least-significant-digit-first string sort
/// </summary>
public sealed class LeastSignificantDigitFirstStringSort
{
    public void Sort(string[] a, int w)
    {
        var radix = 256; // radix, 256 possible characters in ASCII
        var n = a.Length;
        var aux = new string[n];

        for (var d = w - 1; d >= 0; d--)
        {
            var count = new int[radix + 1];

            // Count the frequency of characters at this position
            for (var i = 0; i < n; i++)
            {
                var j = a[i][d] + 1;
                count[j]++;
            }

            // Compute frequency cumulates which specify destinations
            for (var ri = 0; ri < radix; ri++)
            {
                count[ri + 1] += count[ri];
            }

            // Access cumulates using key as index to move items
            for (var i = 0; i < n; i++)
            {
                var j = a[i][d];
                var k = count[j]++;
                aux[k] = a[i];
            }

            // Copy back into original array
            for (var i = 0; i < n; i++)
            {
                a[i] = aux[i];
            }
        }
    }
}