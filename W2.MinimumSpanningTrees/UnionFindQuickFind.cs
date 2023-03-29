namespace W2.MinimumSpanningTrees;

public class UnionFindQuickFind
{
    private readonly int[] _array;

    private readonly int _size;

    public UnionFindQuickFind(int size)
    {
        _size = size;
        _array = new int[_size];

        for (var i = 0; i < _size; i++)
        {
            _array[i] = i;
        }
    }

    public void Union(int p, int q)
    {
        var pid = _array[p];
        var qid = _array[q];
        for (var i = 0; i < _size; i++)
        {
            if (_array[i] == pid)
            {
                _array[i] = qid;
            }
        }
    }

    public bool IsConnected(int p, int q)
    {
        return _array[p] == _array[q];
    }
}