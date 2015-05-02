import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

public class AutocompleteTest {

    static TrieAuto trie;

    @Before
    public void setUp() {
        trie = new TrieAuto();
        trie.insert("town", 20568700.00);
        trie.insert("towns", 35240.20);
        trie.insert("township", 4029.24);
        trie.insert("Kag" , 205600);
        trie.insert("Karjat, India", 34554);
        trie.insert("Kandahār, Afghanistan"     ,391190);
        trie.insert("Kandy, Sri Lanka"          ,111701);
        trie.insert("Kandi, Benin"              ,109701);
        trie.insert("Kandhkot, Pakistan"         ,88468);
        trie.insert("Kandukūr, India"            ,53662);
        trie.insert("Kandalaksha, Russia"        ,38431);
        trie.insert("Kanda, Japan"               ,36122);
        trie.insert("Kandana, Sri Lanka"         ,33424);
        trie.insert("Kandiāro, Pakistan"         ,26807);
        trie.insert("Kandıra, Turkey"            ,13805);
        trie.insert("Kandry, Russia"             ,12100);
        trie.insert("Kandé, Togo"                ,11466);
        trie.insert("Kandete, Tanzania"          ,10318);
        trie.insert("Kandel, Germany"            , 8385);
        trie.insert("Kandri, India"              , 8315);
        trie.insert("Kandern, Germany"           , 8070);
        trie.insert("Kandiāri, Pakistan"         , 6861);
        trie.insert("Kandava, Latvia"            , 3592);
        trie.insert("Kandabong, Philippines"     , 2367);
        trie.insert("Kandíla, Greece"            , 1245);
        trie.insert("Kandersteg, Switzerland"    , 1123);
        trie.insert("Kandrian, Papua New Guinea" , 1014);
        trie.insert("Kalisz, Poland", 108759);
        trie.insert("Kai" , 100);
        trie.insert("Kar" , 100);
    }

    @Test
    public void testBasic() {

        TrieNodeAuto tNode = trie.getRootnode();

        while (tNode.hasSubnode()) {
            TrieNodeAuto ttemp = tNode.getmaxNode();
            if (ttemp.getmaxWeight() >= tNode.getmaxWeight()) {
                tNode = ttemp;
            } else {
                break;
            }
        }

        assertEquals("town", tNode.getWord());
    }

    @Test
    public void testAutocompleteBasic() {

        Autocomplete acp = new Autocomplete(trie);
        assertEquals("Kandahār, Afghanistan", acp.topMatch("Kand"));

    }

    @Test
    public void testTopMatches() {
        Autocomplete acp = new Autocomplete(trie);
        Iterable<String> it = acp.topMatches("Kand",  3);
        for (String s: it) {
            System.out.println(s);
        }

    }


    /** Runs tests. */
    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(AutocompleteTest.class);
    }

}
