import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * BoardReader class reads a Scrabble board from a file, identifies words on the board, and prints them along with their positions.
 */
public class BoardReader {
    private BufferedReader reader;
    public String tray = ""; // Public variable to hold the string "toloeri"
    private char[][] board; // 2D array to represent the board
    private int boardSize; // Size of the board
    private Map<String, WordInfo> words; // Map to hold words found on the board along with their information

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
            boardSize = Integer.parseInt(reader.readLine());
            board = new char[boardSize][boardSize];
            words = new HashMap<>();
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
                String[] parts = line.split("\\s+");
                for (int j = 0; j < boardSize; j++) {
                    board[i][j] = parts[j].charAt(0);
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
                // If the cell contains a letter, append it to the wordBuilder
                if (Character.isLetter(board[i][j])) {
                    wordBuilder.append(board[i][j]);
                }
                // If the cell is not a letter and the wordBuilder has more than one character,
                // record the word found in the horizontal direction
                else if (wordBuilder.length() > 1) {
                    words.put(wordBuilder.toString(), new WordInfo(i, j - wordBuilder.length() + 1, WordDirection.HORIZONTAL));
                    wordBuilder = new StringBuilder(); // Reset the wordBuilder
                } else {
                    wordBuilder = new StringBuilder(); // Reset the wordBuilder
                }
            }
            // If there's a word at the end of the row, record it
            if (wordBuilder.length() > 1) {
                words.put(wordBuilder.toString(), new WordInfo(i, boardSize - wordBuilder.length(), WordDirection.HORIZONTAL));
            }
        }

        // Iterate through each column
        for (int j = 0; j < boardSize; j++) {
            StringBuilder wordBuilder = new StringBuilder();
            for (int i = 0; i < boardSize; i++) {
                // If the cell contains a letter, append it to the wordBuilder
                if (Character.isLetter(board[i][j])) {
                    wordBuilder.append(board[i][j]);
                }
                // If the cell is not a letter and the wordBuilder has more than one character,
                // record the word found in the vertical direction
                else if (wordBuilder.length() > 1) {
                    words.put(wordBuilder.toString(), new WordInfo(i - wordBuilder.length() + 1, j, WordDirection.VERTICAL));
                    wordBuilder = new StringBuilder(); // Reset the wordBuilder
                } else {
                    wordBuilder = new StringBuilder(); // Reset the wordBuilder
                }
            }
            // If there's a word at the end of the column, record it
            if (wordBuilder.length() > 1) {
                words.put(wordBuilder.toString(), new WordInfo(boardSize - wordBuilder.length(), j, WordDirection.VERTICAL));
            }
        }



        // Iterate through each column
        for (int j = 0; j < boardSize; j++) {
            StringBuilder wordBuilder = new StringBuilder();
            for (int i = 0; i < boardSize; i++) {
                if (Character.isLetter(board[i][j])) {
                    wordBuilder.append(board[i][j]);
                } else if (wordBuilder.length() > 1) {
                    words.put(wordBuilder.toString(), new WordInfo(i - wordBuilder.length() + 1, j, WordDirection.VERTICAL));
                    wordBuilder = new StringBuilder();
                } else {
                    wordBuilder = new StringBuilder();
                }
            }
            if (wordBuilder.length() > 1) {
                words.put(wordBuilder.toString(), new WordInfo(boardSize - wordBuilder.length(), j, WordDirection.VERTICAL));
            }
        }
    }

    /**
     * printBoard method prints the Scrabble board.
     */
    public void printBoard() {
        // Print the Scrabble board
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                System.out.print(board[i][j] + " ");
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
        boardReader.printWords();
    }

}

/**
 * WordInfo class holds information about a word found on the Scrabble board.
 */
class WordInfo {
    int row; // Starting row position of the word
    int column; // Starting column position of the word
    WordDirection direction; // Direction of the word (horizontal or vertical)

    /**
     * Constructor to initialize WordInfo object with row, column, and direction.
     * @param row Starting row position of the word.
     * @param column Starting column position of the word.
     * @param direction Direction of the word (horizontal or vertical).
     */
    public WordInfo(int row, int column, WordDirection direction) {
        this.row = row;
        this.column = column;
        this.direction = direction;
    }
}

/**
 * Enum representing the direction of a word.
 */
enum WordDirection {
    HORIZONTAL, // Horizontal direction
    VERTICAL // Vertical direction
}

