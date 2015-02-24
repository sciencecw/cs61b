package synthesizer;
import org.junit.Test;
import static org.junit.Assert.*;

/** Tests the ArrayRingBuffer class.
 *  @author Josh Hug
 */

public class TestArrayRingBuffer {
    @Test
    public void someTest() {
        ArrayRingBuffer arb = new ArrayRingBuffer(10);
		assertEquals(arb.capacity(),10);          
		assertEquals(arb.fillCount(),0);        
		assertTrue(arb.isEmpty());       
		assertFalse(arb.isFull());        
		arb.enqueue(1.1);
		arb.enqueue(2.2);
		arb.enqueue(3.3);
		arb.enqueue(4.4); 
		assertEquals(arb.capacity(),10);          
		assertEquals(arb.fillCount(),4);        
		assertFalse(arb.isEmpty());       
		assertFalse(arb.isFull());
		assertEquals(arb.peek(),1.1,1e-6);  
		assertEquals(arb.dequeue(),1.1,1e-6);
		assertEquals(arb.fillCount(),3);         
		assertEquals(arb.dequeue(),2.2,1e-6);
		assertEquals(arb.dequeue(),3.3,1e-6);
		assertEquals(arb.dequeue(),4.4,1e-6);
		assertTrue(arb.isEmpty());  

		//enqueue and dequeue 9 items      
		for (int i=0;i<10;i++){
			arb.enqueue(1.1);
		}
		assertTrue(arb.isFull());
		for (int i=0;i<10;i++){
			assertEquals(arb.dequeue(),1.1,1e-6);
		}
		assertTrue(arb.isEmpty());


		// fill by 9 items
		for (int i=0;i<9;i++){
			arb.enqueue(i*i*1.1);
		}
		assertFalse(arb.isFull());
		assertFalse(arb.isEmpty());
		// peek millions times
		for (int i=0;i<1000;i++){
			assertEquals(arb.peek(),0.,1e-6);
		}
		System.out.println(arb.dequeue());
		System.out.println(arb.dequeue());
		// peek millions times
		for (int i=0;i<1000;i++){
			assertEquals(arb.peek(),4.4,1e-6);
		}

		//capacity();          
		//fillCount();         
		//isEmpty();       
		//isFull();        
		//enqueue(double x);  
		//dequeue();        
		//peek();           
    }

    /** Calls tests for ArrayRingBuffer. */
    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestArrayRingBuffer.class);
    }
} 