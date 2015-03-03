package ngordnet;
import java.util.TreeMap;
import java.util.NavigableSet;
import java.util.SortedSet;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

public class TimeSeries<T extends Number> extends TreeMap<Integer, T> {    
    /** Constructs a new empty TimeSeries. */
    public TimeSeries(){
      super();
    }

    /** Returns the years in which this time series is valid. Doesn't really
      * need to be a NavigableSet. This is a private method and you don't have 
      * to implement it if you don't want to. */
    private SortedSet<Integer> validYears(int startYear, int endYear){
      SortedSet<Integer> temp = this.navigableKeySet();
      return temp.subSet(startYear, endYear);
    }

    /** Creates a copy of TS, but only between STARTYEAR and ENDYEAR. 
     * inclusive of both end points. */
    public TimeSeries(TimeSeries<T> ts, int startYear, int endYear){
      super();
      for (int i: validYears(startYear,endYear)){
        this.put(i,ts.get(i));
      }
    }

    /** Creates a copy of TS. */
    public TimeSeries(TimeSeries<T> ts){
      super();
      for (int i: ts.navigableKeySet()){
        T value = ts.get(i);
        this.put(i,value);
      }
    }

    /** Returns the quotient of this time series divided by the relevant value in ts.
      * If ts is missing a key in this time series, return an IllegalArgumentException. */
    public TimeSeries<Double> dividedBy(TimeSeries<? extends Number> ts){
      TimeSeries<Double> temp = new TimeSeries<Double>();
      for (Integer i:this.navigableKeySet()){
        if (ts.containsKey(i) && this.containsKey(i)){
          Double quotient = ((Double) this.get(i))/((Double)ts.get(i));
          temp.put(i,quotient);
        }else{
          throw new IllegalArgumentException();
        }
      }
      return temp;
    }

    /** Returns the sum of this time series with the given ts. The result is a 
      * a Double time series (for simplicity). */
    public TimeSeries<Double> plus(TimeSeries<? extends Number> ts){
      TimeSeries<Double> temp = new TimeSeries<Double>();
      for (Integer i:this.navigableKeySet()){
        if (ts.containsKey(i) && this.containsKey(i)){
          Double sum = ((Double) this.get(i))+((Double)ts.get(i));
          temp.put(i,sum);
        }else{
          throw new IllegalArgumentException();
        }
      }
      return temp;

    } 

    /** Returns all years for this time series (in any order). */
    public Collection<Number> years(){
      Collection<Number> temp = new TreeSet<Number>();
      temp.addAll(this.keySet());
      return temp;
    } 

    /** Returns all data for this time series (in any order). */
    public Collection<Number> data(){
      Collection<Number> temp = new TreeSet<Number>();
      temp.addAll(this.values());
      return temp;
    } 
}