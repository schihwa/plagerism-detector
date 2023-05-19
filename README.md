# Basic Plagiarism Checker

This Java application is designed to detect plagiarism in written text

### Features
* Ignores sentences in quotes
* Uses the brute force method to check for plagiarism, despite variations in punctuation
* Tokenizes words and accounts for compound and punctuated words
* Produces websites to display the information

### Limitations
* This application assumes proper English usage and may not be effective for detecting plagiarism in other languages
* Large documents will take a long time to process due to the brute forcing technique
* The application can not be detect advanced types of plagiarism, such as paraphrasing or lack of references

## Requirements

To run the application, you will need:

* Java
* Jsoup library (set the class path and environment variable)

## Usage

1. Place at least two data files in the "data" folder within the project directory for the application to find and process.
2. Compile the application by running the command `javac -cp lib/jsoup-1.16.1.jar -d bin src/main/*.java` in the root directory.
3. Run the compiled class file located in the "bin" folder

## Installation

You can download the repository as a ZIP file or clone it using the following command:

```
git clone https://github.com/schihwa/plagiarismChecker.git
```

After that, you can follow the instructions in the "Usage" section to compile and run the application.