import java.util.HashMap;

public class TrieNodeAuto {
    private String word;
    private boolean isWord;
    private HashMap<Character, TrieNodeAuto> map;
    private double weight;
    private double maxweight;

    public TrieNodeAuto() {
        map = new HashMap<Character, TrieNodeAuto>();
        weight = Double.NEGATIVE_INFINITY;
        isWord = false;
    }

    public void setWord(String s, double wg) {
        isWord = true;
        word = s;
        weight = wg;
    }

    public String getWord() {
        return word;
    }

    public HashMap<Character, TrieNodeAuto> getMap() {
        return map;
    }

    public void setmaxWeight(double w) {
        maxweight = w;
    }

    public double getWeight() {
        return weight;
    }

    public double getmaxWeight() {
        return maxweight;
    }

    public TrieNodeAuto getNode(char c) {
        return map.get(c);
    }

    public boolean hasNode(char c) {
        return map.containsKey(c);
    }

    public TrieNodeAuto setNode(char c, boolean isW, String s, double wg) {
        TrieNodeAuto temp = new TrieNodeAuto();
        if (isW) {
            temp.setWord(s, wg);
        }
        temp.setmaxWeight(wg);
        map.put(c, temp);
        return temp;
    }

    public void updateMaxweight(double wg) {
        if (wg > maxweight) {
            maxweight = wg;
        }
    }

    public boolean hasSubnode() {
        return !map.isEmpty();
    }

    public TrieNodeAuto getmaxNode() {
        double maxweight = Double.NEGATIVE_INFINITY;
        TrieNodeAuto maxnode = null;
        for (TrieNodeAuto tNode :map.values()) {
            double w = tNode.getmaxWeight();
            if (maxnode == null || w > maxweight ) {
                maxweight = w;
                maxnode = tNode;
            }
        }
        return maxnode;
    }

    public boolean isWord() {
        return isWord;
    }

    public TrieNodeAuto findWord(String s) {
        if (s == null || s.isEmpty()) {
            return null;
        }
        char c = s.charAt(0);
        TrieNodeAuto next = this.getNode(c);
        if (next == null || s.length() == 1) {
            return next;
        }
        return next.findWord(s.substring(1));
    }





}