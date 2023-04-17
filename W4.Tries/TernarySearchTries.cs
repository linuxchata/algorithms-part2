namespace W4.Tries;

/// <summary>
/// Ternary (composed of three parts) Search Tries
/// </summary>
public sealed class TernarySearchTries
{
    private TernaryNode _root;

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
        return x.Value;
    }

    private TernaryNode PutInternal(TernaryNode x, string key, object value, int d)
    {
        var c = key[d];
        if (x is null)
        {
            x = new TernaryNode
            {
                C = c,
            };
        }

        if (c < x.C)
        {
            x.Left = PutInternal(x.Left, key, value, d);
        }
        else if (c > x.C)
        {
            x.Right = PutInternal(x.Right, key, value, d);
        }
        else if (d < key.Length - 1)
        {
            x.Mid = PutInternal(x.Mid, key, value, d + 1);
        }
        else
        {
            x.Value = value;
        }

        return x;
    }

    private TernaryNode GetInternal(TernaryNode x, string key, int d)
    {
        if (x is null)
        {
            return null!;
        }

        var c = key[d];
        if (c < x.C)
        {
            return GetInternal(x.Left, key, d);
        }
        else if (c > x.C)
        {
            return GetInternal(x.Right, key, d);
        }
        else if (d < key.Length - 1)
        {
            return GetInternal(x.Mid, key, d + 1);
        }
        else
        {
            return x;
        }
    }
}