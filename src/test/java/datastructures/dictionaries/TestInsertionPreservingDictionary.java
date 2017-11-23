package datastructures.dictionaries;

import static org.junit.Assert.assertFalse;

import org.junit.Test;

import datastructures.concrete.KVPair;
import datastructures.concrete.dictionaries.InsertionPreservingDictionary;
import datastructures.interfaces.IDictionary;

public class TestInsertionPreservingDictionary extends TestChainedHashDictionary {

    protected <K, V> IDictionary<K, V> newDictionary() {
        return new InsertionPreservingDictionary<>();
    }

    @Test(timeout = SECOND)
    public void testInsertionPreserving() {
        IDictionary<Integer, String> dict = this.newDictionary();

        for (int i = 0; i < 500; i++) {
            dict.put(i, "" + i);
        }

        int i = 0;
        for (KVPair<Integer, String> pair : dict) {
            assertEquals(i, pair.getKey());
            i++;
        }
    }

    @Test(timeout = SECOND)
    public void testInsertionPreservingAfterRemove() {
        IDictionary<Integer, String> dict = this.newDictionary();

        for (int i = 0; i < 500; i++) {
            dict.put(i, "" + i);
        }

        for (int i = 0; i < 500; i += 2) {
            dict.remove(i);
        }

        int i = 1;
        for (KVPair<Integer, String> pair : dict) {
            assertEquals(i, pair.getKey());
            i += 2;
        }
    }

    @Test(timeout = SECOND)
    public void testInsertionPreservingAfterRemoveAndInsert() {
        IDictionary<Integer, String> dict = this.newDictionary();

        for (int i = 0; i < 500; i++) {
            dict.put(i, "" + i);
        }

        for (int i = 1; i < 500; i += 2) {
            dict.remove(i);
        }

        for (int i = 1; i < 500; i += 2) {
            dict.put(i, "" + i);
        }
        int i = 0;
        for (KVPair<Integer, String> pair : dict) {
            assertEquals(i, pair.getKey());
            i += 2;
            if (i == 500) {
                i = 1;
            }
        }

    }

    @Test(timeout = 10 * SECOND)
    public void stressTest() {
        IDictionary<Integer, Integer> dict = this.newDictionary();
        for (int i = 0; i < 1000000; i++) {
            assertEquals(i, dict.size());
            dict.put(i, i);
            assertEquals(i, dict.get(i));
        }

        for (int i = 0; i < 1000000; i++) {
            assertFalse(dict.containsKey(-1));
        }

        for (int i = 0; i < 1000000; i++) {
            dict.put(i, -i);
        }

        for (int i = 0; i < 1000000; i++) {
            assertEquals(-i, dict.get(i));
            dict.remove(i);
        }
    }

    @Test(timeout = 10 * SECOND)
    public void stressTest2() {
        IDictionary<Integer, Integer> dict = this.newDictionary();
        for (int i = 0; i < 5000000; i++) {
            assertEquals(i, dict.size());
            dict.put(i, i);
            assertEquals(i, dict.get(i));
        }

        int i = 0;
        for (KVPair<Integer, Integer> pair : dict) {
            assertEquals(i, pair.getKey());
            assertEquals(i, dict.remove(i));
            i++;
        }

    }

    @Test(timeout = SECOND)
    public void testInsertionPreservingAfterRemoveAndInsert2() {
        IDictionary<Integer, String> dict = this.newDictionary();

        for (int i = 0; i < 500; i++) {
            dict.put(i, "" + i);
        }

        for (int i = 0; i < 500; i++) {
            dict.remove(i);
        }

        for (int i = 0; i < 1000; i++) {
            dict.put(i, "" + i);
        }

        int i = 0;
        for (KVPair<Integer, String> pair : dict) {
            assertEquals(i, pair.getKey());
            i++;
        }

    }
}
