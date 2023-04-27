namespace W5.DataCompression;

public sealed class HuffmanEncodingTrie
{
    public Node BuildTrie(BinaryReader binaryReader)
    {
        var frequency = new int[HuffmanCompression.R];
        while (binaryReader.BaseStream.Position != binaryReader.BaseStream.Length)
        {
            var @char = binaryReader.ReadChar();
            frequency[@char]++;
        }

        binaryReader.BaseStream.Seek(0, SeekOrigin.Begin);

        return BuildTrieInternal(frequency);
    }

    private Node BuildTrieInternal(int[] frequency)
    {
        var priorityQueue = new PriorityQueue<Node, int>();
        for (var i = (char)0; i < HuffmanCompression.R; i++)
        {
            if (frequency[i] > 0)
            {
                var node = new Node((char)i, frequency[i], null!, null!);
                priorityQueue.Enqueue(node, frequency[i]);
            }
        }

        while (priorityQueue.Count > 1)
        {
            // Merge two smallest tries
            var x = priorityQueue.Dequeue();
            var y = priorityQueue.Dequeue();
            var totalFrequency = x.Frequency + y.Frequency;
            var parentNode = new Node('\0', totalFrequency, x, y);
            priorityQueue.Enqueue(parentNode, totalFrequency);
        }

        return priorityQueue.Dequeue();
    }
}