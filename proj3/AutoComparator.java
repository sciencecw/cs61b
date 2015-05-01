import java.util.Comparator;

public class AutoComparator implements Comparator<TrieNodeAuto> {

    public boolean isMaxweight;

    public AutoComparator(boolean isMaxw) {
        isMaxweight = isMaxw;
    }

    @Override
    public int compare(TrieNodeAuto c1, TrieNodeAuto c2) {
        double diff = 0;
        if (isMaxweight) {
            diff = c2.getmaxWeight() - c1.getmaxWeight();           
            //System.out.println("max " + c2.getmaxWeight() + " " + c1.getmaxWeight() + " " + (int) diff);
        } else {
            diff = c2.getWeight() - c1.getWeight();
            //System.out.println(c2.getWeight() + " " + c1.getWeight() + " " + diff);
        }
        return (int) diff; 
    }

    
}