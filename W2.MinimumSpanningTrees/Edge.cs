namespace W2.MinimumSpanningTrees;

/// <summary>
/// Edge abstruction needed for weighted edges
/// </summary>
public sealed class Edge : IComparable<Edge>
{
    private readonly int _v;
    private readonly int _w;
    private readonly double _weight;

    /// <summary>
    /// Create a weighted edge v-w
    /// </summary>
    /// <param name="v"></param>
    /// <param name="w"></param>
    /// <param name="weight"></param>
    public Edge(int v, int w, double weight)
    {
        _v = v;
        _w = w;
        _weight = weight;
    }

    /// <summary>
    /// Either endpoint
    /// </summary>
    /// <returns></returns>
    public int Either()
    {
        return _v;
    }

    /// <summary>
    /// The endpoint that is not v
    /// </summary>
    /// <param name="v"></param>
    /// <returns></returns>
    public int Other(int v)
    {
        return v switch
        {
            var value when value == _v => _w,
            var value when value == _w => _v,
            _ => -1,
        };
        ;
    }

    /// <summary>
    /// Compares this edge to that edge
    /// </summary>
    /// <param name="that"></param>
    /// <returns></returns>
    public int CompareTo(Edge? that)
    {
        if (that is null)
        {
            throw new InvalidOperationException();
        }

        if (GetWeight() > that.GetWeight())
        {
            return 1;
        }
        else if (GetWeight() > that.GetWeight())
        {
            return -1;
        }
        else
        {
            return 0;
        }
    }

    /// <summary>
    /// Get weight
    /// </summary>
    /// <returns></returns>
    public double GetWeight()
    {
        return _weight;
    }

    /// <summary>
    /// String representation
    /// </summary>
    /// <returns></returns>
    public override string ToString()
    {
        return $"{_v}-{_w}";
    }
}
