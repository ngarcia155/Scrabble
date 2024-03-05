import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * BoardReader class reads a Scrabble board from a file, identifies words on the board, and prints them along with their positions.
 */
public class BoardReader {
    private BufferedReader reader;
    public String tray = ""; // Public variable to hold the string "toloeri"
    private int boardSize; // Size of the board
    private Map<String, WordInfo> words; // Map to hold words found on the board along with their information
    public static ArrayList<Tiles> boardTiles; // 2D array to represent the board with Tiles objects

    //Words on Board as well as their positions
    private static Map<Map<String,WordInfo> , List<Integer>> wordMap = new HashMap<>();


    private ArrayList<int[]> wordMultiplierPositions; // List to hold word multiplier positions
    private ArrayList<int[]> letterDoublerPositions; // List to hold letter doubler positions

    /**
     * getFile method initializes the BufferedReader to read the specified file.
     * @param fileName The name of the file to be read.
     */
    public void getFile(String fileName) {
        ClassLoader classLoader = BoardReader.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);

        if (inputStream != null) {
            reader = new BufferedReader(new InputStreamReader(inputStream));
        } else {
            System.err.println("File not found!");
        }
    }

    /**
     * initializeBoard method reads the board size from the file and initializes the board and words map.
     */
    public void initializeBoard() {
        // Read board size
        try {
            //wordMultiplierPositions = new ArrayList<>();
            //letterDoublerPositions = new ArrayList<>();
            boardSize = Integer.parseInt(reader.readLine());
            //board = new char[boardSize][boardSize];
            words = new HashMap<>();
            boardTiles = new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * readBoard method reads the Scrabble board configuration from the file.
     * It parses the content to create the board and reads the tray configuration.
     */
    public void readBoard() {
        String line;
        try {
            // Parse the content to create the board
            for (int i = 0; i < boardSize; i++) {
                line = reader.readLine();

                // Split the line by space to get parts
                String[] lineParts = line.split("\\s+");

                for (int j = 0; j < lineParts.length; j++) {
                    String part = lineParts[j];

                    // Skip empty parts
                    if (part.isEmpty())
                        continue;

                    char currentChar = part.charAt(0); // Get the first character of the part

                    Tiles t = new Tiles();

                    if (Character.isDigit(currentChar)) {
                        if (part.length() > 1) {
                            char nextChar = part.charAt(1); // Get the next character
                            if (nextChar == '.') {
                                int multiplier = Character.getNumericValue(currentChar);
                                t.setWordMultiplier(multiplier); // Set word multiplier
                                t.setisWordMultiplier(true);
                                t.setPosition(i, j); // Set position
                                t.setTileString(part);
                                boardTiles.add(t);
                                continue; // Continue to the next iteration
                            } else if (Character.isDigit(nextChar)) {
                                int multiplier = Character.getNumericValue(nextChar);
                                t.setDoubleLetterMult(multiplier); // Set double letter multiplier
                                t.setPosition(i, j); // Set position
                                t.setTileString(part);
                                boardTiles.add(t);
                                continue; // Continue to the next iteration
                            }
                        }
                    }

                    // Check if it's a dot
                    if (currentChar == '.') {
                        if (part.length() > 1) {
                            char nextChar = part.charAt(1); // Get the next character
                            if(Character.isDigit(nextChar)){
                                int multiplier = Character.getNumericValue(nextChar);
                                t.setDoubleLetterMult(multiplier); // Set double letter multiplier
                                t.setPosition(i, j); // Set position
                                t.setTileString(part);
                                boardTiles.add(t);
                                continue; // Continue to the next iteration
                            }
                        }
                        t.setTileString(part);
                        t.setPosition(i,j);
                    } else {
                        t.setLetter(currentChar); // Set letter
                        t.setPosition(i, j); // Set position
                        t.setTileString(part);
                    }
                    boardTiles.add(t);
                }
            }

            // Read the tray configuration
            tray = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * findWords method identifies words on the board and their positions.
     */
    public void findWords() {
        // Iterate through each row
        for (int i = 0; i < boardSize; i++) {
            StringBuilder wordBuilder = new StringBuilder();
            for (int j = 0; j < boardSize; j++) {
                Tiles tempT = boardTiles.get(i * boardSize + j);
                if (tempT.hasLetter()) {
                    wordBuilder.append(tempT.getLetter());
                } else {
                    // If the current tile is blank, calculate the blank spaces on each side
                    if (wordBuilder.length() > 1) {
                        // Calculate the number of blank spaces to the left
                        int leftBlankSpaces = j - wordBuilder.length();
                        // Calculate the number of blank spaces to the right
                        int rightBlankSpaces = boardSize - j ;
                        // Record the word along with its information
                        WordInfo wi = new WordInfo(i, j - wordBuilder.length(), WordDirection.HORIZONTAL);
                        wi.rightBlankSpaces = rightBlankSpaces;
                        wi.leftBlankSpaces = leftBlankSpaces;
                        words.put(wordBuilder.toString(), wi);
                        List<Integer> coordinates = Arrays.asList(i, j - wordBuilder.length());
                        Map<String, WordInfo> innerMap = new HashMap<>();
                        innerMap.put(wordBuilder.toString(), wi);
                        wordMap.put(innerMap, coordinates);
                    }
                    wordBuilder = new StringBuilder(); // Reset the wordBuilder
                }
            }
            // If there's a word at the end of the row, record it
            if (wordBuilder.length() > 1) {
                // Calculate the number of blank spaces to the left
                int leftBlankSpaces = boardSize - wordBuilder.length();
                // Record the word along with its information
                WordInfo wi = new WordInfo(i, boardSize - wordBuilder.length(), WordDirection.HORIZONTAL);
                wi.leftBlankSpaces = leftBlankSpaces;
                words.put(wordBuilder.toString(), wi);
                List<Integer> coordinates = Arrays.asList(i, boardSize - wordBuilder.length());
                Map<String, WordInfo> innerMap = new HashMap<>();
                innerMap.put(wordBuilder.toString(), wi);
                wordMap.put(innerMap, coordinates);
            }
        }

        // Iterate through each column
        for (int j = 0; j < boardSize; j++) {
            StringBuilder wordBuilder = new StringBuilder();
            for (int i = 0; i < boardSize; i++) {
                Tiles tempT = boardTiles.get(i * boardSize + j);
                if (tempT.hasLetter()) {
                    wordBuilder.append(tempT.getLetter());
                } else {
                    // If the current tile is blank, calculate the blank spaces on each side
                    if (wordBuilder.length() > 1) {
                        // Calculate the number of blank spaces above
                        int upBlankSpaces = i - wordBuilder.length();
                        // Calculate the number of blank spaces below
                        int downBlankSpaces = boardSize - i - 1;
                        // Record the word along with its information
                        WordInfo wi = new WordInfo(i - wordBuilder.length(), j, WordDirection.VERTICAL);
                        wi.downBlankSpaces = downBlankSpaces;
                        words.put(wordBuilder.toString(), wi);
                        List<Integer> coordinates = Arrays.asList(i - wordBuilder.length(), j);
                        Map<String, WordInfo> innerMap = new HashMap<>();
                        innerMap.put(wordBuilder.toString(), wi);
                        wordMap.put(innerMap, coordinates);
                    }
                    wordBuilder = new StringBuilder(); // Reset the wordBuilder
                }
            }
            // If there's a word at the end of the column, record it
            if (wordBuilder.length() > 1) {
                // Calculate the number of blank spaces above
                int upBlankSpaces = boardSize - wordBuilder.length();
                // Record the word along with its information
                WordInfo wi = new WordInfo(boardSize - wordBuilder.length(), j, WordDirection.VERTICAL);
                wi.upBlankSpaces = upBlankSpaces;
                words.put(wordBuilder.toString(), wi);
                List<Integer> coordinates = Arrays.asList(boardSize - wordBuilder.length(), j);
                Map<String, WordInfo> innerMap = new HashMap<>();
                innerMap.put(wordBuilder.toString(), wi);
                wordMap.put(innerMap, coordinates);
            }
        }
        // Printing values using a for loop
//        for (Map.Entry<Map<String, WordInfo>, List<Integer>> entry : wordMap.entrySet()) {
//            Map<String, WordInfo> innerMap = entry.getKey();
//            List<Integer> coordinates = entry.getValue();
//
//            // Iterate over the inner map to get the word and WordInfo
//            for (Map.Entry<String, WordInfo> innerEntry : innerMap.entrySet()) {
//                String word = innerEntry.getKey();
//                WordInfo wordInfo = innerEntry.getValue();
//
//                System.out.println("Word: " + word + ", Coordinates: " + coordinates + ", WordInfo: " + wordInfo);
//            }
//        }

    }







    /**
     * printBoard method prints the Scrabble board.
     */
    public void printBoard() {
        // Print the Scrabble board
        int pos = 0;
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                //System.out.print(board[i][j] + " ");
                System.out.print(boardTiles.get(pos)+" ");
                pos++;
            }
            System.out.println();
        }
    }

    /**
     * printWords method prints all words found on the board along with their positions.
     */
    public void printWords() {
        // Print all words found on the board along with their information
        System.out.println("Words found on the board:");
        for (Map.Entry<String, WordInfo> entry : words.entrySet()) {
            System.out.println("Word: " + entry.getKey() + ", Direction: " + entry.getValue().direction +
                    ", Starting Position: (" + entry.getValue().row + ", " + entry.getValue().column + ")");
        }
    }

    public List<String> getWordsOnBoard() {
        List<String> wordList = new ArrayList<>(); // Rename the list variable to avoid confusion

        // Iterate over the words map and add each word to the list
        for (Map.Entry<String, WordInfo> entry : words.entrySet()) {
            //System.out.println();
            wordList.add(entry.getKey());
        }

        return wordList; // Return the list of words
    }


    /**
     * WordInfo class holds information about a word found on the Scrabble board.
     */
    class WordInfo {
        int row; // Starting row position of the word
        int column; // Starting column position of the word
        WordDirection direction; // Direction of the word (horizontal or vertical)
        int leftBlankSpaces;
        int rightBlankSpaces;
        int downBlankSpaces;
        int upBlankSpaces;

        /**
         * Constructor to initialize WordInfo object with row, column, direction, and blank spaces information.
         *
         * @param row              Starting row position of the word.
         * @param column           Starting column position of the word.
         * @param direction        Direction of the word (horizontal or vertical).
         */
        public WordInfo(int row, int column, WordDirection direction) {
            this.row = row;
            this.column = column;
            this.direction = direction;
        }
    }

    public Map<String, WordInfo> getWordInfos(BoardReader boardReader) {
        Map<String, WordInfo> wordInfos = new HashMap<>();

        for (Map.Entry<String, WordInfo> entry : boardReader.words.entrySet()) {
            String word = entry.getKey();
            WordInfo wordInfo = entry.getValue();
            wordInfos.put(word, wordInfo);
            System.out.println("\nWord: " + word);
            System.out.printf( wordInfo.row+"-"+wordInfo.column);
//            System.out.println("Direction: " + wordInfo.direction);
            //System.out.println("Left Blank Spaces: " + wordInfo.leftBlankSpaces);
            //System.out.println("Right Blank Spaces: " + wordInfo.rightBlankSpaces);
//            System.out.println("Up Blank Spaces: " + wordInfo.upBlankSpaces);
//            System.out.println("Down Blank Spaces: " + wordInfo.downBlankSpaces);
//            System.out.println();
        }
        return wordInfos;
    }

    public void playHorizontalWord(BoardReader br){

    }



    /**
     * Enum representing the direction of a word.
    */
    enum WordDirection {
    HORIZONTAL, // Horizontal direction
    VERTICAL // Vertical direction
    }

    /**
     * main method initializes the BoardReader object, reads the board, finds words, and prints the results.
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
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
        //System.out.println(WordSolver.getWords("toloerire", "resid"));

        System.out.println();

        //boardReader.getWordInfos(boardReader);
        System.out.println("Best word to play");
        //System.out.println(WordSolver.playBigWord(WordSolver.wordSolver.getFittedWord("mat", "troolie", 1, 3)));

        // Printing values using a for loop
        for (Map.Entry<Map<String, WordInfo>, List<Integer>> entry : wordMap.entrySet()) {
            Map<String, WordInfo> innerMap = entry.getKey();
            List<Integer> coordinates = entry.getValue();

            // Iterate over the inner map to get the word and WordInfo
            for (Map.Entry<String, WordInfo> innerEntry : innerMap.entrySet()) {
                String word = innerEntry.getKey();
                WordInfo wordInfo = innerEntry.getValue();

//                System.out.println("Word: " + word + ", Coordinates: " + coordinates + ", WordInfo: " + wordInfo);
                System.out.println("Word: " + word + ", Coordinates: " + coordinates );
                System.out.println("Best Word To Play Here: " );
//                System.out.println("spacesP "+wordInfo.leftBlankSpaces+ " "+ wordInfo.rightBlankSpaces);
//                System.out.println("direction "+wordInfo.direction);
                if(wordInfo.direction == WordDirection.HORIZONTAL){
                    //System.out.println(word);
                    System.out.println(WordSolver.playBigWord(WordSolver.wordSolver.getFittedWord(word.toLowerCase(), "troolie", wordInfo.leftBlankSpaces, wordInfo.rightBlankSpaces)));
                    //System.out.println("spacesP "+wordInfo.leftBlankSpaces+ " "+ wordInfo.rightBlankSpaces);
                    //System.out.println("direction "+wordInfo.direction);
                }else{

                }
                //System.out.println();
            }
        }

    }

}
