package main;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PlagiarismDetector {
    // @param phraseLength - the length of phrases to find

    private final File directoryPath;
    private final int phraseLength;

    public PlagiarismDetector(File directoryPath, int phraseLength) {
        this.directoryPath = directoryPath;
        this.phraseLength = phraseLength;
    }

    /**
     * Method to detect plagiarism.
     *
     * @param fileToCompare the file to compare against all other files in the
     *                      directory
     * @return a map of strings, where each string corresponds to a file name and
     *         its plagiarism percentage
     */

    public String textmarker(File fileToCompare) {
        // Read files in the directory
        final File[] files = directoryPath.listFiles();
        Set<String> result = new HashSet<>();
        Set<String> plagiarisedPhrases = new HashSet<>();
        String comparedFileContent = "";
        String fileToCompareContent = "";

        // Compare file in question to all other files
        for (File file : files) {
            String fileName = file.getName();

            // Skip the file to be compared
            if (fileName.equals(fileToCompare.getName())) {
                continue;
            }
            

            try {
                // Read all lines in the file
                List<String> comparedFileLines = Files.readAllLines(file.toPath());

                // Read all lines in the file to be compared
                List<String> fileToCompareLines = Files.readAllLines(fileToCompare.toPath());

                // Combine all lines into a single string
                comparedFileContent = String.join(" ", comparedFileLines);
                fileToCompareContent = String.join(" ", fileToCompareLines);

                // Remove all substrings in quotes
                comparedFileContent = removeQuotedSubstrings(comparedFileContent);
                fileToCompareContent = removeQuotedSubstrings(fileToCompareContent);

                // Split the content of each file into words and add them to the corresponding
                // lists
                List<String> comparedFileWordsList = splitContentIntoWords(comparedFileContent);
                List<String> fileToCompareWordsList = splitContentIntoWords(fileToCompareContent);

                // Determine the plagiarism index
                if (fileToCompareWordsList.size() < comparedFileWordsList.size()) {
                    plagiarisedPhrases = plagiarisedPhrases(fileToCompareWordsList, comparedFileContent);
                } else {
                    plagiarisedPhrases = plagiarisedPhrases(comparedFileWordsList, fileToCompareContent);
                }

                // Add the results to the set
                result.addAll(plagiarisedPhrases);

            } catch (IOException e) {
                System.err.println("Error reading file: " + e.getMessage());
            }
        }

        for (String phrase : plagiarisedPhrases) {
            // Remove punctuation and extra white space from phrase and comparedFileContent
            String phraseStr = phrase.replaceAll("[^a-zA-Z0-9 ]", "");
            comparedFileContent = comparedFileContent.replaceAll("[^a-zA-Z0-9 ]", "");

            fileToCompareContent = fileToCompareContent.replaceAll(phraseStr, "<mark>" + phrase + "</mark>");
        }

        return fileToCompareContent;
    }

    /**
     * Method to detect plagiarism.
     *
     * @param fileToCompare the file to compare against all other files in the
     *                      directory
     * @return a map of strings, where each string corresponds to a file name and
     *         its plagiarism percentage
     */

    public Map<String, Integer> detectPlagiarism(File fileToCompare) {
        Map<String, Integer> results = new HashMap<>();
        int commonPhrases;
        // Read files in the directory
        File[] files = directoryPath.listFiles();

        // Compare file in question to all other files
        for (File file : files) {
            String fileName = file.getName();

            // Skip the file to be compared
            if (fileName.equals(fileToCompare.getName())) {
                continue;
            }

            try {

                // Read all lines in the file
                List<String> comparedFileLines = Files.readAllLines(file.toPath());

                // Read all lines in the file to be compared
                List<String> fileToCompareLines = Files.readAllLines(fileToCompare.toPath());

                // Combine all lines into a single string for each file
                String comparedFileContent = String.join(" ", comparedFileLines);
                String fileToCompareContent = String.join(" ", fileToCompareLines);

                // Remove all substrings encased in quotes
                List<String> comparedFileWordsList = new ArrayList<>();
                List<String> fileToCompareWordsList = new ArrayList<>();

                comparedFileContent = removeQuotedSubstrings(comparedFileContent);
                fileToCompareContent = removeQuotedSubstrings(fileToCompareContent);

                // Split the content of each file into words and add them to the corresponding
                // lists
                comparedFileWordsList = splitContentIntoWords(comparedFileContent);
                fileToCompareWordsList = splitContentIntoWords(fileToCompareContent);

                if (fileToCompareWordsList.size() < comparedFileWordsList.size()) {
                    commonPhrases = phraseCount(fileToCompareWordsList, comparedFileContent);
                } else {
                    commonPhrases = phraseCount(comparedFileWordsList, fileToCompareContent);
                }

                int similarityInt = calculateSimilarity(commonPhrases,
                        Math.min(comparedFileWordsList.size(), fileToCompareWordsList.size()));
                results.put(fileName + " - " + fileToCompare.getName(), similarityInt);

            } catch (IOException e) {
                System.err.println("Error reading file: " + e.getMessage());
            }
        }

        return results;
    }

    // A helper function to remove all substrings encased in quotes
    private String removeQuotedSubstrings(String content) {
        return content.replaceAll("\"[^\"]*\"", "");
    }

    // A helper function to split the content of a file into words
    private List<String> splitContentIntoWords(String content) {
        List<String> wordsList = new ArrayList<>();
        String[] words = content.split("[^a-zA-Z]+(-[a-zA-Z]+)*");
        for (String word : words) {
            wordsList.add(word);
        }
        return wordsList;
    }

    // returns a list of plagerised phrases
    private Set<String> plagiarisedPhrases(List<String> fileToCompareWordsList, String comparedFileContent) {
        Set<String> plagiarizedPhrases = new HashSet<>();
        // Length of phrases to compare
        int phraseLen = phraseLength;
        int start = 0;

        while (start + phraseLen <= fileToCompareWordsList.size()) {
            List<String> phrase = fileToCompareWordsList.subList(start, start + phraseLen);

            // Remove punctuation and extra white space from phrase and comparedFileContent
            String phraseStr = String.join(" ", phrase);
            phraseStr = phraseStr.replaceAll("[^a-zA-Z0-9 ]", "");
            comparedFileContent = comparedFileContent.replaceAll("[^a-zA-Z0-9 ]", "");

            if (comparedFileContent.contains(phraseStr)) {
                plagiarizedPhrases.add(phraseStr);
            }

            start += phraseLen;
        }
        return plagiarizedPhrases;
    }

    private int phraseCount(List<String> fileToCompareWordsList, String comparedFileContent) {
        int commonPhrases = 0;
        // Length of phrases to compare
        int phraseLen = phraseLength;
        int start = 0;

        while (start + phraseLen <= fileToCompareWordsList.size()) {
            List<String> phrase = fileToCompareWordsList.subList(start, start + phraseLen);

            // Remove punctuation and extra white space from phrase and comparedFileContent
            String phraseStr = String.join(" ", phrase);
            phraseStr = phraseStr.replaceAll("[^a-zA-Z0-9 ]", "");
            comparedFileContent = comparedFileContent.replaceAll("[^a-zA-Z0-9 ]", "");

            if (comparedFileContent.contains(phraseStr)) {
                commonPhrases++;
            }

            start += phraseLen;
        }
        return commonPhrases;
    }

    // A helper function to calculate the plagiarism percentage based on the number
    // of exact phrases
    private int calculateSimilarity(int phrasesCount, int wordCount) {
        double similarity = ((double) (phrasesCount * phraseLength) / wordCount) * 100;
        if (similarity > 100) {
            similarity = 100;
        }
        return (int) Math.round(similarity);
    }

}
