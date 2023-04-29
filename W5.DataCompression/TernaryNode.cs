namespace W5.DataCompression;

public sealed class TernaryNode
{
    public object Value { get; set; } = null!;

    public char C { get; set; }

    public TernaryNode Left { get; set; } = null!;

    public TernaryNode Mid { get; set; } = null!;

    public TernaryNode Right { get; set; } = null!;

    public override string ToString()
    {
        return $"c - {C}, value - {Value}";
    }
}