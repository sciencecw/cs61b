import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Iterator;

/** ULLMapTest. You should write additional tests.
 *  @author Josh Hug
 */

public class ULLMapTest {
    @Test
    public void testBasic() {
        ULLMap<String, String> um = new ULLMap<String, String>();
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
        //test size
        assertEquals(1, um.size());
        um.put("Agua", "Water");
        assertEquals(um.get("Gracias"),"Dios Basado");
        assertTrue(um.containsKey("Agua"));
        assertTrue(um.containsKey("Gracias"));
        assertEquals(um.size(), 2);

        um.clear();
        assertEquals(um.get("Gracias"),null);
        assertFalse(um.containsKey("Agua"));
        assertEquals(um.size(),0);
    }

    
    @Test
    public void testIterator() {
        ULLMap<Integer, String> um = new ULLMap<Integer, String>();
        um.put(0, "zero");
        um.put(1, "one");
        um.put(2, "two");
        Iterator<Integer> umi = um.iterator();
        System.out.println(umi.next());
        for (int i:um){
            //System.out.println(i);
        }
    }

    @Test
    public void testInverse() {
        ULLMap<Integer, String> um = new ULLMap<Integer, String>();
        um.put(0, "zero");
        um.put(1, "one");
        um.put(2, "two");
        um.put(3, "zero");
        um.put(4, "zero");
        um.put(5, "Five");
        um.put(6, "Five");
        um.put(10, "Ten");
        assertEquals(um.size(),8);

        //first inverse
        System.out.println("first inverse");
        ULLMap<String,Integer> ivum = ULLMap.invert(um);
        for (String s:ivum){
            System.out.print("("+s+", "+ivum.get(s)+") ");
        }
        System.out.println(" ");
        assertEquals(ivum.size(),5);
        ivum.put("decade",10);

        //second inverse
        System.out.println("second inverse");
        ULLMap<Integer, String> ivivum = ULLMap.invert(ivum);
        for (int i:ivivum){
            System.out.print("("+i+", "+ivivum.get(i)+")");
        }
        System.out.println(" ");
        assertEquals(ivivum.size(),5);
    }
    

    /** Runs tests. */
    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(ULLMapTest.class);
    }
} 