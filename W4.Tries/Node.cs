namespace W4.Tries;

public sealed class Node
{
    public Node(short r)
    {
        Next = new Node[r];
    }

    public Node[] Next { get; set; }

    public object? Value { get; set; }

    public override string ToString()
    {
        return Value is null ? string.Empty : Value.ToString();
    }
}