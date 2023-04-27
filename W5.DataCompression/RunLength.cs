using System.Text;

namespace W5.DataCompression;

/// <summary>
/// Run-length encoding
/// </summary>
public sealed class RunLength
{
    private const int R = 256; // Maximum run-length count
    private const int LgR = 4; // Number of bits per count

    public void Compress()
    {
        using var readFileStream = GetReadFileStream("input.txt");
        using var binaryReader = new BinaryReader(readFileStream);

        using var writeFileStream = GetWriteFileStream("compressed.txt");
        using var binaryWriter = new BinaryWriter(writeFileStream);

        var count = 0;
        var old = false;
        while (binaryReader.BaseStream.Position != binaryReader.BaseStream.Length)
        {
            var b = binaryReader.ReadBoolean();
            if (b != old)
            {
                WriteString(binaryWriter, count);
                count = 0;
                old = !old;
            }
            else if (count == R)
            {
                WriteString(binaryWriter, count);
                count = 0;
            }
            count++;
        }
        WriteString(binaryWriter, count);
    }

    public void Expand()
    {
        var bit = false;

        using var readFileStream = GetReadFileStream("compressed.txt");
        using var binaryReader = new BinaryReader(readFileStream);

        using var writeFileStream = GetWriteFileStream("decompressed.txt");
        using var binaryWriter = new BinaryWriter(writeFileStream);

        var result = new StringBuilder();
        while (binaryReader.BaseStream.Position != binaryReader.BaseStream.Length)
        {
            var count = ReadInt(binaryReader, LgR); // Read 4-bit count from standard input
            for (var i = 0; i < count; i++)
            {
                binaryWriter.Write(bit);
            }
            bit = !bit;
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

    private int ReadInt(BinaryReader binaryReader, int count)
    {
        var run = binaryReader.ReadBytes(count);
        var runString = new string(run.Select(a => a == 1 ? '1' : '0').ToArray());
        return Convert.ToInt32(runString, 2);
    }

    private void WriteString(BinaryWriter binaryWriter, int value)
    {
        var binary = Convert.ToString(value, 2).PadLeft(4, '0'); // PadLeft to add leading zeros (111 => 0111)
        for (var i = 0; i < binary.Length; i++)
        {
            binaryWriter.Write(binary[i] == '1' ? true : false);
        }
    }
}