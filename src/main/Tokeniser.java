package main;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tokeniser {
    // Define a regular expression pattern for matching words
    private static final Pattern WORD_PATTERN = Pattern.compile("[a-zA-Z]+(-[a-zA-Z]+)*");

    /**
     * tokenises all words in a single file 
     *
     * @return a word count map
     * @throws IOException if an I/O error occurs while reading the files
     */
    
    public static Map<String, Integer> countWords(File filePath) throws IOException {
        // Create a new HashMap to store the word count data for the file
        Map<String, Integer> wordCountMap = new HashMap<>();

        // Read all lines of the file and loop through each line
        for (String line : Files.readAllLines(filePath.toPath())) {
            // Use a regular expression to match words in the line
            Matcher matcher = WORD_PATTERN.matcher(line);
            while (matcher.find()) {
                // Get the matched word and add it to the word count map for the file
                String word = matcher.group().toLowerCase();
                wordCountMap.put(word, wordCountMap.getOrDefault(word, 0) + 1);
            }
        }
        return wordCountMap;
    }
}
