// Hoang Le CSE373
package datastructures.concrete.dictionaries;

import java.util.Iterator;
import java.util.NoSuchElementException;

import datastructures.concrete.KVPair;
import datastructures.interfaces.IDictionary;
import misc.exceptions.NoSuchKeyException;

/**
 * See IDictionary for more details on what this class should do
 */
public class ArrayDictionary<K, V> implements IDictionary<K, V> {
    // You may not change or rename this field: we will be inspecting
    // it using our private tests.
    private Pair<K, V>[] pairs;

    // You're encouraged to add extra fields (and helper methods) though!
    private int size;
    private static final int CAPACITY = 1;

    // constructor
    public ArrayDictionary() {
        this(CAPACITY);
    }

    // default constructor
    // set size == 0;
    public ArrayDictionary(int capacity) {
        pairs = makeArrayOfPairs(capacity);
        size = 0;
    }

    /**
     * This method will return a new, empty array of the given size that can contain Pair<K, V> objects.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private Pair<K, V>[] makeArrayOfPairs(int arraySize) {
        // It turns out that creating arrays of generic objects in Java
        // is complicated due to something known as 'type erasure'.
        //
        // We've given you this helper method to help simplify this part of
        // your assignment. Use this helper method as appropriate when
        // implementing the rest of this class.
        //
        // You are not required to understand how this method works, what
        // type erasure is, or how arrays and generics interact. Do not
        // modify this method in any way.
        return (Pair<K, V>[]) (new Pair[arraySize]);

    }

    @Override

    public V get(K key) {
        V result = null;
        // check if key is in the dictionary
        if (!this.containsKey(key)) {
            // throw exception if not
            throw new NoSuchKeyException();
        }

        // return the value
        result = pairs[indexOf(key)].value;
        return result;
    }

    @Override
    public void put(K key, V value) {
        // resize the array when there is no space left
        remakeIfNeeded(size + 1);
        if (this.containsKey(key)) {
            // replace value if key exists
            pairs[this.indexOf(key)].value = value;
        } else {
            // add key and value
            pairs[size] = new Pair<K, V>(key, value);
            // increment size
            size++;
        }
    }

    @Override
    public V remove(K key) {
        V result = null;
        int indexAtRemoval = 0;
        if (!this.containsKey(key)) {
            // if no key in dictionary throw exception
            throw new NoSuchKeyException();
        } else {
            // get index before perform remove();
            indexAtRemoval = indexOf(key);
            result = pairs[indexAtRemoval].value;
            // loop through dictionary and shift left
            for (int i = indexAtRemoval; i < size - 1; i++) {
                pairs[i] = pairs[i + 1];
            }
        }
        // decrement size
        size--;
        return result;
    }

    @Override
    public boolean containsKey(K key) {
        // indexOf(key) == 1 means key doesn't exist return false;
        // else return true
        return indexOf(key) != -1;
    }

    @Override
    public int size() {
        return size;
    }

    // helper methods
    public int indexOf(K key) {
        for (int i = 0; i < size; i++) {
            // check null key to use "==" instead of equals()
            if (isNull(key)) {
                if (pairs[i].key == null) {
                    return i;
                }
            } else {
                // if not null key use equals() to compare
                if (pairs[i].key != null && pairs[i].key.equals(key)) {
                    return i;
                }
            }
        }
        // return -1 if key is not in dictionary
        return -1;
    }

    // check null key
    private boolean isNull(K key) {
        return key == null;
    }

    // resize dictionary
    public void remakeIfNeeded(int capacity) {
        // if capacity > current length, then resize
        if (capacity > pairs.length) {
            // new size
            int newCapacity = pairs.length * 2 + 1;

            // new array
            Pair<K, V>[] newPairs = makeArrayOfPairs(newCapacity);
            // copy keys and values from old array to new one
            for (int i = 0; i < pairs.length; i++) {
                newPairs[i] = pairs[i];
            }
            // assign new array to pairs
            pairs = newPairs;
        }
    }

    // toString() used for debugging
    public String toString() {
        String string = "";
        for (int i = 0; i < size; i++) {
            string += pairs[i] + " ";
        }
        return string;
    }

    private static class Pair<K, V> {
        public K key;
        public V value;

        // You may add constructors and methods to this class as necessary.
        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return this.key + "=" + this.value;
        }
    }

    private static class ArrayDictionaryIterator<K, V> implements Iterator<KVPair<K, V>> {

        private KVPair<K, V>[] pairs;
        int index = -1;
        int size = 0;

        @SuppressWarnings("unchecked")
        public ArrayDictionaryIterator(Pair<K, V>[] pairs2, int size) {
            this.size = size;
            pairs = (KVPair<K, V>[]) (new KVPair[pairs2.length]);
            for (int i = 0; i < size; i++) {
                K key = pairs2[i].key;
                V value = pairs2[i].value;
                pairs[i] = new KVPair<K, V>(key, value);
            }
        }

        public boolean hasNext() {
            if (size == 0) {
                return false;
            }
            return index + 1 < size;

        }

        public KVPair<K, V> next() {

            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            index++;
            return pairs[index];
        }

    }

    public Iterator<KVPair<K, V>> iterator() {

        return new ArrayDictionaryIterator<>(this.pairs, size);
    }
}
