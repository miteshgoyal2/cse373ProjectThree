package datastructures.concrete;

import datastructures.interfaces.IPriorityQueue;
import misc.exceptions.EmptyContainerException;
import misc.exceptions.NotYetImplementedException;

import java.util.Arrays;

/**
 * See IPriorityQueue for details on what each method must do.
 */
public class ArrayHeap<T extends Comparable<T>> implements IPriorityQueue<T> {
    // See spec: you must implement a implement a 4-heap.
    private static final int NUM_CHILDREN = 4;

    // You MUST use this field to store the contents of your heap.
    // You may NOT rename this field: we will be inspecting it within
    // our private tests.
    private T[] heap;

    // Feel free to add more fields and constants.
    private int numOfElements;
    private int height;
    
    public ArrayHeap() {
        heap = makeArrayOfT(5);
        numOfElements = 0;
        height = 2;
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain elements of type T.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private T[] makeArrayOfT(int size) {
        // This helper method is basically the same one we gave you
        // in ArrayDictionary and ChainedHashDictionary.
        //
        // As before, you do not need to understand how this method
        // works, and should not modify it in any way.
        return (T[]) (new Comparable[size]);
    }

    @Override
    public T removeMin() {
        T min = heap[0];
        heap[0] = heap[numOfElements - 1];
        heap[numOfElements - 1] = null;
        rectifyOrderRemove();
        numOfElements--;
        return min;
    }

    @Override
    public T peekMin() {
        return heap[0];
    }

    @Override
    public void insert(T item) {
    	if (numOfElements == heap.length) {
    		increaseArraySize();
    	}
    	int index = numOfElements;    		
        heap[index] = item;
        numOfElements++;
        rectifyOrderInsert(index);
    }

    @Override
    public int size() {
        return numOfElements;
    }
    
    private void increaseArraySize() {
    	// increases size by a row in the heap
    	int newSize = size() + (int)Math.pow(4, height);
    	T[] oldHeap = heap;
    	heap = makeArrayOfT(newSize);
    	for (int i = 0; i < oldHeap.length; i++) {
    		heap[i] = oldHeap[i];
    	}
    	height++;
    }
    
    private int getParentIndex(int index) {
    	if (index % 4 == 0) {
    		return (index - 4) / 4;
    	} else {
    		return (index - (index % 4)) / 4;
    	}
    }
    
    private void rectifyOrderRemove() {
    	
    }
    
    private void rectifyOrderInsert(int index) {
    	
    	if (index != 0) {
	    	int parentIndex = getParentIndex(index);
	    	T parent = heap[parentIndex];
	    	T item = heap[index];
	    	if (parent.compareTo(item) > 0) {
	    		heap[parentIndex] = item;
	    		heap[index] = parent;
	    		rectifyOrderInsert(parentIndex);
	    	}
    	}
    }
}
