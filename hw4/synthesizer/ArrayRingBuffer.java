// Make sure to make this class a part of the synthesizer package
package synthesizer;

public class ArrayRingBuffer extends AbstractBoundedQueue {
  /* Index for the next dequeue or peek. */
  private int first;           
  /* Index for the next enqueue. */
  private int next;             
  /* Array for storing the buffer data. */
  private double[] rb;

  /** Create a new ArrayRingBuffer with the given capacity. */
  public ArrayRingBuffer(int capacity) {
    // TODO: Create new array with capacity elements.
    //       first, last, and fillCount should all be set to 0. 
    //       this.capacity should be set appropriately. Note that the local variable
    //       here shadows the field we inherit from AbstractBoundedQueue.
    this.rb = new double[capacity];
    this.first = 0;
    this.next = 0;
    this.fillCount =0;
    this.capacity = capacity;
  }

  /** Adds x to the end of the ring buffer. If there is no room, then
    * throw new RuntimeException("Ring buffer overflow") 
    */
  public void enqueue(double x) {
    // TODO: Enqueue the item. Don't forget to increase fillCount and update last.
    // is there room?
    if (isFull()){
      throw new RuntimeException("Ring buffer overflow");
    }else{
      fillCount+=1;
      rb[next]=x;
      next+=1;
      if (next>=capacity){
        next=0;
      }
    }
    return;
  }

  /** Dequeue oldest item in the ring buffer. If the buffer is empty, then
    * throw new RuntimeException("Ring buffer underflow");
    */
  public double dequeue() {
    // TODO: Dequeue the first item. Don't forget to decrease fillCount and update first.
    if (isEmpty()){
      throw new RuntimeException("Ring buffer underflow");
    }
    double temp;
    temp = rb[first];
    first+=1;
    fillCount-=1;
    if (first>=capacity){
        first=0;
    }
    return temp;
  }

  /** Return oldest item, but don't remove it. */
  public double peek() {
    if (isEmpty()){
      throw new RuntimeException("Ring buffer underflow");
    }
    return rb[first];
    // TODO: Return the first item. None of your instance variables should change.
  }

}
