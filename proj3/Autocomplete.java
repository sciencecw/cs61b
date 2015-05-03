import java.util.PriorityQueue;
import java.util.LinkedList;

/**
 * Implements autocomplete on prefixes for a given dictionary of terms and weights.
 * @author Kak Wong
 */
public class Autocomplete {

    private TrieAuto trie;
    /**
     * Initializes required data structures from parallel arrays.
     * This should run in linear time with respect to array length
     * and linear with resepct to word length
     * @param terms Array of terms.
     * @param weights Array of weights.
     */
    public Autocomplete(String[] terms, double[] weights) {
        trie = new TrieAuto();
        if (terms.length != weights.length) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < terms.length; i++) {
            if (weights[i] < 0) {
                throw new IllegalArgumentException();
            }
            trie.insert(terms[i], weights[i]);
        }
    }

    /**
     * alternate constructor of class with trie provided
     * @param tr Trie used for construction
     */  
    public Autocomplete(TrieAuto tr) {
        trie = tr;
    }

    public TrieAuto getTrie() {
        return trie;
    }

    /**
     * Find the weight of a given term. If it is not in the dictionary, return 0.0
     * @param term string in interest 
     * @return double weight
     */
    public double weightOf(String term) {
        TrieNodeAuto tNode = trie.prefixNode(term);
        if (tNode.isWord()) {
            return tNode.getWeight();
        }
        return 0.0;
    }

    /**
     * Return the top match for given prefix, or null if there is no matching term.
     * @param prefix Input prefix to match against.
     * @return Best (highest weight) matching string in the dictionary.
     */
    public String topMatch(String prefix) {
        checkString(prefix);
        TrieNodeAuto tNode = trie.prefixNode(prefix);
        while (tNode.hasSubnode()) {
            TrieNodeAuto ttemp = tNode.getmaxNode();
            if (!tNode.isWord() || ttemp.getmaxWeight() >= tNode.getmaxWeight()) {
                tNode = ttemp;
            } else {
                break;
            }
        }
        return tNode.getWord();
    }

    /**
     * helper method. check if string is suitable for input
     * @param s input string
     */
    private void checkString(String s) {
        if (s == null) {
            throw new NullPointerException();
        }
    }

    /**
     * Returns the top k matching terms (in descending order of weight) as an iterable.
     * If there are less than k matches, return all the matching terms.
     * @param prefix with which the matches must start with
     * @param k number of matches provided
     * @return an interable for the function
     */
    public Iterable<String> topMatches(String prefix, int k) {
        checkString(prefix);
        if (k < 0) {
            throw new IllegalArgumentException();
        }
        TrieNodeAuto tNode = trie.prefixNode(prefix);
        if (tNode == null) {
            return new LinkedList<String>();
        }      
        PriorityQueue<TrieNodeAuto> pq 
            = new PriorityQueue<TrieNodeAuto>(k, new AutoComparator(true));
        WordList wordsets = new WordList(k);
        pq.add(tNode);
        while (!pq.isEmpty()) {
            tNode = pq.poll();
            if (tNode.isWord()) {
                wordsets.add(tNode);
                if (wordsets.isFull() && wordsets.isLighter(tNode)) {
                    /*System.out.println("stuff in pq");
                    for (int i =0; i<20; i++) {
                        System.out.print("" + pq.poll());
                    }
                    System.out.println("tNode " + tNode);
                    System.out.println(wordsets);
                    */
                    break;
                }
            }
            for (TrieNodeAuto n: tNode.getMap().values()) {
                pq.add(n);
            }
        }
        return wordsets.getIterable();
    }

    /**
     * Returns the highest weighted matches within k edit distance of the word.
     * If the word is in the dictionary, then return an empty list.
     * @param word The word to spell-check
     * @param dist Maximum edit distance to search
     * @param k    Number of results to return 
     * @return Iterable in descending weight order of the matches
     */
    public Iterable<String> spellCheck(String word, int dist, int k) {
        LinkedList<String> results = new LinkedList<String>();  
        /* YOUR CODE HERE; LEAVE BLANK IF NOT PURSUING BONUS */
        return results;
    }
    /**
     * Test client. Reads the data from the file, 
     * then repeatedly reads autocomplete queries from standard 
     * input and prints out the top k matching terms.
     * @param args takes the name of an input file and an integer k as command-line arguments
     */
    public static void main(String[] args) {
        // initialize autocomplete data structure
        In in = new In(args[0]);
        int N = in.readInt();
        String[] terms = new String[N];
        double[] weights = new double[N];
        for (int i = 0; i < N; i++) {
            weights[i] = in.readDouble();   // read the next weight
            in.readChar();                  // scan past the tab
            terms[i] = in.readLine();       // read the next term
        }

        Autocomplete autocomplete = new Autocomplete(terms, weights);

        // process queries from standard input
        int k = Integer.parseInt(args[1]);
        while (StdIn.hasNextLine()) {
            String prefix = StdIn.readLine();
            for (String term : autocomplete.topMatches(prefix, k)) {
                StdOut.printf("%14.1f  %s\n", autocomplete.weightOf(term), term);                
            }
        }
    }



}
