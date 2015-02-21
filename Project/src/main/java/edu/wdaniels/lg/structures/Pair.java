package edu.wdaniels.lg.structures;

/**
 *
 * @author William
 * @param <T> This is the first element of the pair
 * @param <U> This is the second element of the pair.
 */
public class Pair<T, U> implements Comparable<Pair> {

    private T firstItem;
    private U secondItem;

    /**
     * This is the default constructor of the Pair object that takes in a first
     * and a second value.
     *
     * @param first the first value in the pair
     * @param second The second value in the pair
     */
    public Pair(T first, U second) {
        firstItem = first;
        secondItem = second;
    }

    /**
     * This is the defazult constructor of Pair, which allows the user to
     * specify the first and second datums later, separately.
     */
    public Pair() {
        this(null, null);
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

    @Override
    public String toString() {
        return (firstItem + ", " + secondItem);
    }

    @Override
    public int compareTo(Pair t) {
        if ((this.firstItem == t.firstItem) && (this.secondItem == t.secondItem)){
            return 1;
        }
        return -1;
    }

}
