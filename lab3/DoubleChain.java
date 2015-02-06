
public class DoubleChain {
	
	private DNode head;
	
	public DoubleChain(double val) {
		/* your code here. */
		head = new DNode(val); 
	}

	public DNode getFront() {
		return head;
	}

	/** Returns the last item in the DoubleChain. */		
	public DNode getBack() {
		/* your code here */
		DNode pointer = head;
		while (pointer.next !=null){
			pointer=pointer.next;
		}
		return pointer;
	}
	
	/** Adds D to the front of the DoubleChain. */	
	public void insertFront(double d) {
		/* your code here */
		DNode temp =head;
		head = new DNode(null,d,temp);
		temp.prev = head;
		return;
	}
	
	/** Adds D to the back of the DoubleChain. */	
	public void insertBack(double d) {
		/* your code here */
		DNode pointer = getBack();
		DNode temp = new DNode(pointer,d,null);
		pointer.next = temp;
		return;
	}
	
	/** Removes the last item in the DoubleChain and returns it. 
	  * This is an extra challenge problem. */
	public DNode deleteBack() {
		/* your code here */
		DNode pointer = getBack();
		DNode temp = pointer;
		pointer.prev.next = null; // TODO CHECK THIS 
		return temp;
	}
	
	/** Returns a string representation of the DoubleChain. 
	  * This is an extra challenge problem. */
	public String toString() {
		/* your code here */		
		/* String rep = new String();
 		DNode pointer = head;
		while (pointer.next != null){
			rep = rep + "->[" + pointer.val+ "]";
			pointer = pointer.next;
		}
		return rep; */
		return toString(head);
	}
	
	private String toString(DNode pointer){
		if (pointer == null){
			return "";
		}
		String rep = "->[" + pointer.val+ "]"+toString(pointer.next);
		return rep;
		
	}

	public static class DNode {
		public DNode prev;
		public DNode next;
		public double val;
		
		private DNode(double val) {
			this(null, val, null);
		}
		
		private DNode(DNode prev, double val, DNode next) {
			this.prev = prev;
			this.val = val;
			this.next =next;
		}
	}
	
}
