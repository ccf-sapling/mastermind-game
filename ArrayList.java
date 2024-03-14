import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayList<E> implements List<E>{
    // ------------------ nested ArrayIterator class ------------------
    /**
     * A nonstatic inner class. Note that each instance contains an implicit reference
     * to the containing list, allowing it to access the list's members.
     */
    private class ArrayIterator implements Iterator<E> {
        // instance variables
        private int j = 0; // index of the next element to report
        private boolean removable = false; // can remove be called at this time?

        // public methods

        /**
         * Tests whether the iterator has a next object.
         * @return true if there are further objects, false otherwise
         */
        public boolean hasNext() {
            return j < size; // size is field of outer instance
        }

        /**
         * Returns the next onject in the iterator
         * @return next object
         * @throws NoSuchElementException if there are no further elements
         */
        public E next() throws NoSuchElementException {
            if (j == size) throw new NoSuchElementException("No next element");
            this.removable = true; // this element can subsequently be removed
            return data[j++]; // post-increment j, so it is ready for future call to next
        }

        /**
         * Removes the element returned by most recent call to next.
         * @throws IllegalStateException if next has not yet been called
         * @throws IllegalStateException if remove was already called since recent next
         */
        public void remove() throws IllegalStateException {
            if (!removable) throw new IllegalStateException("Nothing to remove");
            ArrayList.this.remove(j - 1); // that was the last one returned
            j--; // next element has shifted one cell to the left
            removable = false; // don't allow remove again until next is called
        }
    }
    // ------------------ end of nested ArrayIterator class ------------------

    // instance variables
    public static final int CAPACITY = 4; // default capacity is 4
    private E[] data; // generic array used for storage
    private int size = 0; // current number of elements in array
    private int currentCapacity = CAPACITY; // Used to track the current capacity of the list

    // constructors
    public ArrayList() {
        this(CAPACITY);
    }

    public ArrayList(int capacity) {
        this.data = (E[]) new Object[capacity]; // casting Object to generic array
    }

    // public methods

    /**
     * Returns the number of elements in the array list.
     * @return int
     */
    public int size() {
        return this.size;
    }

    /**
     * Returns whether the array list is empty.
     * @return boolean
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns (but does not remove) the element at index i.
     * @param i the index of the element to return
     * @return E
     * @throws IndexOutOfBoundsException Thrown if trying to get an index that's out of bounds
     */
    public E get(int i) throws IndexOutOfBoundsException {
        checkIndex(i, this.size);
        return this.data[i];
    }

    /**
     * Replaces the element at index i with e, and returns the replaced element.
     * @param i the index of the element to replace
     * @param e the new element to be stored
     * @return E
     * @throws IndexOutOfBoundsException Thrown if trying to get an index that's out of bounds
     */
    public E set(int i, E e) throws IndexOutOfBoundsException {
        checkIndex(i, this.size);
        E temp = this.data[i];
        this.data[i] = e;
        return temp;
    }

    /**
     * Inserts element e to be at index i, shifting all subsequent elements later.
     * @param i the index at which the new element should be stored
     * @param e the new element to be stored
     * @throws IndexOutOfBoundsException Thrown if trying to add to an index that's out of bounds
     * @throws IllegalStateException Thrown if trying to add multiple indexes ahead of size
     */
    public void add(int i, E e) throws IndexOutOfBoundsException, IllegalStateException {
        checkIndex(i, this.size + 1);
        if (this.size == this.data.length) {
            //throw new IllegalStateException("Array is full"); // not enough capacity
            resize(2 * this.data.length); // double the capacity
            this.currentCapacity = this.data.length; // track new capacity of list
        }
        for (int k = this.size - 1; k >= i; k--) {
            this.data[k + 1] = this.data[k]; // shifting right
        }
        this.data[i] = e; // place new element
        this.size++;
    }
    public void add(E e) throws IndexOutOfBoundsException, IllegalStateException {
        checkIndex(this.size + 1, this.size + 1); // check index after end of list elements
        if (this.size == this.data.length) {
            //throw new IllegalStateException("Array is full"); // not enough capacity
            resize(2 * this.data.length); // double the capacity
            this.currentCapacity = this.data.length; // track new capacity of list
        }
        this.data[this.size + 1] = e; // add new element after current last element in list
        this.size++;
    }

    /**
     * Removes/returns the element at index i, shifting subsequent elements earlier.
     * @param i the index of the element to be removed
     * @return E
     * @throws IndexOutOfBoundsException Thrown if trying to remove an index that's out of bounds
     */
    public E remove(int i) throws IndexOutOfBoundsException {
        checkIndex(i, this.size);
        // if number of elements in list is < currentCapacity / 4, reduce size by half
        if (this.size < (this.currentCapacity / 4)) {
            resize(this.data.length / 2); // cut the capacity in half
        }
        E temp = data[i];
        for (int k = i; k < this.size - 1; k++) {
            this.data[k] = this.data[k + 1];
        }
        this.data[this.size - 1] = null;
        this.size--;
        return temp;
    }

    /**
     * Check whether the given index is in the range [0, n - 1].
     * @param i the index to check
     * @param n the capacity of the array
     * @throws IndexOutOfBoundsException Thrown if trying to check an index that's out of bounds
     */
    protected void checkIndex(int i, int n) throws IndexOutOfBoundsException {
        if (i < 0 || i >= n) {
            throw new IndexOutOfBoundsException("Illegal index: " + i);
        }
    }
    protected void resize(int capacity) {
        E[] temp = (E[]) new Object[capacity];
        // create a copy of the data array, called temp
        for (int k = 0; k < this.size; k++) {
            temp[k] = this.data[k];
        }
        this.data = temp; // start using the new array
    }

    /**
     * Returns an iterator of the elements stored in the array list
     * @return Iterator object of type E
     */
    public Iterator<E> iterator() {
        return new ArrayIterator(); // create a new instance of the inner class
    }

    /**
     * Compare two instances of the ArrayList class.
     * @param obj An object to compare
     * @return true if both ArrayLists are equal and false otherwise
     */
    public boolean equals(Object obj) {
        // if it's the same reference to same object, they are equal
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof  ArrayList)) {
            // if the object to compare isn't of type ArrayList, can't be equal
            return false;
        }
        ArrayList<E> other = (ArrayList<E>) obj; // cast object to ArrayList
        // check if the number of elements in each ArrayList are equal with size()
        if (this.size() != other.size()) {
            return false;
        }
        // create an ArrayIterator instance for this object and other object to iterate over them
        Iterator<E> thisIterator = this.iterator();
        Iterator<E> otherIterator = other.iterator();

        // loop through each of the ArrayLists and compare each element
        // while thisIterator has a next object
        while (thisIterator.hasNext()) {
            E thisElem = thisIterator.next();
            Object otherElem = otherIterator.next();
            // compare both lists for null values
            if (thisElem == null && otherElem != null || thisElem != null && otherElem == null) {
                return false;
            }
            // compare using the element E's equals method
            else if (thisElem != null) {
                if (!thisElem.equals(otherElem)) {
                    return false;
                }
            }
        }
        return true;
    }
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (E e : this) {
            sb.append(e).append(" ");
        }
        return sb.toString();
    }
}
