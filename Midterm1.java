Template for CS61b Midterm 1 Cheatsheet

CHAPTER1 Basics

public class Automobile{
	/*  Fields (Static and non-static) 			*
	 *  (private, public, protected, default) 	*/
	private int field1;
	static String name = "Automobile";

	/* Class Constructors */
	public Automobile(){					// default empty constructor
		field1=0;
	}
	public Automobile(int field1){			// non empty constructor
		this.field1 = field1;
	}

	/* Methods */
	public void donothing(){				// public void method
		doNothingHelper(0);
		return;
	}
	private int doNothingHelper(int input){ // private helper method; 
		return input;						// takes int, returns int
	}
	public static void printAutoName(){		// static method
		System.out.println(Automobile.name);
		return;
	}

	/* Main Method! */
	public static void main(String[] args) {
		Automobile.printAutoName();				// static method call
		Automobile car0;						// variable declaration
		Automobile car1 = new Automobile();		// object instatiation
		car1.donothing(); 						// invoke non-static method

		String s = new String();				// Initiate strings
		int[] a = new int[3]{1,2,3}				// array Declaration, Instatiation and Assignment
		s="string";
		s.equals("string")						// Comparing strings (!)
		if (args!= null || args.length>0){		// ALWAYS check edge cases
			Integer.parseInt(args[0])			// Parsing integers
		}
	}
}


CHAPTER2 Testing
import org.junit.Test;
import static org.junit.Assert.*;
public class CalTest{
  	@Test
    public void testadd1() {
    	assertEquals(3,Cal.add(1,2));
    }
    public static void main(String... args) {
        jh61b.junit.textui.runClasses(CalTest.class);
    }       
}

CHAPTER3 Bitwise Operations

//byte 128, short 32768, int 2147483647, long , char 0-65535
public class Bitwise{
	1010 & 1100 = 1000; //(And)
	1010 | 1100 = 1110; //(Or)
	1010 ^ 1100 = 0110; //(Xor)
	~1010 = 0101;	   //(Not)
	10001101 >> 3  =11110001; //(top bit copied) 
	10001101 >>> 3 =00010001; //(top bit zeros) 
	10001101 << 3  =01101000; //(bottom bit zero)
}


CHAPTER4 Hiding and Overriding

public class MaxSList extends SList{
	protected int max;		//subclasses can use this
	public MaxSList(int x){ //default SList() is called if we call MaxSList()
		super(int x);
	}
	@Override
	public void insertBack(){
		super.insertBack();
		return;
	}
}
Automobile x = (Automobile) new Car("Prius"); // casting


1. Field only depends on static type (Hiding)
2 a. Method calls depend on the signature func(Input i) which match static type
  b. At runtime, program searches for Overriding method with same signature for the dynamic type
  c. if none is found it falls back to static type method calls
  d. static method always depend on static type

// interfaces: to be implemented. No concrete methods yet
public interface IntUnaryFunction {
    int apply(int x);
}
// ADT: To be extended, can implement interface
public abstract class AbstractXList implements XList{
	public int getback(){
		return 0; // ADT does not implement all methods
	}
	public abstract int newAbstractmethod(); // new abstract method
}

CHAPTER5 Comparables
// both not static! Must have constructor
public class Dog implements Comparable<Dog>{
	public int compareTo(Dog d1) {return 0;}
}
public class DogComparator implements Comparator<Dog>{
	public int compare(Dog d1, Dog d2) {return 0;}
	public boolean equals(Object o) {return false;}
}
import java.util.Comparator;
import java.util.Arrays;
Arrays.sort(objects, comparator);



