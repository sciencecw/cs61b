import java.util.Set; /* java.util.Set needed only for challenge problem. */
import java.util.Iterator;

/** A data structure that uses a linked list to store pairs of keys and values.
 *  Any key must appear at most once in the dictionary, but values may appear multiple
 *  times. Supports get(key), put(key, value), and contains(key) methods. The value
 *  associated to a key is the value in the last call to put with that key. 
 *
 *  For simplicity, you may assume that nobody ever inserts a null key or value
 *  into your map.
 */ 
public class ULLMap<K, V> implements Map61B<K, V>,Iterable<K> { //FIX ME
    /** Keys and values are stored in a linked list of Entry objects.
      * This variable stores the first pair in this linked list. You may
      * point this at a sentinel node, or use it as a the actual front item
      * of the linked list. 
      */
    private Entry<K,V> front;
    private int size=0;

    @Override
    public V get(K key) { 
        // front is null
        if (front == null){
            return null;
        }
        if (this.containsKey(key)){
        // if there is such key 
            Entry<K,V> entry = front.get(key);
            return entry.val;
        }
        //there are no such key. return null
        return null; 
    }

    @Override
    public void put(K key, V val) { //FIX ME
        if (key==null||val==null){
            return;
        }
        // check every existing entry. Put value if matched key is found
        for (Entry<K,V> pointer=front; pointer!=null;pointer=pointer.next){
            if (pointer.key==key){
                pointer.val = val;
                return;
            }
        }
        // otherwise, put entry in front and increment count
        front = new Entry<K,V>(key,val,front);
        size =size+1;
        return;
    }

    @Override
    public boolean containsKey(K key) { //FIX ME
        // front cases
        if (front==null){
            return false;
        }else if (front.key == key){
            return true;
        }
        Entry<K,V> pointer = front;
        while (pointer.next!=null){
            pointer =pointer.next;
            if (pointer.key == key){
                return true;
            }
        }        
        return false; //FIX ME
    }

    @Override
    public int size() {
        return size; // FIX ME (you can add extra instance variables if you want)
    }

    @Override
    public void clear() {
        size = 0;
        front =null;
        return;
    }

    public static <V,K> ULLMap<V,K> invert(ULLMap<K,V> map){
        ULLMap<V,K> temp = new ULLMap<V,K>();
        for (K key:map){
            temp.put(map.get(key),key);
        }
        return temp;
    }

    //returns iterator
    public Iterator<K> iterator(){
        return new ULLMapIter();
    }


    // iterator class
    public class ULLMapIter implements Iterator<K> { 
    //ULLMapIter does not need <K> for it does not declare a new generic
        private Entry<K,V> pointer = front;

        @Override
        public boolean hasNext(){
            return (pointer!=null);
        }

        @Override
        public K next(){
            Entry<K,V> entryreturned = pointer;
            pointer =pointer.next;
            return entryreturned.key;
        }

        @Override
        public void remove(){
            throw new UnsupportedOperationException();
        }
        
    }


    /** Represents one node in the linked list that stores the key-value pairs
     *  in the dictionary. */
    private class Entry<K,V> {
    
        /** Stores KEY as the key in this key-value pair, VAL as the value, and
         *  NEXT as the next node in the linked list. */
        public Entry(K k, V v, Entry<K,V> n) { //FIX ME
            key = k;
            val = v;
            next = n;
        }

        /** Returns the Entry in this linked list of key-value pairs whose key
         *  is equal to KEY, or null if no such Entry exists. */
        public Entry<K,V> get(K k) { //FIX ME
            //FILL ME IN (using equals, not ==)
            if (key.equals(k)){
                return this;
            }else if (next!=null){
                return next.get(k);
            }
            return null;
        }

        /** Stores the key of the key-value pair of this node in the list. */
        private K key; //FIX ME
        /** Stores the value of the key-value pair of this node in the list. */
        private V val; //FIX ME
        /** Stores the next Entry in the linked list. */
        private Entry<K,V> next;
    
    }

    /* Methods below are all challenge problems. Will not be graded in any way. 
     * Autograder will not test these. */
    @Override
    public V remove(K key) { //FIX ME SO I COMPILE
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key, V value) { //FIX ME SO I COMPILE
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<K> keySet() { //FIX ME SO I COMPILE
        throw new UnsupportedOperationException();
    }


}