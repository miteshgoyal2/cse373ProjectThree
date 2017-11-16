// Hoang Le + Mitesh Goyal
package datastructures.sorting;

import misc.BaseTest;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import misc.Searcher;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestTopKSortFunctionality extends BaseTest {
    @Test(timeout = SECOND)
    public void testSimpleUsage() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }

        IList<Integer> top = Searcher.topKSort(5, list);
        assertEquals(5, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(15 + i, top.get(i));
        }
    }

    @Test(timeout = SECOND)
    public void testNegativeKErrorHandling() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }
        IList<Integer> top = new DoubleLinkedList<>();
        try {
            for (int i = -1; i > -10; i--) {
                top = Searcher.topKSort(i, list);
                fail("Expect IllegalArgumentException");
            }
        } catch (IllegalArgumentException ex) {

        }
    }

    @Test(timeout = SECOND)
    public void testZeroK() {

        IList<String> input = new DoubleLinkedList<String>();
        for (int i = 0; i < 150; i++) {
            input.add(i + " ");
        }
        IList<String> output = Searcher.topKSort(0, input);
        assertEquals(0, output.size());
    }

    @Test(timeout = SECOND)
    public void testKSort() {
        IList<String> input = new DoubleLinkedList<String>();
        List<String> utilList = new ArrayList<String>();
        for (int i = 0; i < 5000; i++) {
            input.add("" + i * Math.pow(i, i * i));
            utilList.add("" + i * Math.pow(i, i * i));
        }
        Collections.sort(utilList);
        IList<String> output = Searcher.topKSort(2500, input);
        assertEquals(2500, output.size());
        for (int i = 0; i < 2500; i++) {
            assertEquals(utilList.get(utilList.size() - (2500 - i)), output.get(i));
        }

    }

    @Test(timeout = SECOND)
    public void testOriginalList() {
        IList<String> input = new DoubleLinkedList<String>();
        List<String> utilList = new ArrayList<String>();
        for (int i = 0; i < 100; i++) {
            input.add("" + i * -98);
            utilList.add("" + i * -98);
        }
        int originalSize = input.size();
        IList<String> output = Searcher.topKSort(10, input);
        Collections.sort(utilList);

        for (int i = 0; i < 10; i++) {
            assertEquals(utilList.get(utilList.size() - (10 - i)), output.get(i));
        }

        assertEquals(originalSize, input.size());
        for (int i = 0; i < 100; i++) {
            assertEquals("" + i * -98, input.get(i));
        }

    }

    @Test(timeout = SECOND)
    public void testLegalKOnEmptyList() {
        IList<String> input = new DoubleLinkedList<String>();

        for (int i = 0; i < 500; i++) {
            IList<String> output1 = Searcher.topKSort(i, input);
            assertEquals(0, output1.size());
        }
    }

    @Test(timeout = SECOND)
    public void testKLargerThanN() {
        IList<String> input = new DoubleLinkedList<String>();
        List<String> utilList = new ArrayList<String>();

        for (int i = 0; i < 20; i++) {
            input.add("ahsh" + i + "ko" + i * 2);
            utilList.add("ahsh" + i + "ko" + i * 2);
        }
        Collections.sort(utilList);
        IList<String> output;
        for (int k = 21; k < 100; k++) {
            output = Searcher.topKSort(k, input);
            for (int j = 0; j < 20; j++) {
                assertEquals(utilList.get(j), output.get(j));
                assertEquals(20, output.size());
            }
        }
    }

    @Test(timeout = SECOND)
    public void testOnDuplicateList() {
        IList<String> input = new DoubleLinkedList<String>();
        List<String> utilList = new ArrayList<String>();

        for (int i = 0; i < 20; i++) {
            input.add("CSE373");
            utilList.add("CSE373");
        }
        Collections.sort(utilList);
        IList<String> output = Searcher.topKSort(17, input);
        for (int i = 0; i < 17; i++) {
            assertEquals(utilList.get(utilList.size() - (20 - i)), output.get(i));
        }
    }

    @Test(timeout = SECOND)
    public void testOnSortedList() {
        IList<Integer> input = new DoubleLinkedList<Integer>();
        List<Integer> utilList = new ArrayList<Integer>();

        for (int i = 0; i < 200; i++) {
            input.add(i);
            utilList.add(i);
        }
        Collections.sort(utilList);

        IList<Integer> output = Searcher.topKSort(20, input);
        for (int i = 0; i < 20; i++) {
            assertEquals(utilList.get(utilList.size() - (20 - i)), output.get(i));
        }

    }

    @Test(timeout = SECOND)
    public void testOnUnsortedList() {
        IList<String> input = new DoubleLinkedList<String>();
        List<String> utilList = new ArrayList<String>();

        for (int i = 0; i < 20; i++) {
            input.add("ahsh" + i + "ko" + i * 2);
            utilList.add("ahsh" + i + "ko" + i * 2);
        }
        Collections.sort(utilList);
        IList<String> output = Searcher.topKSort(20, input);
        for (int i = 0; i < 20; i++) {
            assertEquals(utilList.get(utilList.size() - (20 - i)), output.get(i));
        }

    }

    @Test(timeout = SECOND)
    public void testOnNullList() {
        IList<String> list = null;
        try {
            IList<String> top = Searcher.topKSort(5, list);
            fail("Expect IllegalArgumentException");
        } catch (IllegalArgumentException ex) {

        }
    }

    @Test(timeout = SECOND)
    public void testKEqualN() {
        IList<String> input = new DoubleLinkedList<String>();
        List<String> utilList = new ArrayList<String>();

        for (int i = 0; i < 25; i++) {
            input.add("ahsh" + i + "ko" + i * 2);
            utilList.add("ahsh" + i + "ko" + i * 2);
        }
        Collections.sort(utilList);
        IList<String> output = Searcher.topKSort(utilList.size(), input);
        for (int i = 0; i < utilList.size(); i++) {
            assertEquals(utilList.get(i), output.get(i));
            assertEquals(utilList.size(), output.size());
        }
    }

}
