using System.Text.RegularExpressions;

namespace W1.DirectedGraphs;

public class WebCrawler
{
    private readonly Queue<string> _queue;
    private readonly HashSet<string> _discovered;

    public WebCrawler()
    {
        _queue = new Queue<string>();
        _discovered = new HashSet<string>();
    }

    public async Task Crawl()
    {
        var pattern = "https://(\\w+\\.)*(\\w+)";
        var regex = new Regex(pattern);

        var root = "https://princeton.edu";
        _queue.Enqueue(root);
        _discovered.Add(root);

        var iterationCount = 0;

        while (_queue.Any() && iterationCount < 100)
        {
            iterationCount++;

            var url = _queue.Dequeue();

            var rawHtml = await GetRawHtml(url);

            var matchedUrls = regex.Matches(rawHtml);
            for (int i = 0; i < matchedUrls.Count; i++)
            {
                var foundUrl = matchedUrls[i].Value;
                if (!_discovered.Contains(foundUrl))
                {
                    _discovered.Add(foundUrl);
                    _queue.Enqueue(foundUrl);
                }
            }
        }
    }

    private async Task<string> GetRawHtml(string url)
    {
        try
        {
            var rawHtml = string.Empty;
            using var client = new HttpClient();
            return await client.GetStringAsync(url);
        }
        catch (Exception)
        {
            return string.Empty;
        }
    }
}