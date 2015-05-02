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
        double diff = 0;
        if (isMaxweight) {
            diff = c2.getmaxWeight() - c1.getmaxWeight();           
            //System.out.println("max " + c2.getmaxWeight() + " " + c1.getmaxWeight() + " " + (int) diff);
        } else {
            diff = c2.getWeight() - c1.getWeight();
            //System.out.println(c2.getWeight() + " " + c1.getWeight() + " " + diff);
        }
        if (diff == 0) {
            diff = c2.getChar() - c1.getChar();
        }
        return (int) diff; 
    }

    
}