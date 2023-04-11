namespace W3.MaximumFlowAndMinimumCut;

/// <summary>
/// Flow edge data structure
/// </summary>
public sealed class FlowEdge
{
    private readonly int _v;
    private readonly int _w;
    private readonly double _capacity;

    private double _flow;

    public FlowEdge(int v, int w, double capacity)
    {
        _v = v;
        _w = w;
        _capacity = capacity;
        _flow = 0.0;
    }

    public int From()
    {
        return _v;
    }

    public int To()
    {
        return _w;
    }

    public int Other(int v)
    {
        if (_v == v)
        {
            return _w;
        }
        else if (_w == v)
        {
            return _v;
        }

        throw new ArgumentException();
    }

    public double GetCapacity()
    {
        return _capacity;
    }

    public double GetFlow()
    {
        return _flow;
    }

    public double GetResidualCapacityTo(int vertex)
    {
        if (vertex == _v)
        {
            return _flow; // Backward edge, flow
        }
        else if (vertex == _w)
        {
            return _capacity - _flow; // Forward edge, capacity minus flow
        }

        throw new ArgumentException();
    }

    public void AddResidualCapacityTo(int vertex, double delta)
    {
        if (vertex == _v)
        {
            _flow -= delta;
        }
        else if (vertex == _w)
        {
            _flow += delta;
        }
        else
        {
            throw new ArgumentException();
        }
    }

    public override string ToString()
    {
        return $"({_v}) —— [{string.Format("{0:0.0}", _flow)} / {string.Format("{0:0.0}", _capacity)}] ——> ({_w})";
    }
}