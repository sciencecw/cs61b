package ngordnet;

import org.junit.Test;
import static org.junit.Assert.*;

public class WordNetTest {
	@Test
    public void testBasic() {
    	WordNet wn = new WordNet("wordnet/synsets11.txt", "wordnet/hyponyms11.txt");
    	System.out.println(wn.wMap);
    }

    @Test
    public void testBasic2() {
    	WordNet wn2 = new WordNet("wordnet/synsets14.txt", "wordnet/hyponyms14.txt");
    	System.out.println(wn2.wMap);
    }

    @Test
    public void testIsNoun_Basic() {
    	WordNet wn = new WordNet("wordnet/synsets11.txt", "wordnet/hyponyms11.txt");
    	assertTrue(wn.isNoun("jump"));
    	assertTrue(wn.isNoun("leap"));
    	assertTrue(wn.isNoun("actifed"));
    	assertFalse(wn.isNoun("air"));
    	assertFalse(wn.isNoun("dummy"));
    }

    /** Runs tests. */
    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(WordNetTest.class);
    }
}