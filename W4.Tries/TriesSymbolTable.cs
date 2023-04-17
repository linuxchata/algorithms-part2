namespace W4.Tries;

/// <summary>
/// R-way Tries
/// </summary>
public sealed class TriesSymbolTable
{
    private const short R = 256; // Extended ASCII

    private Node _root = new(R);

    public List<string> GetKeys()
    {
        var queue = new Queue<string>();
        Collect(_root, string.Empty, queue);
        return queue.ToList();
    }

    public List<string> GetKeysWithPrefix(string prefix)
    {
        var queue = new Queue<string>();
        var x = GetInternal(_root, prefix, 0);
        Collect(x, prefix, queue);
        return queue.ToList();
    }

    public string GetLongestPrefixOf(string query)
    {
        var length = Search(_root, query, 0, 0);
        return query.Substring(0, length);
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

    public void Put(string key, object value)
    {
        _root = PutInternal(_root, key, value, 0);
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

    private void Collect(Node x, string prefix, Queue<string> queue)
    {
        if (x is null)
        {
            return;
        }

        if (x.Value is not null)
        {
            queue.Enqueue(prefix);
        }

        for (var c = 0; c < R; c++)
        {
            Collect(x.Next[c], prefix + (char)c, queue);
        }
    }

    private int Search(Node x, string query, int d, int length)
    {
        if (x is null)
        {
            return length;
        }

        if (x.Value is not null)
        {
            length = d;
        }

        if (d == query.Length)
        {
            return length;
        }

        var c = query[d];
        return Search(x.Next[c], query, d + 1, length);
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
}