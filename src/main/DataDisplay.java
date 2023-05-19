package main;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * A class for modifying an HTML file to display data.
 * 
 * @param file The path to the HTML file to modify.
 * @param html The contents of the HTML file.
 * @param doc  The parsed HTML document.
 */

 
public class DataDisplay {

    private String file;
    private String html;
    private Document doc;

    /**
     * Constructs a new DataVisualizer object.
     * 
     * @param file The path to the HTML file to modify.
     * @throws IOException If there is an error reading the HTML file.
     */
    public DataDisplay(String file) throws IOException {
        this.file = file;
        this.html = readHtmlFile(file);
        this.doc = Jsoup.parse(html);
    }

    /**
     * Replaces the text inside the "text-view" element with the given text.
     * 
     * @param newText The new text to display.
     * @throws IOException If there is an error writing to the HTML file.
     */
    public void replaceTextView(String newText) throws IOException {
        Element textView = doc.getElementsByClass("text-view").first();

        // Replace the text inside the element
        if (textView != null) {
            textView.html(newText);
        }

        // Write the modified HTML back to the file
        writeHtmlFile(file, doc.html());
    }

    public void homeBuilder(List<Map<String, Integer>> allResults, List<String> visualiserFilePaths)
            throws IOException {
        Element phraseMatch = doc.getElementsByClass("phraseMatch").first();
        Element carousel = doc.getElementsByClass("carousel").first();

        // Add a new row to the table inside the "phraseMatch" element
        if (phraseMatch != null) {
            Element tbody = phraseMatch.appendElement("tbody");
            for (Map<String, Integer> resultMap : allResults) {
                for (Map.Entry<String, Integer> entry : resultMap.entrySet()) {
                    String fileEntry = entry.getKey();
                    Integer result = entry.getValue();
                    Element tr = tbody.appendElement("tr");
                    tr.appendElement("td").text(fileEntry);
                    tr.appendElement("td").text(result.toString());
                }
            }
        }

        // Append file paths to the carousel
        if (carousel != null) {
            for (String filePath : visualiserFilePaths) {
                String fileName = "";
                Pattern pattern = Pattern.compile("\\b\\w+\\.html\\b");
                Matcher matcher = pattern.matcher(filePath);
                if (matcher.find()) {
                    fileName = matcher.group();
                }
                Element li = carousel.appendElement("li");
                li.text(fileName);
                li.attr("onclick", "window.location.href='" + filePath + "'");
            }
        }

        // Write the modified HTML back to the file
        writeHtmlFile(file, doc.html());
    }

    /**
     * Adds a new row to the "phraseMatch" table with the given plagiarism results.
     * 
     * @param plagiarismResults A map of file names to plagiarism percentages.
     * @throws IOException If there is an error writing to the HTML file.
     */
    public void addPhraseMatch(Map<String, Integer> plagiarismResults) throws IOException {
        Element phraseMatch = doc.getElementsByClass("phraseMatch").first();

        // Add a new row to the table inside the "phraseMatch" element
        if (phraseMatch != null) {
            Element tbody = phraseMatch.appendElement("tbody");
            for (Map.Entry<String, Integer> entry : plagiarismResults.entrySet()) {
                String file = entry.getKey();
                Integer result = entry.getValue();
                Element tr = tbody.appendElement("tr");
                tr.appendElement("td").text(file);
                tr.appendElement("td").text(result.toString());
            }
        }

        // Write the modified HTML back to the file
        writeHtmlFile(file, doc.html());

    }

    /**
     * Adds a new row to the "wordCount" table with the given word count data.
     * 
     * @param wordCountMap A map of words to their counts.
     * @throws IOException If there is an error writing to the HTML file.
     */
    public void addWordCount(Map<String, Integer> wordCountMap) throws IOException {
        Element wordCount = doc.getElementsByClass("wordCount").first();

        // Add a new row to the table inside the "wordCount" element for each word/count
        // pair
        if (wordCount != null) {
            Element tbody = wordCount.appendElement("tbody");
            for (Map.Entry<String, Integer> entry : wordCountMap.entrySet()) {
                String word = entry.getKey();
                int count = entry.getValue();
                Element tr = tbody.appendElement("tr");
                tr.appendElement("td").text(word);
                tr.appendElement("td").text(Integer.toString(count));
            }
        }

        // Write the modified HTML back to the file
        writeHtmlFile(file, doc.html());
    }

    /**
     * Reads the contents of an HTML file.
     * 
     * @param dataPath The path to the HTML file.
     * @return The contents of the HTML file.
     * @throws IOException If there is an error reading the HTML file.
     */

    private String readHtmlFile(String dataPath) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(dataPath));
        return new String(encoded, StandardCharsets.UTF_8);
    }

    /**
     * Writes the modified HTML back to the file.
     * 
     * @param file    The path to the HTML file.
     * @param content The modified HTML content.
     * @throws IOException If there is an error writing to the HTML file.
     */
    private void writeHtmlFile(String file, String content) throws IOException {
        Files.write(Paths.get(file), content.getBytes(StandardCharsets.UTF_8));
    }
}
