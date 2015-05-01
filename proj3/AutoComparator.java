import java.util.Comparator;

public class AutoComparator implements Comparator<TrieNodeAuto> {

	public AutoComparator() {
	}

	public int compare(TrieNodeAuto c1, TrieNodeAuto c2) {
		double diff = c2.getmaxWeight() - c1.getmaxWeight();
		if (diff == 0) {
			diff = diff + c2.getChar()-c1.getChar();
		}
		if (diff > 0) {
			return +1;
		} else if (diff < 0){
			return -1;
		} 
		String s1 = c1.getWord();
		String s2 = c2.getWord();
		if (s1 == null || s2 == null) {
			return 0;
		}
		return s1.compareTo(c2.getWord());
	}

	
}