import java.util.Scanner;

/**
 *  implement a sorting method for strings
 *  based on trie with dfs graph traversal
 * @author Kak Wong
 */

public class AlphabetSort {

    private char[] charset;
    private Trie trie;

    /**
     * Initializes AlphabetSort
     * @param c character array determining sort order
     */
    public AlphabetSort(char[] c) {
        charset = c;
        trie = new Trie();
    }

    /**
     * add word to trie
     * @param s input string
     */
    private void addword(String s) {
        if (s != null && !s.isEmpty()) {
            trie.insert(s);
        }
    }

    /**
     * helper method: sort strings
     * @return string representing dfs sorting
     */
    private String sortString() {
        return trie.dfs(charset);
    }

    /**
     * helper method: check if input has next line
     * @param s scanner
     */
    private static void nextlineCheck(Scanner s) {
        if (!s.hasNextLine()) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * helper method: check if there are duplicate char
     * if yes, throw IllegalsArgumentExecption
     * @param c character array
     */
    private static void charCheck(char[] c) {
        for (int i = 0; i < c.length; i++) {
            for (int j = i + 1; j < c.length; j++) {
                if (c[i] == c[j]) {
                    throw new IllegalArgumentException();
                }
            }
        }
    }

    /**
     * main method: for testing purpose only
     * @param args required field
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        nextlineCheck(sc);
        String s = sc.nextLine();
        char[] carray = s.toCharArray();
        charCheck(carray);
        AlphabetSort as = new AlphabetSort(carray);
        nextlineCheck(sc);
        while (sc.hasNextLine()) {
            s = sc.nextLine();
            as.addword(s);
        }
        System.out.println(as.sortString());
    }
}
