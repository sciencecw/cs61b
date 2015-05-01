import java.util.Comparator;

public class AutoComparator implements Comparator<TrieNodeAuto> {

	public AutoComparator() {
	}

	public int compare(TrieNodeAuto c1, TrieNodeAuto c2) {
		double diff = c1.getmaxWeight() - c2.getmaxWeight();
		if (diff > 0) {
			return 1;
		} else if (diff < 0){
			return -1;
		} 
		return c1.getWord().compareTo(c2.getWord());
	}

	
}