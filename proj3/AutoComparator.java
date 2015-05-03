import java.util.Comparator;

public class AutoComparator implements Comparator<TrieNodeAuto> {

    public boolean isMaxweight;

    public AutoComparator(boolean isMaxw) {
        isMaxweight = isMaxw;
    }

    @Override
    public int compare(TrieNodeAuto c1, TrieNodeAuto c2) {
        if (c1 == c2) return 0;
        if (c1 == null)       return 1;
        else if (c2 == null)  return -1;
        double diff;
        if (isMaxweight) {
            diff = c2.getmaxWeight() - c1.getmaxWeight();           
        } else {
            diff = c2.getWeight() - c1.getWeight();
        }
        if (diff > 0) {
            return 1;
        } else if (diff < 0) {
            return -1;
        } 
        return (int) (c2.getChar() - c1.getChar()); 
    }
}