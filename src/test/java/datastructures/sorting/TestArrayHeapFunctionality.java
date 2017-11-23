// Hoang Le + Mitesh Goyal
package datastructures.sorting;

import static org.junit.Assert.fail;

import java.util.PriorityQueue;

import misc.BaseTest;
import datastructures.concrete.ArrayHeap;
import datastructures.interfaces.IPriorityQueue;
import misc.exceptions.EmptyContainerException;
import org.junit.Test;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestArrayHeapFunctionality extends BaseTest {
    protected <T extends Comparable<T>> IPriorityQueue<T> makeInstance() {
        return new ArrayHeap<>();
    }

    @Test(timeout = SECOND)
    public void testBasicSize() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.insert(3);
        assertEquals(1, heap.size());
    }

    @Test(timeout = SECOND)
    public void testUpdateSize() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        assertEquals(0, heap.size());
        for (int i = 0; i < 100; i++) {
            heap.insert(i);
        }
        assertEquals(100, heap.size());
        for (int i = 0; i < 50; i++) {
            heap.removeMin();
        }
        assertEquals(50, heap.size());

        for (int i = 0; i < 25; i++) {
            heap.insert(i);
            heap.removeMin();
        }
        assertEquals(50, heap.size());

        for (int i = 0; i < 50; i++) {
            heap.peekMin();
        }
        assertEquals(50, heap.size());
    }

    @Test(timeout = SECOND)
    public void testInsertBasic() {
        IPriorityQueue<String> heap = this.makeInstance();
        PriorityQueue<String> utilHeap = new PriorityQueue<String>();
        for (int i = 0; i < 15; i++) {
            heap.insert("" + i * i * 50);
            utilHeap.add("" + i * i * 50);
            assertEquals(utilHeap.size(), heap.size());
            assertEquals(utilHeap.remove(), heap.removeMin());
        }
    }

    @Test(timeout = SECOND)
    public void testInsertNullErrorHandling() {
        IPriorityQueue<String> heap = this.makeInstance();
        heap.insert("HELLO");
        try {
            for (int i = 0; i < 10000; i++) {
                heap.insert(null);
                fail("Expected IllegalArgumentException");
            }
        } catch (IllegalArgumentException ex) {
            //System.out.println("Caught exception: " + ex.getMessage());
        }
    }

    @Test(timeout = SECOND)
    public void testAlternatingInsertAndRemove() {
        IPriorityQueue<String> heap = this.makeInstance();
        PriorityQueue<String> utilHeap = new PriorityQueue<String>();
        for (int i = 0; i < 150; i++) {
            heap.insert("" + i + "GOOO" + Math.pow(i, i));
            utilHeap.add("" + i + "GOOO" + Math.pow(i, i));
            assertEquals(utilHeap.remove(), heap.removeMin());
        }
    }

    @Test(timeout = SECOND)
    public void testInsertMany() {
        IPriorityQueue<String> heap = this.makeInstance();
        for (int i = 0; i < 15000; i++) {
            heap.insert("" + i);
            assertEquals(i + 1, heap.size());
        }
    }

    @Test(timeout = SECOND)
    public void testInsertMin() {
        IPriorityQueue<String> heap = this.makeInstance();
        PriorityQueue<String> utilHeap = new PriorityQueue<String>();
        for (int i = 15000; i > 0; i--) {
            heap.insert("" + "wow" + Math.log(i));
            utilHeap.add("" + "wow" + Math.log(i));
            assertEquals(utilHeap.size(), heap.size());
        }
        for (int i = 0; i < 15000; i++) {
            assertEquals(utilHeap.remove(), heap.removeMin());
        }
    }

    @Test(timeout = SECOND)
    public void testInsertMax() {
        IPriorityQueue<String> heap = this.makeInstance();
        PriorityQueue<String> utilHeap = new PriorityQueue<String>();
        for (int i = 0; i < 15000; i++) {
            heap.insert("" + i + "hello" + i * 90);
            utilHeap.add("" + i + "hello" + i * 90);
            assertEquals(utilHeap.size(), heap.size());

        }
        for (int i = 0; i < 15000; i++) {
            assertEquals(utilHeap.remove(), heap.removeMin());
        }
    }

    @Test(timeout = SECOND)
    public void testInsertAndRemoveMany() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        PriorityQueue<Integer> utilHeap = new PriorityQueue<Integer>();

        for (int i = 0; i < 15000; i++) {
            heap.insert(i + (int) Math.PI);
            utilHeap.add(i + (int) Math.PI);
        }
        assertEquals(heap.size(), utilHeap.size());
        for (int i = 0; i < 15000; i++) {
            assertEquals(utilHeap.remove(), heap.removeMin());
            assertEquals(utilHeap.size(), heap.size());
        }
    }

    @Test(timeout = SECOND)
    public void testPeekMinBasic() {
        IPriorityQueue<String> heap = this.makeInstance();
        PriorityQueue<String> utilHeap = new PriorityQueue<String>();
        heap.insert("CSE373");
        heap.insert("UW");
        utilHeap.add("CSE373");
        utilHeap.add("UW");

        assertEquals(utilHeap.peek(), heap.peekMin());
        utilHeap.remove();
        heap.removeMin();
        assertEquals(utilHeap.peek(), heap.peekMin());
    }

    @Test(timeout = SECOND)
    public void testPeekMinOverEmptyHeap() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.insert(3);
        heap.removeMin();
        try {
            for (int i = 0; i < 10000; i++) {
                heap.peekMin();
                fail("Expected EmptyContainerException");
            }
        } catch (EmptyContainerException ex) {
            //System.out.println("Caught exception: " + ex.getMessage());
        }
    }

    @Test(timeout = SECOND)
    public void testPeekMinAndRemoveMany() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        PriorityQueue<Integer> utilHeap = new PriorityQueue<Integer>();
        for (int i = 0; i < 150000; i++) {
            heap.insert(i + i * -9234);
            utilHeap.add(i + i * -9234);
        }

        for (int i = 0; i < 150000; i++) {
            assertEquals(utilHeap.peek(), heap.peekMin());
            assertEquals(utilHeap.remove(), heap.removeMin());
        }
        assertEquals(utilHeap.size(), heap.size());
    }

    @Test(timeout = SECOND)
    public void testRemoveMinMany() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        PriorityQueue<Integer> utilHeap = new PriorityQueue<Integer>();
        for (int i = 0; i < 150000; i++) {
            heap.insert(i - i * 987);
            utilHeap.add(i - i * 987);
            assertEquals(utilHeap.remove(), heap.removeMin());
        }

    }

    @Test(timeout = SECOND)
    public void testRemoveMinBasic() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        PriorityQueue<Integer> utilHeap = new PriorityQueue<Integer>();
        for (int i = 0; i < 10; i++) {
            heap.insert(i);
            utilHeap.add(i);
            assertEquals(utilHeap.remove(), heap.removeMin());
        }
    }

    @Test(timeout = SECOND)
    public void testRemoveMinOverEmptyHeap() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.insert(3);
        heap.removeMin();
        try {
            for (int i = 0; i < 10000; i++) {
                heap.removeMin();
                fail("Expected EmptyContainerException");
            }
        } catch (EmptyContainerException ex) {
            //System.out.println("Caught exception: " + ex.getMessage());
        }
    }

    @Test(timeout = SECOND)
    public void testInsertAndRemoveDuplicates() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        PriorityQueue<Integer> utilHeap = new PriorityQueue<Integer>();
        for (int i = 0; i < 500; i++) {
            heap.insert(1);
            utilHeap.add(1);
            heap.insert(3);
            utilHeap.add(3);
        }
        for (int i = 0; i < 500; i++) {
            assertEquals(utilHeap.peek(), heap.peekMin());
            assertEquals(utilHeap.remove(), heap.removeMin());
            assertEquals(utilHeap.size(), heap.size());
        }

        IPriorityQueue<String> heap2 = this.makeInstance();
        PriorityQueue<String> utilHeap2 = new PriorityQueue<String>();
        for (int i = 0; i < 1000; i++) {
            heap2.insert("hello");
            heap2.insert("bye");
            utilHeap2.add("hello");
            utilHeap2.add("bye");

        }
        for (int i = 0; i < 1000; i++) {
            assertEquals(utilHeap2.peek(), heap2.peekMin());
            assertEquals(utilHeap2.remove(), heap2.removeMin());
            assertEquals(utilHeap2.size(), heap2.size());
        }
    }

    @Test(timeout = SECOND)
    public void testHeapProperty() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = 0; i < 15000; i++) {
            heap.insert(i);
        }

        for (int i = 0; i < 15000; i++) {
            assertEquals(i, heap.removeMin());
        }
    }

    @Test(timeout = SECOND)
    public void testAlternatingPeekAndInsert() {
        IPriorityQueue<String> heap2 = this.makeInstance();
        PriorityQueue<String> utilHeap2 = new PriorityQueue<String>();
        for (int i = 15000; i > 0; i--) {
            heap2.insert("" + i + i * 12);
            utilHeap2.add("" + i + i * 12);
            assertEquals(utilHeap2.peek(), heap2.peekMin());
            assertEquals(utilHeap2.remove(), heap2.removeMin());
        }
    }

}
