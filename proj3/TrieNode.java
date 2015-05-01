import java.util.HashMap;

public class TrieNode {
    public char c;
    public String word;
    public boolean isWord;
    public HashMap<Character, TrieNode> map;
    public int idNode;

    public static int numNode = 0;

    public TrieNode(char character) {
        c = character;
        map = new HashMap<Character, TrieNode>();
        numNode++;
    }

    public void setWord(String w) {
        isWord = true;
        word = w;
    }

    public String getWord() {
        return word;
    }

    public char getChar() {
        return c;
    }

    public TrieNode getNode(char c) {
        return map.get(c);
    }

    public boolean hasNode(char c) {
        return map.containsKey(c);
    }

    public TrieNode setNode(char c, boolean isW, String w) {
        TrieNode temp = new TrieNode(c);
        if (isW) {
            temp.setWord(w);
        }
        map.put(c, temp);
        return temp;
    }

    public boolean isWord() {
        return isWord;
    }

    public TrieNode findWord(String s) {
        if (s == null || s.isEmpty()) {
            return null;
        }
        char c = s.charAt(0);
        TrieNode next = this.getNode(c);
        if (next == null || s.length() == 1) {
            return next;
        }
        return next.findWord(s.substring(1));
    }





}