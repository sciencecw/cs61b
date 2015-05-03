import java.util.ArrayDeque;

/**
 * Prefix-Trie. Supports linear time find() and insert(). 
 * Should support determining whether a word is a full word in the 
 * Trie or a prefix.
 * @author Kak Wong
 */

public class Trie {
    private TrieNode root;

    /**
     * Constructor: Initializes Trie
     * constant time
     */
    public Trie() {
        root = new TrieNode((char) 0); // sentinel node
    }

    /**
     * find if string is added to trie
     * @param s input string
     * @param isFullWord if only prefix or fullword is searched for
     * @return true if such word is found
     */
    public boolean find(String s, boolean isFullWord) {
        checkstring(s);
        TrieNode tr = prefixNode(s);
        if (tr == null) {
            return false;
        } else if (!isFullWord) {
            return true;
        }
        return tr.isWord();
    }

    /**
     * insert string to trie
     * runtime scales with length of string
     * @param s input string
     */
    public void insert(String s) {
        checkstring(s);
        TrieNode pointer = root;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            boolean isLast = (i == (s.length() - 1));
            if (pointer.hasNode(c)) {
                pointer = pointer.getNode(c);
                // turn node to be true if it is last node
                if (isLast) {
                    pointer.setWord(s);
                }
            } else {
                pointer = pointer.setNode(c, isLast, s);
            }
        } 
    }

    /**
     * helper method. check if string is suitable for input
     * @param s input string
     */
    private void checkstring(String s) {
        if (s == null || s.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * helper method. graph traversal with charset as alphabet ordering
     * runtime scales with size of trie
     * @param charset input character ordering
     * @return string representation of dfs graph traversal
     */
    public String dfs(char[] charset) {
        ArrayDeque<TrieNode> stack = new ArrayDeque<TrieNode>();
        StringBuilder sb = new StringBuilder();
        stack.push(root);
        while (!stack.isEmpty()) {
            TrieNode n = stack.pop();
            if (n.isWord()) {
                sb.append(n.getWord()).append("\n");
            }
            if (n.map.size() != 0) {
                for (int i = charset.length -  1; i >= 0; i--) {
                    char c = charset[i];
                    if (n.hasNode(c)) {
                        stack.push(n.getNode(c));
                    }
                }
            }
        }
        return sb.toString();
    }

    /**
     * return node associated with certain prefix
     * runtime scales with legnth of string s
     * @param s prefix string
     * @return Node associated with prefix
     */
    public TrieNode prefixNode(String s) {
        if (s == null || s.isEmpty()) {
            return root;
        }
        int index = 0;
        TrieNode pointer = root;
        while (index < s.length() && pointer != null) {
            char c = s.charAt(index);
            pointer = pointer.getNode(c);
            index++;
        }
        return pointer;
    }

    /**
     * main method
     * @param args redundant
     */
    public static void main(String[] args) { 
    }
}
