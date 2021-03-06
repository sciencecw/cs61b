/* Radix.java */

package radix;
//import edu.princeton.cs.introcs.StdRandom;
//import java.util.Arrays;

/**
 * Sorts is a class that contains an implementation of radix sort.
 * @author
 */
public class Sorts {


    /**
     *  Sorts an array of int keys according to the values of <b>one</b>
     *  of the base-16 digits of each key. Returns a <b>NEW</b> array and
     *  does not modify the input array.
     *  
     *  @param key is an array of ints.  Assume no key is negative.
     *  @param whichDigit is a number in 0...7 specifying which base-16 digit
     *    is the sort key. 0 indicates the least significant digit which
     *    7 indicates the most significant digit
     *  @return an array of type int, having the same length as "keys"
     *    and containing the same keys sorted according to the chosen digit.
     **/
    public static int[] countingSort(int[] keys, int whichDigit) {
        //YOUR CODE HERE
        int[] nkey = new int[keys.length];
        int j = 0;
        for (int digit = 0; digit <  16; digit++) {
            for (int i = 0; i < keys.length; i++) {
                if ( ((keys[i] >>> (whichDigit * 4)) & 15) == digit) {
                    nkey[j] = keys[i];
                    j++;
                }
            }
        }
        return nkey;
    }

    /**
     *  radixSort() sorts an array of int keys (using all 32 bits
     *  of each key to determine the ordering). Returns a <b>NEW</b> array
     *  and does not modify the input array
     *  @param key is an array of ints.  Assume no key is negative.
     *  @return an array of type int, having the same length as "keys"
     *    and containing the same keys in sorted order.
     **/
    public static int[] radixSort(int[] keys) {
        //YOUR CODE HERE
        int[] temp = new int[keys.length];
        System.arraycopy(keys, 0, temp, 0, keys.length);
        for (int d = 0; d < 8; d++) {
            temp = countingSort(temp, d);
            //System.out.println("Sorting: " + Arrays.toString(temp));
        }
        return temp;
    }

    /*public static void main(String[] args) {
        int[] a = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20};
        StdRandom.shuffle(a);
        int[] b = radixSort(a);
        System.out.println(Arrays.toString(a));
        System.out.println(Arrays.toString(b));
    }
    */

}
