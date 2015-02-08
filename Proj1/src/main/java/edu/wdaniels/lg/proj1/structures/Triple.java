package edu.wdaniels.lg.proj1.structures;

/**
 *
 * @author William
 * @param <T> This is the first element of the Triple
 * @param <U> This is the second element of the Triple.
 * @param <V> This is the third element of the Triple
 */
public class Triple<T, U, V> implements Comparable<Triple> {

    private T firstItem;
    private U secondItem;
    private V thirdItem;

    /**
     * This is the default constructor of the Triple object that takes in a first
     * and a second value.
     *
     * @param first the first value in the Triple
     * @param second The second value in the Triple
     * @param third the third value in the Triple
     */
    public Triple(T first, U second, V third) {
        firstItem = first;
        secondItem = second;
        thirdItem = third;
    }

    /**
     * This is the default constructor of Triple, which allows the user to
     * specify the first and second datums later, separately.
     */
    public Triple() {
        this(null, null, null);
    }

    /**
     * This method sets the first of the two items.
     *
     * @param input the new item to assign the first item to.
     */
    public void setFirst(T input) {
        firstItem = input;
    }

    /**
     * This method sets the second of the two items
     *
     * @param input the new item to assign the second item to.
     */
    public void setSecond(U input) {
        secondItem = input;
    }
    
    public void setThird(V input){
        thirdItem = input;
    }

    /**
     * This method is for getting the first of the two items.
     *
     * @return the first item of type T
     */
    public T getFirst() {
        return firstItem;
    }

    /**
     * This method is for getting the second of the two items.
     *
     * @return the second item of type U
     */
    public U getSecond() {
        return secondItem;
    }
    
    public V getThird(){
        return thirdItem;
    }

    @Override
    public String toString() {
        return (firstItem + ", " + secondItem + ", " + thirdItem);
    }

    @Override
    public int compareTo(Triple t) {
        if ((this.firstItem == t.firstItem) && (this.secondItem == t.secondItem) && (this.thirdItem == t.thirdItem)){
            return 1;
        }
        return -1;
    }

}
