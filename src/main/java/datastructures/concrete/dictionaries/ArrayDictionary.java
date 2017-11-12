package datastructures.concrete.dictionaries;

import java.util.Iterator;
import java.util.NoSuchElementException;

import javax.swing.JOptionPane;

import org.omg.CORBA.Current;

import datastructures.concrete.KVPair;
import datastructures.interfaces.IDictionary;
import misc.exceptions.NoSuchKeyException;
import misc.exceptions.NotYetImplementedException;

/**
 * See IDictionary for more details on what this class should do
 */
public class ArrayDictionary<K, V> implements IDictionary<K, V> {
    // You may not change or rename this field: we will be inspecting
    // it using our private tests.
    private Pair<K, V>[] pairs;

    // You're encouraged to add extra fields (and helper methods) though!
    private int numOfPairs;

    public ArrayDictionary() {
        numOfPairs = 0;
        pairs = makeArrayOfPairs(4);
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

        for (int i = 0; i < numOfPairs; i++) {
            if ((key == null && pairs[i].key == null) || pairs[i].key.equals(key)) {
                return pairs[i].value;
            }
        }
        throw new NoSuchKeyException();
    }

    @Override
    public void put(K key, V value) {
        boolean added = false;
        for (int i = 0; i < pairs.length && (!added); i++) {
            // when index has a pair
            if (i < numOfPairs) {
                if ((key == null && pairs[i].key == null) || (pairs[i].key != null && pairs[i].key.equals(key))) {
                    pairs[i].value = value;
                    added = true;
                }
            } else if (numOfPairs < pairs.length) {
                // when index is null
                pairs[i] = new Pair<K, V>(key, value);
                numOfPairs++;
                added = true;
            }
        }

        // if dictionary is full, increase size, then put the Pair.
        if (!added) {
            int originalLength = pairs.length;
            doubleTheSize();
            pairs[originalLength] = new Pair<K, V>(key, value);
            numOfPairs++;
        }
    }

    public void doubleTheSize() {
        Pair<K, V>[] biggerArray = makeArrayOfPairs(pairs.length * 2);
        for (int i = 0; i < pairs.length; i++) {
            biggerArray[i] = pairs[i];
        }
        pairs = biggerArray;
    }

    @Override
    public V remove(K key) {
        for (int i = 0; i < numOfPairs; i++) {
            if ((key == null && pairs[i].key == null) || pairs[i].key.equals(key)) {
                V value = pairs[i].value;
                deleteAndShift(i);
                return value;
            }
        }
        throw new NoSuchKeyException();
    }

    public void deleteAndShift(int index) {
        // shifting the elements
        for (int i = index; i < numOfPairs - 1; i++) {
            pairs[i] = pairs[i + 1];
        }
        pairs[numOfPairs - 1] = null;
        numOfPairs--;
    }

    @Override
    public boolean containsKey(K key) {
        for (int i = 0; i < numOfPairs; i++) {
            if ((key == null && pairs[i].key == null) || pairs[i].key.equals(key))
                return true;
        }
        return false;
    }

    @Override
    public int size() {
        return numOfPairs;
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

    public Iterator<KVPair<K, V>> iterator() {
        return new ArrayDictionaryIterator(pairs);
    }

    private class ArrayDictionaryIterator implements Iterator<KVPair<K, V>> {

        private Pair<K, V> current;
        private int index;

        public ArrayDictionaryIterator(Pair[] pairs) {
            index = 0; // start from the first element.
            current = pairs[index];

        }

        public KVPair<K, V> next() {
            if (!hasNext())
                throw new NoSuchElementException();
            K key = current.key;
            V value = current.value;
            if (pairs.length > index + 1) {
                current = pairs[++index];
            } else {
                current = null;
            }
            return new KVPair<K, V>(key, value);

        }

        public boolean hasNext() {
            if (current == null || (current.key == null && current.value == null)) {
                return false;
            }
            return true;
        }

        public K getKey() {
            return current.key;
        }

        public V getValue() {
            return current.value;
        }
    }
}
