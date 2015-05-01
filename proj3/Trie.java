
import java.util.ArrayDeque;

/**
 * Prefix-Trie. Supports linear time find() and insert(). 
 * Should support determining whether a word is a full word in the 
 * Trie or a prefix.
 * @author Kak Wong
 */

public class Trie {
    public TrieNode root;

    public Trie() {
        root = new TrieNode((char) 0); // sentinel node
    }

    public boolean find(String s, boolean isFullWord) {
        checkstring(s);
        TrieNode tr = root.findWord(s);
        if (tr == null) {
            return false;
        } else if (!isFullWord) {
            return true;
        }
        return tr.isWord();
    }

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

    private void checkstring(String s) {
        if (s == null || s.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

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
                for (int i = charset.length -  1 ; i >= 0 ; i--) {
                    char c = charset[i];
                    if (n.hasNode(c)) {
                        stack.push(n.getNode(c));
                    }
                }
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        Trie t = new Trie();
        t.insert("hello");
        t.insert("hey");
        t.insert("goodbye");
        System.out.println(t.find("hell", false));
        System.out.println(t.find("hello", true));
        System.out.println(t.find("good", false));
        System.out.println(t.find("bye", false));
        System.out.println(t.find("heyy", false));
        System.out.println(t.find("hell", true));   
    }
}
