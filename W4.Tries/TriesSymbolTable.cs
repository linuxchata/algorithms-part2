namespace W4.Tries;

public sealed class TriesSymbolTable
{
    private const short R = 256; // Extended ASCII

    private Node _root = new(R);

    public void Put(string key, object value)
    {
        _root = PutInternal(_root, key, value, 0);
    }

    public object Get(string key)
    {
        var x = GetInternal(_root, key, 0);
        if (x is null)
        {
            return null!;
        }

        return x.Value!;
    }

    private Node PutInternal(Node x, string key, object value, int d)
    {
        if (x is null)
        {
            x = new(R);
        }

        if (d == key.Length)
        {
            x.Value = value;
            return x;
        }

        var c = key[d];
        x.Next[c] = PutInternal(x.Next[c], key, value, d + 1);
        return x;
    }

    private Node GetInternal(Node x, string key, int d)
    {
        if (x is null)
        {
            return null!;
        }

        if (d == key.Length)
        {
            return x;
        }

        var c = key[d];
        return GetInternal(x.Next[c], key, d + 1);
    }
}