namespace W3.RadixSorts;

/// <summary>
/// Most-significant-digit-first string sort
/// </summary>
public sealed class MostSignificantDigitFirstStringSort
{
    private const short Radix = 256; // radix, 256 possible characters in ASCII

    public void Sort(string[] a)
    {
        var aux = new string[a.Length];
        Sort(a, aux, 0, a.Length - 1, 0);
    }

    private void Sort(string[] a, string[] aux, int lo, int hi, int d)
    {
        if (hi <= lo)
        {
            return;
        }

        var count = new int[Radix + 2];

        // Count the frequency of characters at this position
        for (var i = lo; i <= hi; i++)
        {
            var j = CharAt(a[i], d) + 2;
            count[j]++;
        }

        // Compute frequency cumulates which specify destinations
        for (var ri = 0; ri < Radix + 1; ri++)
        {
            count[ri + 1] += count[ri];
        }

        // Access cumulates using key as index to move items
        for (var i = lo; i <= hi; i++)
        {
            var j = CharAt(a[i], d) + 1;
            var k = count[j]++;
            aux[k] = a[i];
        }

        // Copy back into original array
        for (var i = lo; i <= hi; i++)
        {
            a[i] = aux[i - lo];
        }

        // Sort R subarrays recursively
        for (var radixi = 0; radixi < Radix; radixi++)
        {
            var newLo = lo + count[radixi];
            var newHi = lo + count[radixi + 1] - 1;
            Sort(a, aux, newLo, newHi, d + 1);
        }
    }

    private int CharAt(string s, int d)
    {
        if (d < s.Length)
        {
            return s[d];
        }
        else
        {
            return -1;
        };
    }
}