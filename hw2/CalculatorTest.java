import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CalculatorTest {
    /* Do not change this to be private. For silly testing reasons it is public. */
    public Calculator tester;

    /**
     * setUp() performs setup work for your Calculator.  In short, we 
     * initialize the appropriate Calculator for you to work with.
     * By default, we have set up the Staff Calculator for you to test your 
     * tests.  To use your unit tests for your own implementation, comment 
     * out the StaffCalculator() line and uncomment the Calculator() line.
     **/
    @Before
    public void setUp() {
        // tester = new StaffCalculator(); // Comment me out to test your Calculator
        tester = new Calculator();   // Un-comment me to test your Calculator
    }

    // TASK 1: WRITE JUNIT TESTS
    // YOUR CODE HERE
    @Test
    public void testadd1() {
	// positive and positive
    	assertEquals(3,tester.add(1,2));
    	assertEquals(20,tester.add(17,3));
    	assertEquals(8,tester.add(5,3));
    	assertEquals(8,tester.add(3,5));
    	assertEquals(5,tester.add(0,5));
    	assertEquals(3,tester.add(3,0));
    	assertEquals(0,tester.add(0,0));
    }

    @Test
    public void testadd2() {
	// positive and negative
    	assertEquals(-3,tester.add(10,-13));
    	assertEquals(-14,tester.add(-17,3));
    	assertEquals(8,tester.add(15,-7));
    	assertEquals(-7,tester.add(3,-10));
    }

    @Test
    public void testadd3() {
	// negative and negative
    	assertEquals(-333,tester.add(-100,-233));
    	assertEquals(-20,tester.add(-17,-3));
    	assertEquals(-8,tester.add(-5,-3));
    	assertEquals(-7,tester.add(-3,-4));
    }

    @Test
    public void testmultiply(){
       assertEquals(10,tester.multiply(2,5));
       assertEquals(-8,tester.multiply(2,-4));
       assertEquals(-72375,tester.multiply(375,-193));
       assertEquals(0,tester.multiply(0,-48));
       assertEquals(0,tester.multiply(-99,0));
    }

    /* Run the unit tests in this file. */
    public static void main(String... args) {
        jh61b.junit.textui.runClasses(CalculatorTest.class);
    }       
}
