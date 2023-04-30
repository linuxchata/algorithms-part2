using System.Text;

namespace W5.DataCompression;

/// <summary>
/// Lempel–Ziv–Welch Compression (LZW Compression)
/// </summary>
public sealed class LempelZivWelchCompression
{
    private const int R = 256; // ASCII alphabet
    private const int L = 4096; // number of codewords = 2^12

    public void Compress()
    {
        using var readFileStream = GetReadFileStream("input_lzw.txt");
        using var binaryReader = new BinaryReader(readFileStream);

        using var writeFileStream = GetWriteFileStream("compressed_lzw.txt");
        using var binaryWriter = new BinaryWriter(writeFileStream);
        var output = new StringBuilder();

        var contentLength = (int)binaryReader.BaseStream.Length;
        var content = binaryReader.ReadChars(contentLength);
        var input = new string(content);

        // Codewords for singlechar, radix R keys
        var trie = new TernarySearchTries();
        for (var i = 0; i < R; i++)
        {
            var key = string.Empty + (char)i;
            trie.Put(key, i);
        }

        var code = R + 1; // R is codeword for EOF

        while (input.Length > 0)
        {
            // Find longest prefix match s
            var s = trie.LongestPrefixOf(input);
            var value = trie.Get(s);

            // Write W-bit codeword for s
            binaryWriter.Write((int)value);
            output.Append((int)value + " ");
            var t = s.Length;
            if (t < input.Length && code < L)
            {
                // Add new codeword
                var key = input.Substring(0, t + 1);
                trie.Put(key, code++);
            }
            input = input.Substring(t);
        }

        // Write "stop" codeword
        binaryWriter.Write(R);
        output.Append(R);
        var result = output.ToString();
    }

    public void Expand()
    {
        using var readFileStream = GetReadFileStream("compressed_lzw.txt");
        using var binaryReader = new BinaryReader(readFileStream);

        using var writeFileStream = GetWriteFileStream("decompressed_lzw.txt");
        using var binaryWriter = new BinaryWriter(writeFileStream);
        var output = new StringBuilder();

        var st = new string[L];

        // Initialize table for chars
        int i;
        for (i = 0; i < R; i++)
        {
            var key = string.Empty + (char)i;
            st[i] = key;
        }
        st[i++] = " ";

        var codeword = binaryReader.ReadInt32();
        var value = st[codeword];

        while (true)
        {
            // Write current substring
            binaryWriter.Write(value.ToCharArray());
            output.Append(value);

            // Get next codeword
            codeword = binaryReader.ReadInt32();
            if (codeword == R)
            {
                break;
            }

            var s = st[codeword];

            // If lookahead is invalid, make codeword from last one
            if (i == codeword)
            {
                s = value + value[0];
            }

            // Add new entry to code table
            if (i < L)
            {
                st[i++] = value + s[0];
            }

            // Update current codeword
            value = s;
        }
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