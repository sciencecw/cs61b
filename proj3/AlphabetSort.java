import java.util.Scanner;

public class AlphabetSort{

    private char[] charset;
    private Trie trie;

    public AlphabetSort(char[] c) {
        charset = c;
        trie = new Trie();
    }

    private void addword(String s) {
        if (s != null && !s.isEmpty()) {
            trie.insert(s);
        }
    }

    private String sortString() {
        return trie.dfs(charset);
    }

    private static void nextlineCheck(Scanner s) {
        if (!s.hasNextLine()) {
            throw new IllegalArgumentException();
        }
    }

    private static void charCheck(char[] c) {
        for (int i = 0; i < c.length; i++) {
            for (int j = i + 1; j < c.length; j++) {
                if (c[i] == c[j]) {
                    throw new IllegalArgumentException();
                }
            }
        }
    }

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