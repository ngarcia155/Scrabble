import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class WordSolver {
    private BufferedReader reader;

    // Method to get the file and initialize the BufferedReader
    public void getFile(String fileName) {
        ClassLoader classLoader = WordSolver.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);

        if (inputStream != null) {
            reader = new BufferedReader(new InputStreamReader(inputStream));
        } else {
            System.err.println("File not found!");
        }
    }

    // Method to get the next word from the file
    public String getNextWord() {
        try {
            if (reader != null) {
                return reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null; // Return null if there's an error or no more words
    }


    // Method to generate a list of possible words given a set of characters
    public static List<String> generateWordsFromCharacters(String characters) {
        List<String> possibleWords = new ArrayList<>();
        generateWordsFromCharactersHelper("", characters, possibleWords);
        return possibleWords;
    }

    // Helper method to generate words from characters considering all permutations
    private static void generateWordsFromCharactersHelper(String prefix, String remainingChars, List<String> possibleWords) {
        if (Trie.search(prefix)) {
            possibleWords.add(prefix);
        }
        if (remainingChars.isEmpty()) {
            return;
        }

        for (int i = 0; i < remainingChars.length(); i++) {
            char ch = remainingChars.charAt(i);
            String newPrefix = prefix + ch;
            String newRemainingChars = remainingChars.substring(0, i) + remainingChars.substring(i + 1);
            generateWordsFromCharactersHelper(newPrefix, newRemainingChars, possibleWords);
        }
    }

    //From given characters, get the biggest word
    private String getBestWord(String chars) {
        List<String> possibleWords = generateWordsFromCharacters(chars);

        String bestWord = ""; // Initialize the best word as an empty string

        for (String word : possibleWords) {
            if (word.length() > bestWord.length()) {
                bestWord = word; // Update the best word if the current word is longer
            }
        }

        return bestWord; // Return the best word found
    }

    //Generate Trie Tree based on the dictionary file you give it
    public void genTrieTree(String filename){
        wordSolver.getFile(filename);
        // Example usage to print all words
        String word;
        while ((word = wordSolver.getNextWord()) != null) {
            //System.out.println(word);
            //Put Words into a Trie
            Trie.insert(word);
        }
    }

    public static WordSolver wordSolver = new WordSolver();

    public static void main(String[] args) {
        //Create TrieTree with the dictionary file
        wordSolver.genTrieTree("sowpods words.txt");

        System.out.println(wordSolver.getBestWord("toloeri"));
    }
}
