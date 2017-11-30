package datastructures.dictionaries;

import org.junit.Test;

import datastructures.concrete.KVPair;
import datastructures.concrete.dictionaries.AvlTreeDictionary;
import datastructures.interfaces.IDictionary;

public class TestAvlTreeDictionary extends TestSortedDictionary {

    @Override
    protected <K extends Comparable<K>, V> IDictionary<K, V> newDictionary() {
        return new AvlTreeDictionary<>();
    }

    @Test(timeout = 5 * SECOND)
    public void stressTest() {

        int limit = 1000000;
        IDictionary<String, String> dict = this.newDictionary();
        for (int i = 0; i < limit; i++) {
            assertEquals(i, dict.size());
            dict.put(i + "", i + "");
            assertEquals(i + "", dict.get("" + i));
        }

        for (int i = 0; i < limit; i++) {
            assertEquals(limit - i, dict.size());
            assertEquals(dict.remove(i + ""), i + "");
        }

        for (int i = 0; i < limit; i++) {
            dict.put(0 + "", "hello");
            assertEquals(1, dict.size());
        }
    }

    @Test(timeout = 5 * SECOND)
    public void stressTest2() {
        IDictionary<String, String> dict = this.newDictionary();
        for (int i = 0; i < 5000000; i++) {
            dict.put(i + "", i + "");
            assertEquals(i + "", dict.remove(i + ""));
        }
    }

    @Test(timeout = SECOND)
    public void testAscendingOrder() {
        IDictionary<Integer, String> dict = this.newDictionary();
        IDictionary<Integer, String> dict2 = this.newDictionary();
        for (int i = 0; i < 100; i++) {
            dict.put(i, i + "");
            dict2.put(100 - i, i + "");
        }
        int i = 0;
        for (KVPair<Integer, String> pair : dict) {
            assertEquals(i, pair.getKey());
            assertEquals(i + "", pair.getValue());
            i++;
        }
        i--;
        for (KVPair<Integer, String> pair : dict2) {
            assertEquals(100 - i, pair.getKey());
            i--;
        }

    }

    @Test(timeout = SECOND)
    public void testAscendingOrder2() {
        IDictionary<Integer, String> dict = this.newDictionary();
        for (int i = 0; i < 100; i++) {
            dict.put(i * 2 + 1, i + "");
            dict.put(i * 2, i + "");
        }

        int i = 0;
        for (KVPair<Integer, String> pair : dict) {
            assertEquals(i, pair.getKey());
            i++;
        }
    }

    @Test(timeout = SECOND)
    public void testAscendingOrder3() {
        IDictionary<Integer, String> dict = this.newDictionary();
        int k = 100;
        for (int i = 0; i < 100; i++) {
            dict.put(k, i + "");
            if (i % 10 == 0) {
                k -= 2;
            }
        }
        k = 80;
        for (KVPair<Integer, String> pair : dict) {
            assertEquals(k, pair.getKey());
            k += 2;
        }
    }

}