package datastructures.dictionaries;

import org.junit.Test;

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

}