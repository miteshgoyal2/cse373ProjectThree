package datastructures.concrete.dictionaries;

import datastructures.interfaces.IDictionary;
import misc.exceptions.NoSuchKeyException;
import misc.exceptions.NotYetImplementedException;

import java.util.Iterator;
import java.util.NoSuchElementException;

import javax.print.attribute.Size2DSyntax;

/**
 * See the spec and IDictionary for more details on what each method should do
 */
public class ChainedHashDictionary<K, V> implements IDictionary<K, V> {
    // You may not change or rename this field: we will be inspecting
    // it using our private tests.
    private IDictionary<K, V>[] chains;

    // You're encouraged to add extra fields (and helper methods) though!
    private int sizeOfTable;
    private int numOfPairs;
    
    public ChainedHashDictionary() {
    	numOfPairs = 0;
    	sizeOfTable = 10;
        chains = makeArrayOfChains(sizeOfTable);
    }
    
    private double getLoadFactor() {
    	return (double)numOfPairs / (double)sizeOfTable;
    }
    
    public void rehash() {
    	numOfPairs = 0;
    	sizeOfTable = sizeOfTable * 2;
    	IDictionary<K, V>[] oldChains = chains;
    	chains = makeArrayOfChains(sizeOfTable);
    	for (int i = 0; i < oldChains.length; i++) {
    		if (oldChains[i] != null) {
	    		for (KVPair<K, V> pair : oldChains[i]) {
	    			put(pair.getKey(), pair.getValue());
	    		}
    		}
    	}
    	oldChains = null;
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain IDictionary<K, V> objects.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private IDictionary<K, V>[] makeArrayOfChains(int size) {
        // Note: You do not need to modify this method.
        // See ArrayDictionary's makeArrayOfPairs(...) method for
        // more background on why we need this method.
        return (IDictionary<K, V>[]) new IDictionary[size];
    }

    public int getHashIndex(K key) {
    	int hashCode;
    	if (key != null) {
    		hashCode = Math.abs(key.hashCode());
    	} else {
    		hashCode = 0;
    	}
    	return hashCode % sizeOfTable;
    }
    
    @Override
    public V get(K key) {
        IDictionary<K, V> chain = getChain(key);
        if (chain == null) {
        	throw new NoSuchKeyException();
        }
        return chain.get(key);
    }

    @Override
    public void put(K key, V value) {
    	
    	if (getLoadFactor() > 5) {
    		rehash();
    	}
    	int index;
    		index = getHashIndex(key);
    	if (chains[index] == null) {
    		chains[index] = new ArrayDictionary<K, V>();
    	}
    	int initialChainSize = chains[index].size();
    	chains[index].put(key, value);
    	
    	// increments numOfPairs if new Pair is added
    	if (chains[index].size() > initialChainSize) {
    		numOfPairs++;
    	}
    	
    }

    @Override
    public V remove(K key) {
    	IDictionary<K, V> chain = getChain(key);
    	if (chain == null) {
    		throw new NoSuchKeyException();
    	}
    	V value = chain.remove(key);
    	numOfPairs--;
    	return value;
    }

    private IDictionary<K, V> getChain(K key) {
    	int index = getHashIndex(key);
    	return chains[index];
    }
    
    @Override
    public boolean containsKey(K key) {
    	IDictionary<K, V> chain = getChain(key);
    	if (chain != null) {
    		return getChain(key).containsKey(key);
    	} else {
    		return false;
    	}
    }

    @Override
    public int size() {
        return numOfPairs;
    }

    @Override
    public Iterator<KVPair<K, V>> iterator() {
        // Note: you do not need to change this method
        return new ChainedIterator<>(this.chains);
    }

    /**
     * Hints:
     *
     * 1. You should add extra fields to keep track of your iteration
     *    state. You can add as many fields as you want. If it helps,
     *    our reference implementation uses three (including the one we
     *    gave you).
     *
     * 2. Before you try and write code, try designing an algorithm
     *    using pencil and paper and run through a few examples by hand.
     *
     * 3. Think about what exactly your *invariants* are. An *invariant*
     *    is something that must *always* be true once the constructor is
     *    done setting up the class AND must *always* be true both before and
     *    after you call any method in your class.
     *
     *    Once you've decided, write them down in a comment somewhere to
     *    help you remember.
     *
     *    You may also find it useful to write a helper method that checks
     *    your invariants and throws an exception if they're violated.
     *    You can then call this helper method at the start and end of each
     *    method if you're running into issues while debugging.
     *
     *    (Be sure to delete this method once your iterator is fully working.)
     *
     * Implementation restrictions:
     *
     * 1. You **MAY NOT** create any new data structures. Iterators
     *    are meant to be lightweight and so should not be copying
     *    the data contained in your dictionary to some other data
     *    structure.
     *
     * 2. You **MAY** call the `.iterator()` method on each IDictionary
     *    instance inside your 'chains' array, however.
     */
    private static class ChainedIterator<K, V> implements Iterator<KVPair<K, V>> {
        private IDictionary<K, V>[] chains;
        private IDictionary<K, V> currentChain;
        private Iterator<KVPair<K, V>> innerIterator;
        private int tableIndex;

        public ChainedIterator(IDictionary<K, V>[] chains) {
            this.chains = chains;
            goToNextChain(-1);
        }   

        @Override
        public boolean hasNext() {
        	if (innerIterator == null) {
        		return false;
        	}
            if (innerIterator.hasNext() || hasNextFilledChain(tableIndex))
            	return true;
            else {
            	return false;
            }
        }

        @Override
        public KVPair<K, V> next() {
        	if (innerIterator == null)
        		throw new NoSuchElementException();
            if (innerIterator.hasNext()) {
            	return innerIterator.next();
            } else {
            	if (!hasNextFilledChain(tableIndex))
            		throw new NoSuchElementException();
            	goToNextChain(tableIndex);
            	return innerIterator.next();
            }
        }
        
        
        public boolean hasNextFilledChain(int prevIndex) {
            for (int i = prevIndex + 1; i < chains.length; i++) {
            	IDictionary<K, V> chain = chains[i];
            	if (chain != null) {
            		if (chain.size() > 0) {
            			return true;
            		}
            	}
            }
            return false;
            
       }
        
        public void goToNextChain(int prevIndex) {
        	 boolean foundPair = false;
             for (int i = prevIndex + 1; i < chains.length && (!foundPair); i++) {
             	IDictionary<K, V> chain = chains[i];
             	if (chain != null) {
             		if (chain.size() > 0) {
             			currentChain = chain;
             			tableIndex = i;
             			innerIterator = chain.iterator();
             			foundPair = true;
             		}
             	}
             }
             if (!foundPair) {
            	 innerIterator = null;
             }
        }
        
    }
}
