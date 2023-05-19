package main;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Application {
    private final File templatesDirectory;
    private final File dataDirectory;
    private final File displayDirectory;

    public Application(File templatesDirectory, File dataDirectory, File displayDirectory) {
        this.templatesDirectory = templatesDirectory;
        this.dataDirectory = dataDirectory;
        this.displayDirectory = displayDirectory;
    }

    public File getTemplatesDirectory() {
        return templatesDirectory;
    }

    public File getDataDirectory() {
        return dataDirectory;
    }

    public File getDisplayDirectory() {
        return displayDirectory;
    }

    private void run() {
        try {
            // Check if the data and visualiser directories are valid
            if (!(getDataDirectory().isDirectory() && getDisplayDirectory().isDirectory())) {
                System.out.println("Error: Invalid data or visualiser directory");
                return;
            }

            // Create a new plagiarism detector with a window size of 3
            PlagiarismDetector detector = new PlagiarismDetector(getDataDirectory(), 3);

            // Create lists to store the results and file paths for the visualisations
            List<Map<String, Integer>> allResults = new ArrayList<>();
            List<String> visualiserFilePaths = new ArrayList<>();

            // Loop over each file in the data directory
            for (File file : getDataDirectory().listFiles()) {
                if (file.isFile()) {
                    // Create a new file with the desired name in the visualiser directory
                    String visualiserFilePath = getDisplayDirectory() + "\\"
                            + file.getName().replaceFirst("[.][^.]+$", "")
                            + ".html";
                    File visualiserFile = new File(visualiserFilePath);

                    // Create the new file if it doesn't exist
                    if (!visualiserFile.exists()) {
                        visualiserFile.createNewFile();
                    }

                    // Copy the contents of the results.html file to the new file
                    Files.copy(getTemplatesDirectory().toPath().resolve("results.html"), visualiserFile.toPath(),
                            StandardCopyOption.REPLACE_EXISTING);

                    // Generate the visualisation HTML file
                    DataDisplay visualiser = new DataDisplay(visualiserFilePath);
                    Map<String, Integer> wordCountMap = Tokeniser.countWords(file);
                    visualiser.addWordCount(wordCountMap);

                    // Detect plagiarism for the current file
                    Map<String, Integer> plagiarismResults = detector.detectPlagiarism(file);
                    allResults.add(plagiarismResults);
                    visualiser.addPhraseMatch(plagiarismResults);
                    visualiser.replaceTextView(detector.textmarker(file));
                    visualiserFilePaths.add(visualiserFile.getAbsolutePath().toString().replace("\\", "\\\\"));
                }
            }

            // Build the home page
            File homePageFile = getDisplayDirectory().toPath().resolve("Home.html").toFile().getAbsoluteFile();

            // Create the new file if it doesn't exist
            if (!homePageFile.exists()) {
                homePageFile.createNewFile();
            }

            Files.copy(getTemplatesDirectory().toPath().resolve("home.html"), homePageFile.toPath(),
                    StandardCopyOption.REPLACE_EXISTING);

            DataDisplay visualiser = new DataDisplay(homePageFile.toString());
            visualiser.homeBuilder(allResults, visualiserFilePaths);

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }

    }

    public static void main(String[] args) {
        Application app = new Application(new File("src\\main\\resources\\templates"), new File("data"),
                new File("display"));
        app.run();
    }
}
