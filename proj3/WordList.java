import java.util.Arrays;
import java.util.LinkedList;

public class WordList {
    private TrieNodeAuto[] nodearray;
    private AutoComparator ac;
    private int size = 0;
    // also indicate where next item is inserted

    public WordList(int k) {
        nodearray = new TrieNodeAuto[k];
        ac = new AutoComparator(false);
    }

    public void add(TrieNodeAuto tNode) {
        if (size < nodearray.length) {
            nodearray[size] = tNode;
            sortArray();
            size++;
        } else {
            TrieNodeAuto lastItem = nodearray[size - 1];
            if (ac.compare(lastItem, tNode) > 0) {
                nodearray[size - 1] = tNode;
                sortArray();
            }
        }
    }

    private void sortArray() {
        Arrays.sort(nodearray, ac);
    }

    public boolean isFull() {
        return (size == nodearray.length);
    }

    public boolean isLighter(TrieNodeAuto n) {
        if (size == 0) {
            return true;
        }
        TrieNodeAuto lastItem = nodearray[size - 1]; // prev item
        return (ac.compare(lastItem, n) < 0);
    }

    public Iterable<String> getIterable() {
        LinkedList<String> itlist = new LinkedList<String>();
        for (int i = 0; i < size; i++) {
            itlist.add(nodearray[i].getWord());
        }
        return itlist;
    }

    @Override
    public String toString() {
        return Arrays.toString(nodearray);
    }

    public static void main(String[] args) {
        WordList wl = new WordList(5);
        wl.add(new TrieNodeAuto('c', "chic", 100.0));
        wl.add(new TrieNodeAuto('d', "sdfd", 120.3));
        wl.add(new TrieNodeAuto('a', "sdfa", 103.0));
        wl.add(new TrieNodeAuto('p', "casdgp", 1000.4));
        System.out.println(wl);

        TrieNodeAuto temp = new TrieNodeAuto('d', "fds", 90.4);
        System.out.println(wl.isLighter(temp));

        wl.add(temp);
        System.out.println(wl);

        temp = new TrieNodeAuto('d', "fds", 201.8);
        wl.add(temp);
        System.out.println(wl);
        System.out.println(wl.isLighter(new TrieNodeAuto('d', "f", 100.4)));

    }
}