namespace W5.DataCompression;

public sealed class Node : IComparable<Node>
{
    public Node(char @char, int frequency, Node left, Node right)
    {
        Char = @char;
        Frequency = frequency;
        Left = left;
        Right = right;
    }

    public char Char { get; init; }
    public int Frequency { get; init; }
    public Node Left { get; init; }
    public Node Right { get; init; }

    public bool IsLeaf()
    {
        return Left == null && Right == null;
    }

    public int CompareTo(Node that)
    {
        return Frequency - that.Frequency;
    }

    public override string ToString()
    {
        return $"Char [{Char}]. Frequency {Frequency}";
    }
}