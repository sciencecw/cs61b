/**
 * Prefix-TrieAuto. Supports linear time find() and insert(). 
 * Should support determining whether a word is a full word in the 
 * TrieAuto or a prefix.
 * @author Kak Wong
 */

public class TrieAuto {
    private TrieNodeAuto root;

    /**
     * Initializes Trie
     */
    public TrieAuto() {
        root = new TrieNodeAuto('a'); // sentinel node
    }

    /**
     * find if string is added to trie
     * @param s input string
     * @param isFullWord if only prefix or fullword is searched for
     * @return true if such word is found
     */
    public boolean find(String s, boolean isFullWord) {
        checkstring(s);
        TrieNodeAuto tr = prefixNode(s);
        if (tr == null) {
            return false;
        } else if (!isFullWord) {
            return true;
        }
        return tr.isWord();
    }

    /**
     * insert string to trie
     * @param s input string
     * @param weight for weight associated with this word
     */
    public void insert(String s, double weight) {
        checkstring(s);
        TrieNodeAuto pointer = root;
        boolean hassubNode = true;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            boolean isLast = (i == (s.length() - 1));
            if (hassubNode && pointer.hasNode(c)) { // if the node already exist
                pointer = pointer.getNode(c);
                // update weight
                pointer.updateMaxweight(weight);
                // turn node to be true if it is last node
                if (isLast) {
                    if (pointer.isWord()) {
                        throw new IllegalArgumentException();
                    }
                    pointer.setWord(s, weight);
                }
            } else { // no such node yet -> create one
                pointer = pointer.setNode(c, isLast, s, weight);
                hassubNode = false;
            }
        } 
    }

    /**
     * helper method. return prefix trienode
     * @param s input string
     * @return representing the prefix
     */
    public TrieNodeAuto prefixNode(String s) {
        if (s == null || s.isEmpty()) {
            return root;
        }
        int index = 0;
        TrieNodeAuto pointer = root;
        while (index < s.length() && pointer != null) {
            if (pointer.hasnoMap()) {
                return null;
            }
            char c = s.charAt(index);
            pointer = pointer.getNode(c);
            index++;
        }
        return pointer;
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

    public TrieNodeAuto getRootnode() {
        return root;
    }

}
