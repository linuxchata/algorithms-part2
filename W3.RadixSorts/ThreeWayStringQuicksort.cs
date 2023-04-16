namespace W3.RadixSorts;

/// <summary>
/// 3-way string quicksort
/// </summary>
public sealed class ThreeWayStringQuicksort
{
    public void Sort(string[] a)
    {
        Sort(a, 0, a.Length - 1, 0);
    }

    private void Sort(string[] a, int lo, int hi, int d)
    {
        if (hi <= lo)
        {
            return;
        }

        var lt = lo;
        var gt = hi;
        var v = CharAt(a[lo], d);
        var i = lo + 1;

        while (i <= gt)
        {
            var t = CharAt(a[i], d);
            if (t < v)
            {
                Exchange(a, lt++, i++);
            }
            else if (t > v)
            {
                Exchange(a, i, gt--);
            }
            else
            {
                i++;
            }
        }

        // Sort 3 subarrays recervisely
        Sort(a, lo, lt - 1, d);
        if (v >= 0)
        {
            Sort(a, lt, gt, d + 1);
        }
        Sort(a, gt + 1, hi, d);
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

    private void Exchange(string[] a, int i, int j)
    {
        var temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }
}