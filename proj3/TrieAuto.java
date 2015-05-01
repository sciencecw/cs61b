
import java.util.ArrayDeque;

/**
 * Prefix-TrieAuto. Supports linear time find() and insert(). 
 * Should support determining whether a word is a full word in the 
 * TrieAuto or a prefix.
 * @author Kak Wong
 */

public class TrieAuto {
    private TrieNodeAuto root;

    public TrieAuto() {
        root = new TrieNodeAuto(); // sentinel node
    }

    public boolean find(String s, boolean isFullWord) {
        checkstring(s);
        TrieNodeAuto tr = root.findWord(s);
        if (tr == null) {
            return false;
        } else if (!isFullWord) {
            return true;
        }
        return tr.isWord();
    }

    public void insert(String s, double weight) {
        checkstring(s);
        TrieNodeAuto pointer = root;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            boolean isLast = (i == (s.length() - 1));
            if (pointer.hasNode(c)) {
                pointer = pointer.getNode(c);
                // update weight
                pointer.updateMaxweight(weight);
                // turn node to be true if it is last node
                if (isLast) {
                    pointer.setWord(s, weight);
                }
            } else {
                pointer = pointer.setNode(c, isLast, s, weight);
            }
        } 
    }

    public TrieNodeAuto prefixNode(String s) {
        return root.findWord(s);
    }

    private void checkstring(String s) {
        if (s == null || s.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }


    public static void main(String[] args) {
/*        TrieAuto t = new TrieAuto();
        t.insert("hello");
        t.insert("hey");
        t.insert("goodbye");
        System.out.println(t.find("hell", false));
        System.out.println(t.find("hello", true));
        System.out.println(t.find("good", false));
        System.out.println(t.find("bye", false));
        System.out.println(t.find("heyy", false));
        System.out.println(t.find("hell", true));   
 */

    }
}
