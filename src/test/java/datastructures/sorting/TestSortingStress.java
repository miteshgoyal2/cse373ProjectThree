package datastructures.sorting;

import misc.BaseTest;
import misc.Searcher;

import org.junit.Test;

import datastructures.concrete.ArrayHeap;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import datastructures.interfaces.IPriorityQueue;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestSortingStress extends BaseTest {
    @Test(timeout = 10 * SECOND)
    public void testPlaceholder() {
        // TODO: replace this placeholder with actual code
        assertTrue(true);
    }

    @Test(timeout = 10 * SECOND)
    public void stressTestArrayHeap() {
        IPriorityQueue<String> heap = new ArrayHeap<String>();
        PriorityQueue<String> utilHeap = new PriorityQueue<String>();

        for (int i = 0; i < 1000000; i++) {
            heap.insert("" + i + i * 21);
            utilHeap.add("" + i + i * 21);
            assertEquals(utilHeap.size(), heap.size());
        }

        for (int i = 0; i < 1000000; i++) {
            heap.insert("" + i);
            utilHeap.add("" + i);
            assertEquals(utilHeap.peek(), heap.peekMin());
            assertEquals(utilHeap.size(), heap.size());
        }

        for (int i = 0; i < 2000000; i++) {
            assertEquals(utilHeap.remove(), heap.removeMin());
            assertEquals(utilHeap.size(), heap.size());
        }

        for (int i = 0; i < 1000000; i++) {
            heap.insert("" + i + i * 21);
            utilHeap.add("" + i + i * 21);
            assertEquals(utilHeap.peek(), heap.peekMin());
            assertEquals(utilHeap.remove(), heap.removeMin());
            assertEquals(utilHeap.size(), heap.size());
        }
    }

    @Test(timeout = 2 * SECOND)
    public void stressTestTopKSort() {

        IList<String> input = new DoubleLinkedList<String>();
        List<String> utilList = new ArrayList<String>();

        for (int i = 0; i < 10000; i++) {
            input.add("" + i);
            utilList.add("" + i);
        }
        Collections.sort(utilList);
        IList<String> output = new DoubleLinkedList<String>();
        try {
            for (int i = -1000000; i < 0; i++) {
                output = Searcher.topKSort(i, input);
                fail("Expected IllegalArgumentException");
            }
        } catch (IllegalArgumentException ex) {

        }

        output = Searcher.topKSort(5000, input);
        for (int j = 0; j < 5000; j++) {
            assertEquals(utilList.get(utilList.size() - (5000 - j)), output.get(j));
        }

        output = Searcher.topKSort(10005, input);
        for (int j = 0; j < 10000; j++) {
            assertEquals(utilList.get(j), output.get(j));
        }

        output = Searcher.topKSort(10000, input);
        for (int i = 0; i < 10000; i++) {
            assertEquals(utilList.get(i), output.get(i));
            assertEquals(utilList.size(), output.size());
        }

        for (int i = 0; i < 10000; i++) {
            input.remove();
        }

        for (int i = 0; i < 1000000; i++) {
            output = Searcher.topKSort(0, input);
            assertEquals(0, output.size());
        }

    }
}
