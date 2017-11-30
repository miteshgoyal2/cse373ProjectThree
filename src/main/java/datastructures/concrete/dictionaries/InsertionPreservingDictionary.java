package datastructures.concrete.dictionaries;

import java.util.Iterator;
import java.util.NoSuchElementException;
import datastructures.concrete.KVPair;
import datastructures.interfaces.IDictionary;
import misc.exceptions.NoSuchKeyException;

public class InsertionPreservingDictionary<K, V> implements IDictionary<K, V> {

    // FIELDS
    private int[] indices;
    private Pair<K, V>[] contents;
    private int size;
    private int contentPointer;
    private int capacity = 11;
    private int primeVal;

    // CONSTRUCTOR
    public InsertionPreservingDictionary() {
        indices = makeArrayOfInt(capacity);
        size = 0;
        contentPointer = 0;
        contents = makeArrayOfPairs(capacity);
        primeVal = getPrimeForHash2();
    }

    private int[] makeArrayOfInt(int value) {
        int[] output = new int[size];
        for (int i = 0; i < size; i++) {
            output[i] = -2;
        }
        return output;
    }

    @SuppressWarnings("unchecked")
    private Pair<K, V>[] makeArrayOfPairs(int arraySize) {

        return (Pair<K, V>[]) (new Pair[arraySize]);
    }

    @Override
    public V get(K key) {
        if (!containsKey(key)) {
            throw new NoSuchKeyException();
        }
        int index = contentsIndexOf(key);
        return contents[index].value;
    }

    @Override
    public void put(K key, V value) {

        if (loadFactor() > 0.4) {
            rehash();
        }

        if (containsKey(key)) {
            int index = contentsIndexOf(key);
            contents[index].value = value;
        } else {
            contents[contentPointer] = new Pair<K, V>(key, value);
            contents[contentPointer].isEmpty = false;
            int index = findEmptyCell(key, hashFunction1(key));
            indices[index] = contentPointer;
            contentPointer++;
            size++;
        }
    }

    @Override
    public V remove(K key) {

        if (!containsKey(key)) {
            throw new NoSuchKeyException();
        }
        int index = contentsIndexOf(key);
        V output = contents[index].value;
        contents[index].isEmpty = true;
        indices[indicesIndexOf(key)] = -1;
        size--;
        if (size == 0) {
            makeEmpty();
        }
        return output;

    }

    @Override
    public boolean containsKey(K key) {
        if (size == 0) {
            return false;
        }
        int index = contentsIndexOf(key);
        return index >= 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<KVPair<K, V>> iterator() {
        return new InsertionPreservingIterator(this.contents, contentPointer);
    }

    private class InsertionPreservingIterator implements Iterator<KVPair<K, V>> {

        private Pair<K, V>[] contents;
        private int index;
        private int size;

        public InsertionPreservingIterator(Pair<K, V>[] contents, int size) {
            this.contents = contents;
            index = 0;
            this.size = size;
        }

        @Override
        public boolean hasNext() {
            if (size == 0) {
                return false;
            }
            return index < size;
        }

        @Override
        public KVPair<K, V> next() {
            Pair<K, V> item = contents[index];
            if (!hasNext()) {
                throw new NoSuchElementException();
            } else if (!item.isEmpty) {
                index++;
                return new KVPair<K, V>(item.key, item.value);
            } else {
                index++;
                return this.next();

            }

        }

    }

    // helper methods
    private void rehash() {
        int oldSize = size;
        size = 0;
        contentPointer = 0;
        capacity = getPrimeTableSize(2 * capacity);
        primeVal = getPrimeForHash2();
        Pair<K, V>[] oldContents = contents;
        contents = makeArrayOfPairs(capacity);
        indices = makeArrayOfInt(capacity);
        for (int i = 0; i < oldSize; i++) {
            if (!oldContents[i].isEmpty) {
                put(oldContents[i].key, oldContents[i].value);
            }
        }
    }

    private void makeEmpty() {
        size = 0;
        contentPointer = 0;
        contents = makeArrayOfPairs(capacity);
        indices = makeArrayOfInt(capacity);
    }

    private double loadFactor() {
        return (double) size / capacity;
    }

    private int hashFunction1(K key) {
        int keyCode = (key == null ? 1 : key.hashCode());
        return Math.abs(keyCode % contents.length);
    }

    private int hashFunction2(K key) {
        int keyCode = Math.abs(key == null ? 1 : key.hashCode());
        return primeVal - (keyCode % primeVal);
    }

    private int indicesIndexOf(K key) {

        int hash1 = hashFunction1(key);
        int hash2 = hashFunction2(key);

        if (indices[hash1] == -2) {
            return hash1;
        }

        if (key == null) {
            while (indices[hash1] != -2 && (indices[hash1] != -1 ? contents[indices[hash1]].key != null : true)) {
                hash1 += hash2;
                hash1 %= capacity;
            }
        } else {
            while (indices[hash1] != -2 && (indices[hash1] != -1
                    ? contents[indices[hash1]].key != null && !contents[indices[hash1]].key.equals(key)
                    : true)) {
                hash1 += hash2;
                hash1 %= capacity;
            }
        }

        return hash1;

    }

    private int contentsIndexOf(K key) {
        return indices[indicesIndexOf(key)];
    }

    public String toString() {
        String output = "Content: ";
        for (int i = 0; i < contentPointer; i++) {
            output += contents[i] + " ";
        }
        output += "\n";
        output += "Indices: ";

        for (int i = 0; i < indices.length; i++) {
            output += indices[i] + " ";
        }
        output += "\n";
        for (int i = 0; i < contentPointer; i++) {
            output += contents[i].isEmpty + " ";
        }
        return output;
    }

    private int getPrimeForHash2() {
        for (int i = capacity - 1; i >= 1; i--) {
            int k = 0;
            for (int j = 2; j <= (int) Math.sqrt(i); j++) {
                if (i % j == 0) {
                    k++;
                }
            }
            if (k == 0) {
                return i;
            }
        }
        return 1;
    }

    private int findEmptyCell(K key, int hash1) {
        int hash2 = hashFunction2(key);
        if (indices[hash1] < 0) {
            return hash1;
        } else {
            hash1 += hash2;
            hash1 %= capacity;
            return findEmptyCell(key, hash1);
        }
    }

    private int getPrimeTableSize(int n) {

        while (!isPrime(n)) {
            n++;
        }

        return n;
    }

    private boolean isPrime(int n) {
        if (n % 2 == 0) {
            return false;
        }
        for (int i = 3; i * i <= n; i += 2) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }

    private static class Pair<K, V> {
        public K key;
        public V value;
        public boolean isEmpty;

        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
            this.isEmpty = false;
        }

        @Override
        public String toString() {
            return this.key + "=" + this.value;
        }
    }
}
