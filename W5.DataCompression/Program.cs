namespace W5.DataCompression;

public class Program
{
    public static void Main(string[] args)
    {
        // Run-length encoding
        var runLength = new RunLength();
        runLength.Compress();
        runLength.Expand();

        // Huffman Compression
        var huffmanCompression = new HuffmanCompression();
        huffmanCompression.Compress();
        huffmanCompression.Expand();

        // Lempel–Ziv–Welch Compression (LZW Compression)
        var lempelZivWelchCompression = new LempelZivWelchCompression();
        lempelZivWelchCompression.Compress();

        Console.ReadKey();
    }
}