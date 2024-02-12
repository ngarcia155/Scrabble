public class Trie {

    // Alphabet size (# of symbols)
    static final int ALPHABET_SIZE = 26;

    // Trie node
    static class TrieNode {
        TrieNode[] children = new TrieNode[ALPHABET_SIZE];
        boolean isEndOfWord; // Indicates if the node represents the end of a word

        // Constructor
        TrieNode() {
            isEndOfWord = false;
            for (int i = 0; i < ALPHABET_SIZE; i++)
                children[i] = null;
        }
    }

    static TrieNode root = new TrieNode();;

    // Inserts a key into the trie
    static void insert(String key) {
        int keyLength; // Length of the key
        int charIndex; // Index of the current character in the alphabet
        TrieNode currentNode = root; // Pointer to traverse through the trie

        keyLength = key.length(); // Calculate the length of the key

        // Traverse through each character of the key
        for (int level = 0; level < keyLength; level++) {
            charIndex = key.charAt(level) - 'a'; // Convert the current character to an index

            // If the node for the current character doesn't exist, create a new node
            if (currentNode.children[charIndex] == null)
                currentNode.children[charIndex] = new TrieNode();

            currentNode = currentNode.children[charIndex]; // Move to the next node
        }

        // Mark the last node as the end of the word
        currentNode.isEndOfWord = true;
    }


    // Searches for a key in the trie
    static boolean search(String key) {
        int keyLength; // Length of the key
        int charIndex; // Index of the current character in the alphabet
        TrieNode currentNode = root; // Pointer to traverse through the trie

        keyLength = key.length(); // Calculate the length of the key

        // Traverse through each character of the key
        for (int level = 0; level < keyLength; level++) {
            charIndex = key.charAt(level) - 'a'; // Convert the current character to an index

            // If the node for the current character doesn't exist, the key doesn't exist
            if (currentNode.children[charIndex] == null)
                return false;

            currentNode = currentNode.children[charIndex]; // Move to the next node
        }

        // Return true if the last node is marked as the end of a word
        return currentNode.isEndOfWord;
    }

    // Prints all complete words in the trie
    static void printAllWords() {
        // Start the recursive call from the root node
        printAllWords(root, "");
    }

    // Recursive function to print all complete words
    static void printAllWords(TrieNode node, String prefix) {
        // Base case: If the current node is the end of a word, print the prefix
        if (node.isEndOfWord) {
            System.out.println(prefix);
        }

        // Traverse through all children of the current node
        for (int i = 0; i < ALPHABET_SIZE; i++) {
            // If a child node exists, recursively call printAllWords with the child node and updated prefix
            if (node.children[i] != null) {
                // Convert the character index to its corresponding character
                char ch = (char) (i + 'a');
                // Append the character to the current prefix
                String newPrefix = prefix + ch;
                // Recursive call with the child node and updated prefix
                printAllWords(node.children[i], newPrefix);
            }
        }
    }



    // Driver
    public static void main(String args[]) {
        // Input keys (use only 'a' through 'z' and lower case)
        String keys[] = {"the", "a", "there", "answer", "any",
                "by", "bye", "their"};

        String output[] = {"Not present in trie", "Present in trie"};

        //root = new TrieNode(); // Create root node

        // Construct trie by inserting keys
        for (String key : keys) {
            insert(key);
        }

        //printAllWords();
    }
}
