import java.util.HashMap;

public class TrieNodeAuto {
    private char character;
    private String word;
    private HashMap<Character, TrieNodeAuto> map;
    private double weight;
    private double maxweight;

    public TrieNodeAuto(char c) {
        //map = new HashMap<Character, TrieNodeAuto>();
        weight = -1;
        maxweight = -1;
        character = c;
    }

    public TrieNodeAuto(char c, String s, double wg) {
        //map = new HashMap<Character, TrieNodeAuto>();
        character = c;
        setWord(s, wg);
        maxweight = wg;
    }

    public void setWord(String s, double wg) {
        word = s;
        weight = wg;
    }

    @Override
    public String toString() {
        String s = character + " " + maxweight + " " + weight;
        if (word != null) {
            s = s + " " + word + "\n";
        } else {
            s = s + " \n";
        }
        return s;
    }

    public String getWord() {
        return word;
    }

    public char getChar() {
        return character;
    }

    public double getWeight() {
        return weight;
    }

    public double getmaxWeight() {
        return maxweight;
    }

    public HashMap<Character, TrieNodeAuto> getMap() {
        return map;
    }


    public TrieNodeAuto getNode(char c) {
        return map.get(c);
    }

    public boolean hasNode(char c) {
        return (map != null && map.containsKey(c));
    }

    public boolean hasnoMap() {
        return (map == null);
    }

    // set up next node
    public TrieNodeAuto setNode(char c, boolean isW, String s, double wg) {
        TrieNodeAuto temp;
        if (isW) {
            temp = new TrieNodeAuto(c, s, wg);
        } else {
            temp = new TrieNodeAuto(c);
            temp.maxweight = wg;
        }
        if (map == null) {
            map = new HashMap<Character, TrieNodeAuto>();
        }
        map.put(c, temp);
        return temp;
    }

    public void updateMaxweight(double wg) {
        if (wg > maxweight) {
            maxweight = wg;
        }
    }

    public boolean hasSubnode() {
        return (map != null && !map.isEmpty());
    }

    public TrieNodeAuto getmaxNode() {
        double maxweight = -1.1;
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
        return (weight >= 0);
    }





}