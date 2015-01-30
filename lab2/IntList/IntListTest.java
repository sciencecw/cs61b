import static org.junit.Assert.*;
import org.junit.Test;

public class IntListTest {

    /** Example test that verifies correctness of the IntList.list static 
     *  method. The main point of this is to convince you that 
     *  assertEquals knows how to handle IntLists just fine.
     */

    @Test 
    public void testList() {
        IntList one = new IntList(1, null);
        IntList twoOne = new IntList(2, one);
        IntList threeTwoOne = new IntList(3, twoOne);

        IntList x = IntList.list(3, 2, 1);
        assertEquals(threeTwoOne, x);
    }

    @Test
    public void testdSquareList() {
      IntList L = IntList.list(1, 2, 3);
      IntList.dSquareList(L);
      assertEquals(IntList.list(1, 4, 9), L);
    }

    /** Do not use the new keyword in your tests. You can create
     *  lists using the handy IntList.list method.  
     * 
     *  Make sure to include test cases involving lists of various sizes
     *  on both sides of the operation. That includes the empty list, which
     *  can be instantiated, for example, with 
     *  IntList empty = IntList.list(). 
     *
     *  Keep in mind that dcatenate(A, B) is NOT required to leave A untouched.
     *  Anything can happen to A. 
     */

    //TODO:  Create testSquareListRecursive()
    @Test
    public void testdSquareListRecursive() {
      IntList L = IntList.list(1, 2, 3);
      IntList Lnew = IntList.squareListRecursive(L);
      assertEquals(IntList.list(1, 2, 3), L);
      assertEquals(IntList.list(1, 4, 9), Lnew);
    }
    //TODO:  Create testDcatenate and testCatenate
    @Test
    public void testDcatenate() {
      // test 1
      IntList L1 = IntList.list(1, 2, 3);
      IntList L2 = IntList.list(6, 5, 4);
      IntList.dcatenate(L1,L2);
      assertEquals(IntList.list(1, 2, 3, 6, 5, 4), L1);
      assertEquals(IntList.list(6, 5, 4), L2);
      // test 2
      IntList L3 = IntList.list(1, 2, 3);
      IntList L4 = null;
      IntList.dcatenate(L3,L4);
      assertEquals(IntList.list(1, 2, 3), L3);
      assertEquals(null, L4);
    }

    @Test
    public void testCatenate() {
      IntList L1 = IntList.list(1, 2, 3);
      IntList L2 = IntList.list(6, 5, 4);
      IntList Lcat = IntList.catenate(L1,L2);
      assertEquals(IntList.list(1, 2, 3), L1);
      assertEquals(IntList.list(6, 5, 4), L2);
      assertEquals(IntList.list(1, 2, 3, 6, 5, 4), Lcat);
      // test 2
      IntList L3 = IntList.list(1, 2, 3);
      IntList L4 = null;
      IntList.catenate(L3,L4);
      assertEquals(IntList.list(1, 2, 3), L3);
      assertEquals(null, L4);
    }


    /* Run the unit tests in this file. */
    public static void main(String... args) {
        jh61b.junit.textui.runClasses(IntListTest.class);
    }       
}   
