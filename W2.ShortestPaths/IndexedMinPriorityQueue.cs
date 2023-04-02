namespace W2.ShortestPaths;

/// <summary>
/// Indexed minimum priority queue
/// https://algs4.cs.princeton.edu/24pq/IndexMinPQ.java.html
/// </summary>
/// <typeparam name="T"></typeparam>
public sealed class IndexedMinPriorityQueue<T>
    where T : IComparable<T>
{
    private int _count = 0;
    private int[] _pq; // Binary heap using 1-based indexing
    private int[] _qp; // Inverse of pq - qp[pq[i]] = pq[qp[i]] = i
    private T[] _keys;

    public IndexedMinPriorityQueue(int capacity)
    {
        _keys = new T[capacity + 1];
        _pq = new int[capacity + 1];
        _qp = new int[capacity + 1];
        for (var i = 0; i <= capacity; i++)
        {
            _qp[i] = -1;
        }
    }

    public int Count()
    {
        return _count;
    }

    public bool Contains(int i)
    {
        return _qp[i] != -1;
    }

    public void Insert(int i, T key)
    {
        _count++;
        _qp[i] = _count;
        _pq[_count] = i;
        _keys[i] = key;
        Swim(_count);
    }

    public void DecreaseKey(int i, T key)
    {
        _keys[i] = key;
        Swim(_qp[i]);
    }

    public int DeleteMin()
    {
        var min = _pq[1];
        Exchange(1, _count--);
        Sink(1);
        _qp[min] = -1;
        _pq[_count + 1] = -1;
        return min;
    }

    private void Swim(int k)
    {
        while (k > 1 && Greater(k / 2, k))
        {
            Exchange(k, k / 2);
            k = k / 2;
        }
    }

    private void Sink(int k)
    {
        while (2 * k <= _count)
        {
            int j = 2 * k;
            if (j < _count && Greater(j, j + 1)) j++;
            if (!Greater(k, j)) break;
            Exchange(k, j);
            k = j;
        }
    }

    private bool Greater(int i, int j)
    {
        return _keys[_pq[i]].CompareTo(_keys[_pq[j]]) > 0;
    }

    private void Exchange(int i, int j)
    {
        var swap = _pq[i];
        _pq[i] = _pq[j];
        _pq[j] = swap;
        _qp[_pq[i]] = i;
        _qp[_pq[j]] = j;
    }
}