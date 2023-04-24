namespace W5.RegularExpressions;

/// <summary>
/// Nondeterministic Finite Automaton
/// </summary>
public sealed class NondeterministicFiniteAutomaton
{
    private readonly char[] _re;
    private readonly Digraph _g;
    private readonly int _m;

    public NondeterministicFiniteAutomaton(string regexp)
    {
        _m = regexp.Length;
        _re = regexp.ToCharArray();
        _g = BuildEpsilonTransitionDigraph();
    }

    public bool Recognize(string text)
    {
        // State reachabe from start by ε-transitions
        var programCounter = new List<int>();
        var dfs = new DirectedDfs(_g, 0);
        for (var v = 0; v < _g.V(); v++)
        {
            if (dfs.IsMarked(v))
            {
                programCounter.Add(v);
            }
        }

        for (var i = 0; i < text.Length; i++)
        {
            // Set of states reachable after scanning past txt[i]
            var states = new List<int>();
            foreach (var v in programCounter)
            {
                if (v == _m) // Not necessarily a match (regular expression needs to match full text)
                {
                    continue;
                }

                if (_re[v] == text[i] || _re[v] == '.')
                {
                    states.Add(v + 1);
                }
            }

            // Follow ε-transitions
            dfs = new DirectedDfs(_g, states);
            programCounter = new List<int>();
            for (var v = 0; v < _g.V(); v++)
            {
                if (dfs.IsMarked(v))
                {
                    programCounter.Add(v);
                }
            }
        }

        foreach (var v in programCounter)
        {
            if (v == _m) // Accept if can end in state M
            {
                return true;
            }
        }

        return false; // Reject
    }

    private Digraph BuildEpsilonTransitionDigraph()
    {
        var g = new Digraph(_m + 1);
        var operations = new Stack<int>();

        for (var i = 0; i < _m; i++)
        {
            var lp = i;

            if (_re[i] == '(' || _re[i] == '|')
            {
                operations.Push(i);
            }
            else if (_re[i] == ')')
            {
                var or = operations.Pop();
                if (_re[or] == '|')
                {
                    lp = operations.Pop();
                    g.AddEdge(lp, or + 1);
                    g.AddEdge(or, i);
                }
                else
                {
                    lp = or;
                }
            }

            if (i < _m - 1 && _re[i + 1] == '*')
            {
                g.AddEdge(lp, i + 1);
                g.AddEdge(i + 1, lp);
            }

            if (_re[i] == '(' || _re[i] == '*' || _re[i] == ')')
            {
                g.AddEdge(i, i + 1);
            }
        }

        return g;
    }
}