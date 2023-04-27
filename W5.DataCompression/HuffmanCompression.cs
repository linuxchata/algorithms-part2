namespace W5.DataCompression;

/// <summary>
/// Huffman Compression
/// </summary>
public sealed class HuffmanCompression
{
    public const int R = 256; // ASCII alphabet

    private readonly HuffmanEncodingTrie _huffmanEncodingTrie;

    public HuffmanCompression()
    {
        _huffmanEncodingTrie = new HuffmanEncodingTrie();
    }

    public void Compress()
    {
        using var readFileStream = GetReadFileStream("input_h.txt");
        using var binaryReader = new BinaryReader(readFileStream);

        using var writeFileStream = GetWriteFileStream("compressed_h.txt");
        using var binaryWriter = new BinaryWriter(writeFileStream);

        // Build trie based on a input file
        var root = _huffmanEncodingTrie.BuildTrie(binaryReader);
        WriteEncodingTrie(root);

        // Write content length to a compressed file
        var contentLength = (int)binaryReader.BaseStream.Length;
        binaryWriter.Write(contentLength);

        // Build code table
        var codeTable = new string[R];
        BuildCode(codeTable, root, string.Empty);

        // Read content and write to a compressed file
        var content = binaryReader.ReadChars(contentLength);
        for (var i = 0; i < contentLength; i++)
        {
            var code = codeTable[content[i]];
            for (var j = 0; j < code.Length; j++)
            {
                binaryWriter.Write(code[j] == '1' ? true : false);
            }
        }
    }

    public void Expand()
    {
        using var readFileStream = GetReadFileStream("compressed_h.txt");
        using var binaryReader = new BinaryReader(readFileStream);

        using var writeFileStream = GetWriteFileStream("decompressed_h.txt");
        using var binaryWriter = new BinaryWriter(writeFileStream);

        // Build trie based on a trie file
        var root = ReadEncodingTrie();

        // Read content length from a compressed file
        var n = binaryReader.ReadInt32();

        // Read content and write to a decompressed file
        for (var i = 0; i < n; i++)
        {
            var x = root;
            while (!x.IsLeaf())
            {
                if (!binaryReader.ReadBoolean())
                {
                    x = x.Left;
                }
                else
                {
                    x = x.Right;
                }
            }
            binaryWriter.Write(x.Char);
        }
    }

    private void BuildCode(string[] codeTable, Node x, string s)
    {
        if (x.IsLeaf())
        {
            codeTable[x.Char] = s;
            return;
        }

        BuildCode(codeTable, x.Left, s + '0');
        BuildCode(codeTable, x.Right, s + '1');
    }

    private Node ReadEncodingTrie()
    {
        using var readFileStream = GetReadFileStream("trie_h.txt");
        using var binaryReader = new BinaryReader(readFileStream);
        return ReadEncodingTrieInternal(binaryReader);
    }

    private Node ReadEncodingTrieInternal(BinaryReader binaryReader)
    {
        if (binaryReader.ReadBoolean())
        {
            var c = binaryReader.ReadChar();
            return new Node(c, 0, null!, null!);
        }

        var x = ReadEncodingTrieInternal(binaryReader);
        var y = ReadEncodingTrieInternal(binaryReader);
        return new Node('\0', 0, x, y);
    }

    private void WriteEncodingTrie(Node x)
    {
        using var writeFileStream = GetWriteFileStream("trie_h.txt");
        using var binaryWriter = new BinaryWriter(writeFileStream);
        WriteEncodingTrieIndernal(x, binaryWriter);
    }

    private void WriteEncodingTrieIndernal(Node x, BinaryWriter binaryWriter)
    {
        if (x.IsLeaf())
        {
            binaryWriter.Write(true);
            binaryWriter.Write(x.Char);
            return;
        }

        binaryWriter.Write(false);
        WriteEncodingTrieIndernal(x.Left, binaryWriter);
        WriteEncodingTrieIndernal(x.Right, binaryWriter);
    }

    private FileStream GetReadFileStream(string fileName)
    {
        return new FileStream(fileName, FileMode.Open, FileAccess.Read);
    }

    private FileStream GetWriteFileStream(string fileName)
    {
        File.Delete(fileName);
        return new FileStream(fileName, FileMode.CreateNew, FileAccess.Write);
    }
}