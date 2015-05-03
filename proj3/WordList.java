import java.util.Arrays;
import java.util.LinkedList;

/**
 * sorted arraylist for TrieNode 
 * impelemted with fixed size
 * @author Kak Wong
 */

public class WordList {
    private TrieNodeAuto[] nodearray;
    private AutoComparator ac;
    private int size = 0;
    // also indicate where next item is inserted

    /**
     * constructor of list.
     * runs in constant time and takes k memory
     * @param k size of list
     */
    public WordList(int k) {
        nodearray = new TrieNodeAuto[k];
        ac = new AutoComparator(false);
    }

    /**
     * adds a node to the list.
     * the lowest weighted term is dropped if the list is full
     * runs in k^2 time due to sorting
     * @param tNode a TrieNodeAuto object
     */
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

    /**
     * helper method: sorts the array
     */
    private void sortArray() {
        Arrays.sort(nodearray, ac);
    }

    /**
     * check if the list is full
     * take constant time
     * @return true when the list is full
     */
    public boolean isFull() {
        return (size == nodearray.length);
    }

    /**
     * check if node and its child has less weight than objects in this list
     * @param n node of interest
     * @return true when weight of the node and its child is less than the 
     * least item of the list
     */
    public boolean isLighter(TrieNodeAuto n) {
        if (size == 0) {
            return true;
        }
        TrieNodeAuto lastItem = nodearray[size - 1]; // prev item
        return (lastItem.getWeight() > n.getmaxWeight());
    }

    /**
     * returns an iterable in linear time
     * @return an iterable of the items of the list
     */
    public Iterable<String> getIterable() {
        LinkedList<String> itlist = new LinkedList<String>();
        for (int i = 0; i < size; i++) {
            itlist.add(nodearray[i].getWord());
        }
        return itlist;
    }

    /**
     * returns string representation
     * @return a string
     */
    @Override
    public String toString() {
        return Arrays.toString(nodearray);
    }
}
