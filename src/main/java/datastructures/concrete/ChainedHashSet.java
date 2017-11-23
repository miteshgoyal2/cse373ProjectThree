package datastructures.concrete;

import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.concrete.dictionaries.InsertionPreservingDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.ISet;
import misc.exceptions.NoSuchKeyException;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * See ISet for more details on what each method is supposed to do.
 */
public class ChainedHashSet<T> implements ISet<T> {
    // This should be the only field you need
    private IDictionary<T, Boolean> map;

    public ChainedHashSet() {
        this.map = new InsertionPreservingDictionary<>();
    }

    @Override
    public void add(T item) {
        map.put(item, true);
    }

    @Override
    public void remove(T item) {
        try {
            map.remove(item);
        } catch (NoSuchKeyException exc) {
            throw new NoSuchElementException();
        }
    }

    @Override
    public boolean contains(T item) {
        return map.containsKey(item);
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public Iterator<T> iterator() {
        return new SetIterator<>(this.map.iterator());
    }

    private static class SetIterator<T> implements Iterator<T> {
        // This should be the only field you need
        private Iterator<KVPair<T, Boolean>> iter;

        public SetIterator(Iterator<KVPair<T, Boolean>> iter) {
            this.iter = iter;
        }

        @Override
        public boolean hasNext() {
            return iter.hasNext();
        }

        @Override
        public T next() {
            return iter.next().getKey();
        }
    }

}
