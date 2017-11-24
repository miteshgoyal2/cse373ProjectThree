package datastructures.concrete.dictionaries;

import datastructures.interfaces.IDictionary;

public class Main {

    public static void main(String[] args) {

        IDictionary<Integer, Integer> dict = new AvlTreeDictionary<Integer, Integer>();

        for (int i = 0; i < 100; i++) {
            dict.put(i, i);
        }
        dict.put(1, 5);

        System.out.println(dict.size());

        // for (int i = 0; i < 50; i++) {
        // dict.remove(i);
        // }

        System.out.println(dict.size());

    }

}
