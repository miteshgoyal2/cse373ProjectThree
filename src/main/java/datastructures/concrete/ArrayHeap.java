package datastructures.concrete;

import datastructures.interfaces.IPriorityQueue;
import misc.exceptions.EmptyContainerException;
import misc.exceptions.NotYetImplementedException;

import java.util.Arrays;
import java.util.NoSuchElementException;

/**
 * See IPriorityQueue for details on what each method must do.
 */
public class ArrayHeap<T extends Comparable<T>> implements IPriorityQueue<T> {
    // See spec: you must implement a implement a 3-heap.
    private static final int NUM_CHILDREN = 4;

    // You MUST use this field to store the contents of your heap.
    // You may NOT rename this field: we will be inspecting it within
    // our private tests.
    private T[] heap;

    // Feel free to add more fields and constants.
    private int size;

    public ArrayHeap() {
        heap = makeArrayOfT(1);
        size = 0;
    }

    /**
     * This method will return a new, empty array of the given size that can contain elements of type T.
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
        if (size == 0) {
            throw new NoSuchElementException();
        } else {
            T output = this.peekMin();
            heap[0] = heap[size - 1];
            // percolate down
            int k = 0;
            for (int i = 0; i < height(); i++) {
                int minChildIndex = NUM_CHILDREN * k + 1;
                if (minChildIndex < size) {
                    T minChild = heap[minChildIndex];

                    int childIndex = minChildIndex;
                    // find the minimum child
                    for (int j = 0; j < NUM_CHILDREN - 1; j++) {
                        childIndex++;
                        if ((childIndex < size) && (minChild.compareTo(heap[childIndex]) >= 0)) {
                            minChildIndex = childIndex;
                            minChild = heap[minChildIndex];
                        }
                    }
                    // swap with min child
                    if (heap[k].compareTo(minChild) >= 0) {
                        T newChild = heap[k];
                        heap[minChildIndex] = newChild;
                        heap[k] = minChild;
                        k = minChildIndex;
                    }
                }
            }
            size--;
            return output;
        }
    }

    @Override
    public T peekMin() {
        if (size == 0) {
            throw new NoSuchElementException();
        } else {
            return heap[0];
        }
    }

    @Override
    public void insert(T item) {
        if (item == null) {
            throw new NullPointerException();
        }
        // resize
        resize(size + 1);

        // insert
        heap[size] = item;

        // percolate up
        int k = size;
        for (int i = height(); i >= 0; i--) {
            T newlyInsertedChild = heap[k];
            int parentIndex = (k - 1) / NUM_CHILDREN;
            T parent = heap[parentIndex];
            if (parent.compareTo(newlyInsertedChild) >= 0) {
                heap[parentIndex] = newlyInsertedChild;
                heap[k] = parent;
            }
            k = parentIndex;
        }

        size++;
    }

    @Override
    public int size() {
        return size;
    }

    // helper methods
    public int height() {
        return (int) Math.ceil(((Math.log(size * NUM_CHILDREN - size + 1) / Math.log(NUM_CHILDREN)) - 1));
    }

    private void resize(int capacity) {
        if (capacity + 1 > heap.length) {
            int newCapacity = capacity * 2;
            T[] newHeap = makeArrayOfT(newCapacity);
            for (int i = 0; i < capacity; i++) {
                newHeap[i] = heap[i];
            }
            heap = newHeap;
        }
    }

    public String toString() {
        String result = "Heap: ";
        for (int i = 0; i < size; i++) {
            result += heap[i] + " ";
        }
        return result;
    }
}
