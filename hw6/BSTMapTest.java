import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Iterator;

/** BSTMapTest. You should write additional tests.
 *  @author Josh Hug
 */

public class BSTMapTest {
    @Test
    public void testBasic() {
        BSTMap<String, String> um = new BSTMap<String, String>();
        assertEquals(um.size(),0);
        assertFalse(um.containsKey("Gracias"));
        //test put and get
        um.put("Gracias", "Dios Basado");
        assertEquals(1, um.size());
        assertEquals(um.get("Gracias"), "Dios Basado");
        um.put("Gracias", "Hello");
        assertEquals(um.get("Gracias"), "Hello");
        um.put("Gracias", "Dios Basado");
        assertEquals(um.get("Gracias"), "Dios Basado");
        um.printInOrder();
        //test size
        assertEquals(1, um.size());
        um.put("Agua", "Water");
        assertEquals(um.get("Gracias"),"Dios Basado");
        assertTrue(um.containsKey("Agua"));
        assertTrue(um.containsKey("Gracias"));
        assertEquals(um.size(), 2);
        um.printInOrder();

        um.clear();
        assertEquals(um.get("Gracias"),null);
        assertFalse(um.containsKey("Agua"));
        assertEquals(um.size(),0);
        um.printInOrder();
    }

    
    @Test
    public void testIterator() {
        BSTMap<Integer, String> um = new BSTMap<Integer, String>();
        um.put(0, "zero");
        um.put(1, "one");
        um.put(2, "two");
        um.printInOrder();
    }

    @Test
    public void testInverse() {
        BSTMap<Integer, String> um = new BSTMap<Integer, String>();
        um.put(0, "zero");
        um.put(1, "one");
        um.put(2, "two");
        um.put(3, "zero");
        um.put(4, "zero");
        um.put(5, "Five");
        um.put(6, "Five");
        um.put(10, "Ten");
        assertEquals(um.size(),8);

        um.printInOrder();
    }
    

    /** Runs tests. */
    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(BSTMapTest.class);
    }
} 