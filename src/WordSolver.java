import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

public class WordSolver {
    // Method to generate a list of possible words given a set of characters

    private static String tray;
    private static String pattern;

    public static void Setter(String tray,String pattern) {
        WordSolver.tray = tray.toLowerCase();
        WordSolver.pattern = pattern.toLowerCase();
    }

    /**
     * Generates a list of possible words from the given characters using a trie-based approach.
     * This method starts the word generation process by invoking the helper method.
     *
     * @param characters The input characters from which to generate words.
     * @return A list of possible words formed from the input characters.
     */
    private static List<String> generateWordsFromCharacters(String characters) {
        List<String> possibleWords = new ArrayList<>();
        generateWordsFromCharactersHelper("", characters, possibleWords, Trie.root);
        return possibleWords;
    }

    /**
     * Recursively generates words from a trie node based on the given prefix and remaining characters.
     * Words are added to the list of possible words if they are valid and contain the specified pattern.
     * This method serves as a helper for generating words from characters.
     *
     * @param prefix        The current prefix being constructed.
     * @param remainingChars The remaining characters to form words from.
     * @param possibleWords The list to store possible words.
     * @param node          The current node in the trie representing the prefix.
     */
    private static void generateWordsFromCharactersHelper(String prefix, String remainingChars, List<String> possibleWords, Trie.TrieNode node) {
        // If the current prefix represents a valid word and contains the given pattern, add it to the list of possible words
        if (node.isEndOfWord && containsAdjacentSequence(prefix, pattern)) {
            possibleWords.add(prefix);
        }

        // Base case: if there are no remaining characters, return
        if (remainingChars.isEmpty()) {
            return;
        }

        // Iterate over each character in remainingChars
        for (int i = 0; i < remainingChars.length(); i++) {
            char ch = remainingChars.charAt(i);
            int charIndex = ch - 'a';

            // If the current character exists in the trie, recursively call the helper method
            if (node.children[charIndex] != null) {
                String newPrefix = prefix + ch;
                String newRemainingChars = remainingChars.substring(0, i) + remainingChars.substring(i + 1);
                generateWordsFromCharactersHelper(newPrefix, newRemainingChars, possibleWords, node.children[charIndex]);
            }
        }
    }

    // Method to generate Trie Tree based on the dictionary file
    // We want to make sure not to generate multiple trees and duplicate words
    // We only want to generate once.
    boolean generatedTree = false;

    public void genTrieTree(String filename) {
        if (!generatedTree) {
            generatedTree = true;
            getFile(filename);
            String word;
            while ((word = getNextWord()) != null) {
                Trie.insert(word);
            }
        }
    }

    // Method to get the file and initialize the BufferedReader
    private void getFile(String fileName) {
        ClassLoader classLoader = WordSolver.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);
        if (inputStream != null) {
            reader = new BufferedReader(new InputStreamReader(inputStream));
        } else {
            System.err.println("File not found!");
        }
    }

    // Method to get the next word from the file
    private String getNextWord() {
        try {
            if (reader != null) {
                return reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private BufferedReader reader;

    public static WordSolver wordSolver = new WordSolver();

    // Method to check if a word contains a specific adjacent sequence
    public static boolean containsAdjacentSequence(String word, String sequence) {
        int sequenceLength = sequence.length();
        int wordLength = word.length();

        // Iterate through each character in the word
        for (int i = 0; i <= wordLength - sequenceLength; i++) {
            // Check if the current character matches the first character of the sequence
            if (word.charAt(i) == sequence.charAt(0)) {
                // Check if the rest of the characters in the word match the rest of the sequence
                boolean sequenceFound = true;
                for (int j = 1; j < sequenceLength; j++) {
                    if (word.charAt(i + j) != sequence.charAt(j)) {
                        // If any character doesn't match, break and move to the next character in the word
                        sequenceFound = false;
                        break;
                    }
                }
                if (sequenceFound) {
                    // If the sequence is found, return true
                    return true;
                }
            }
        }
        // If the sequence is not found, return false
        return false;
    }

    // Method to get words from the trie based on tray and pattern
    public static List<String> getWords(String tray, String pattern) {
        wordSolver.genTrieTree("sowpods words.txt");
        WordSolver.Setter(tray,pattern);
        return generateWordsFromCharacters(tray + pattern);
    }



    public Set<String> getFittedWord(String pattern, String tray, int leftBlanks, int rightBlanks) {
        // Get possible words based on the tray and pattern
        List<String> possibleWords = getWords(tray, pattern);

        // Set to store unique words that fit within the left and right blank spaces
        Set<String> fittedWords = new HashSet<>();

        // Iterate through each possible word
        for (String possibleWord : possibleWords) {
            // Skip words that are too long
            if (possibleWord.length() > leftBlanks + rightBlanks + pattern.length()) {
                continue;
            }

            // Check if the pattern fits within the word considering left blanks
            int leftIndex = possibleWord.indexOf(pattern.charAt(0));
            if (leftIndex >= 0 && leftIndex <= leftBlanks) {
                // Calculate the remaining length of the word after considering left blanks
                int remainingLength = possibleWord.length() - leftIndex;

                // Check if the remaining length of the word can accommodate the right blanks and the pattern
                if (remainingLength >= pattern.length() && remainingLength - pattern.length() <= rightBlanks) {
                    fittedWords.add(possibleWord);
                }
            }
        }
        fittedWords.remove(pattern);

        // Print the fitted words
        //System.out.println("Fitted words: " + fittedWords);

        // Return true if any fitted words were found, otherwise return false
        return fittedWords;
    }


    public static String playBigWord(Set<String> words) {
        int maxLen = -1;
        String biggestWord = null;

        for (String word : words) {
            if (word.length() > maxLen) {
                maxLen = word.length();
                biggestWord = word;
            }
        }

        return biggestWord;
    }






    // Driver method
    public static void main(String[] args) {

        //Map<String, WordInfo> words
        //Map<String, BoardReader.WordInfo> words = new HashMap<>();


        // Create TrieTree with the dictionary file
//        wordSolver.genTrieTree("sowpods words.txt");

        // Set tray and pattern
        //Setter("At","C");

        // Get and print words
        //System.out.println(getWords(tray,pattern));


        BoardReader boardReader = new BoardReader();
        boardReader.getFile("boardTest.txt");
        boardReader.initializeBoard();
        boardReader.readBoard();
        boardReader.findWords();

        // Print the board
        System.out.println("Scrabble Board:");
        boardReader.printBoard();
        System.out.println("Tray: " + boardReader.tray);

        // Print the words found on the board
        //boardReader.printWords();
        System.out.println(boardReader.getWordsOnBoard());
        //WordSolver.wordSolver.genTrieTree("sowpods words.txt");
        //System.out.println(Trie.contains("peopl"));

        //System.out.println(WordSolver.getWords("troolie", "lo"));

        //HorizontalSolver hS = new HorizontalSolver(boardTiles);
        System.out.println();

        //boardReader.getWordInfos(boardReader);
        //System.out.println("possible words");
        //wordSolver.getFittedWord("mat", "troolie", 1, 3);
        //System.out.println("Words found: " + wordsFound);
        System.out.println("Best word to play");
        System.out.println(wordSolver.playBigWord(wordSolver.getFittedWord("mat", "troolie", 1, 3)));

    }
}
