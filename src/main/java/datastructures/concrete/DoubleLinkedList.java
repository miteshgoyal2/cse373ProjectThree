package datastructures.concrete;

import datastructures.interfaces.IList;
import misc.exceptions.EmptyContainerException;
import misc.exceptions.NotYetImplementedException;

import java.util.Iterator;
import java.util.NoSuchElementException;


/**
 * Note: For more info on the expected behavior of your methods, see
 * the source code for IList.
 */
public class DoubleLinkedList<T> implements IList<T> {
    // You may not rename these fields or change their types.
    // We will be inspecting these in our private tests.
    // You also may not add any additional fields.
    private Node<T> front;
    private Node<T> back;
    private int size;

    public DoubleLinkedList() {
        this.front = null;
        this.back = null;
        this.size = 0;
    }

    @Override
    public void add(T item) {
    	// empty case
        if (size == 0) {
        	front = new Node<T>(item);
        	back = front;
        } else {  // when list has elements
        	back.next = new Node<T>(back, item, null);
        	back = back.next;
        }
        
        size++;
    }

    @Override
    public T remove() {
        if (size == 0)
        	throw new EmptyContainerException();
        
        T elementRemoved = back.data;
        if (size == 1) {
        	front = null;
        	back = null;
        } else {
        	back = back.prev;
        	back.next = null;
        }
        size--;
        
        return elementRemoved;
        
        
    }

    @Override
    public T get(int index) {
    	
    	if (index >= size || index < 0)
    		throw new IndexOutOfBoundsException();
    	
    	Node<T> temp = front;
        for (int i = 0; i < index; i++) {
        	temp = temp.next;
        }
        return temp.data;
    }

    @Override
    public void set(int index, T item) {
    	if (index >= size || index < 0)
    		throw new IndexOutOfBoundsException();
    	
    	delete(index);
    	insert(index, item);
        
    }

    @Override
    public void insert(int index, T item) {
    	if (index > size || index < 0)
    		throw new IndexOutOfBoundsException();
    	
    	if (index == size) {
    		// adding case
    		add(item);
    	} else {
	    	if (index == 0) {
	            // front case
	        	Node<T> insertion = new Node<T>(item);
	        	insertion.next = front;
	       		front.prev = insertion;
	        	front = insertion;
	        } else {
	        	// middle case
	        	Node<T> temp;
	        	if (index < size / 2) {
		        	// index closer to front
		        	temp = front;
			        for (int i = 0; i < index; i++) {
			        	temp = temp.next;
			        }
	        	} else {
		        	// index closer to back
			        temp = back;
			        for (int i = 0; i < (size - 1) - index; i++) {
			        	temp = temp.prev;
			        }
	        	}
	
	            Node<T> nodeBeforeInsertion = temp.prev;
	            Node<T> nodeAfterInsertion = temp;
	            Node<T> insertion = new Node<T>(nodeBeforeInsertion, item, nodeAfterInsertion);
	            nodeBeforeInsertion.next = insertion;
	            nodeAfterInsertion.prev = insertion;
	        }
	    	size++;
    	}
    }

    @Override
    public T delete(int index) {
    	if (index >= size || index < 0)
    		throw new IndexOutOfBoundsException();
    	T item;
    	if (index == 0) {
            // front case
    		item = front.data;
        	front = front.next;
        	if (front != null)
        		front.prev = null;
        } else if (index == size - 1) {
        	// end case
        	item = back.data;
        	back = back.prev;
        	back.next = null;
        } else {
        	// middle case
	    	Node<T> temp = front;
	        for (int i = 0; i < index; i++) {
	        	temp = temp.next;
	        }
	        item = temp.data;
            Node<T> nodeBefore = temp.prev;
            Node<T> nodeAfter = temp.next;
            nodeBefore.next = nodeAfter;
            nodeAfter.prev = nodeBefore;
            temp = null;
        }
    	size--;
    	return item;
    }

    @Override
    public int indexOf(T item) {
        int index = -1;
       
        Node<T> temp = front;
        for (int i = 0; i < size; i++) {
	        	if (temp.data == null ? temp.data == item : temp.data.equals(item)) {
	        		index = i;
	        		return index;
	        	}
        	if (i != size - 1) // check for last element
        		temp = temp.next;
        }
        return index;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean contains(T other) {
        Node<T> temp = front;
        for (int i = 0; i < size; i++) {
        	if (temp.data == null ? temp.data == other : temp.data.equals(other)) {
        		return true;
        	}
        	if (i != size - 1) // check for last element
        		temp = temp.next;
        }
    	return false;
    }

    @Override
    public Iterator<T> iterator() {
        // Note: we have provided a part of the implementation of
        // an iterator for you. You should complete the methods stubs
        // in the DoubleLinkedListIterator inner class at the bottom
        // of this file. You do not need to change this method.
        return new DoubleLinkedListIterator<>(this.front);
    }

    private static class Node<E> {
        // You may not change the fields in this node or add any new fields.
        public final E data;
        public Node<E> prev;
        public Node<E> next;

        public Node(Node<E> prev, E data, Node<E> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }

        public Node(E data) {
            this(null, data, null);
        }

        // Feel free to add additional constructors or methods to this class.
    }

    private static class DoubleLinkedListIterator<T> implements Iterator<T> {
        // You should not need to change this field, or add any new fields.
        private Node<T> current;

        public DoubleLinkedListIterator(Node<T> current) {
            // You do not need to make any changes to this constructor.
            this.current = current;
        }

        /**
         * Returns 'true' if the iterator still has elements to look at;
         * returns 'false' otherwise.
         */
        public boolean hasNext() {
        	return !(current == null);
        }

        /**
         * Returns the next item in the iteration and internally updates the
         * iterator to advance one element forward.
         *
         * @throws NoSuchElementException if we have reached the end of the iteration and
         *         there are no more elements to look at.
         */
        public T next() {
        	if (!hasNext())
        		throw new NoSuchElementException();
            T data = current.data;
        	current = current.next;
            return data;
        }
        
        public void remove() {
        	Node<T> nodeBefore = current.prev;
        	Node<T> nodeAfter = current.next;
        	current = current.next;
        	nodeBefore.next = nodeAfter;
        	nodeAfter.prev = nodeBefore;
        	
        }
    }
}
