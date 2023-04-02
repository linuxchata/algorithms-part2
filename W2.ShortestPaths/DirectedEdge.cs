namespace W2.ShortestPaths;

/// <summary>
/// Weighted directed edge
/// </summary>
public sealed class DirectedEdge
{
    private readonly int _v;
    private readonly int _w;
    private readonly double _weight;

    public DirectedEdge(int v, int w, double weight)
    {
        _v = v;
        _w = w;
        _weight = weight;
    }

    public int From()
    {
        return _v;
    }

    public int To()
    {
        return _w;
    }

    public double GetWeight()
    {
        return _weight;
    }

    public override string ToString()
    {
        return $"{_v}→{_w} - {_weight}";
    }
}