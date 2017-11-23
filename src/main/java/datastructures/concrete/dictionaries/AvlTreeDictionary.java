package datastructures.concrete.dictionaries;

import java.util.Iterator;
import java.util.NoSuchElementException;

import datastructures.concrete.KVPair;
import datastructures.interfaces.IDictionary;
import misc.exceptions.NoSuchKeyException;

public class AvlTreeDictionary<K extends Comparable<K>, V> implements IDictionary<K, V> {

    private int size;
    private AvlTreeNode<K, V> overallRoot;

    public AvlTreeDictionary() {
        size = 0;
        overallRoot = null;
    }

    @Override
    public V get(K key) {
        return get(key, overallRoot);
    }

    private V get(K key, AvlTreeNode<K, V> root) {
        if (root == null) {
            throw new NoSuchKeyException();
        }
        if (key.equals(root.key)) {
            return root.value;
        }
        int compare = key.compareTo(root.key);
        if (compare < 0) {
            return get(key, root.left);
        } else {
            return get(key, root.right);
        }
    }

    @Override
    public void put(K key, V value) {
        overallRoot = put(key, value, overallRoot);
    }

    private AvlTreeNode<K, V> put(K key, V value, AvlTreeNode<K, V> root) {
        if (root == null) {
            size++;
            return new AvlTreeNode<K, V>(key, value);
        }
        int compare = key.compareTo(root.key);
        if (compare < 0) {
            root.left = put(key, value, root.left);
        } else if (compare > 0) {
            root.right = put(key, value, root.right);
        } else {
            root.value = value;
        }
        root.height = Math.max(height(root.left), height(root.right)) + 1;
        root = validate(root);
        return root;

    }

    @Override
    public V remove(K key) {
        V output = this.get(key);
        overallRoot = remove(key, overallRoot);
        return output;
    }

    private AvlTreeNode<K, V> remove(K key, AvlTreeNode<K, V> root) {
        if (root == null) {
            return root;
        }
        int compare = key.compareTo(root.key);
        if (compare < 0) {
            root.left = remove(key, root.left);
        } else if (compare > 0) {
            root.right = remove(key, root.right);
        } else if (root.left != null && root.right != null) {
            AvlTreeNode<K, V> min = minAvlNode(root.right);
            root.key = min.key;
            root.value = min.value;
            root.right = remove(root.key, root.right);
        } else {
            if (root.left != null) {
                return root.left;
            } else {
                return root.right;
            }
        }
        size--;
        root.height = Math.max(height(root.left), height(root.right)) + 1;
        root = validate(root);
        return root;
    }

    @Override
    public boolean containsKey(K key) {
        if (size == 0) {
            return false;
        }
        return containsKey(key, overallRoot);
    }

    private boolean containsKey(K key, AvlTreeNode<K, V> root) {
        if (root == null) {
            return false;
        }
        if (root.key.equals(key)) {
            return true;
        }
        int compare = key.compareTo(root.key);
        if (compare < 0) {
            return containsKey(key, root.left);
        } else {
            return containsKey(key, root.right);
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<KVPair<K, V>> iterator() {
        return new AvlTreeDictionaryIterator(overallRoot, size);
    }

    // helper methods
    private int height(AvlTreeNode<K, V> item) {
        return item == null ? -1 : item.height;
    }

    public String toString() {
        return toString(overallRoot, "");
    }

    private String toString(AvlTreeNode<K, V> root, String indent) {
        if (root == null) {
            return "";
        } else {
            return toString(root.right, indent + "     ") + indent + root + "\n"
                    + toString(root.left, indent + "     ");
        }
    }

    private AvlTreeNode<K, V> minAvlNode(AvlTreeNode<K, V> root) {
        if (root.left == null) {
            return root;
        }
        return minAvlNode(root.left);
    }

    private AvlTreeNode<K, V> validate(AvlTreeNode<K, V> root) {
        if (root == null) {
            return root;
        }
        if (height(root.left) - height(root.right) > 1) {
            if (height(root.left.left) >= height(root.left.right)) {
                // left left
                root = singleRotationLeft(root);
            } else {
                // left right
                root = doubleRotationLeft(root);
            }
        }

        if (height(root.right) - height(root.left) > 1) {
            if (height(root.right.right) >= height(root.right.left)) {
                // right right
                root = singleRotationRight(root);
            } else {
                // right left
                root = doubleRotationRight(root);
            }
        }

        root.height = Math.max(height(root.left), height(root.right)) + 1;
        return root;
    }

    private AvlTreeNode<K, V> doubleRotationRight(AvlTreeNode<K, V> root) {
        root.right = singleRotationLeft(root.right);
        root = singleRotationRight(root);
        return root;
    }

    private AvlTreeNode<K, V> singleRotationRight(AvlTreeNode<K, V> root) {
        AvlTreeNode<K, V> newRoot = root.right;
        root.right = newRoot.left;
        newRoot.left = root;
        root.height = Math.max(height(root.left), height(root.right)) + 1;
        newRoot.height = Math.max((height(newRoot.left)), height(newRoot.right)) + 1;
        return newRoot;
    }

    private AvlTreeNode<K, V> doubleRotationLeft(AvlTreeNode<K, V> root) {
        root.left = singleRotationRight(root.left);
        root = singleRotationLeft(root);
        return root;
    }

    private AvlTreeNode<K, V> singleRotationLeft(AvlTreeNode<K, V> root) {
        AvlTreeNode<K, V> newRoot = root.left;
        root.left = newRoot.right;
        newRoot.right = root;
        root.height = Math.max(height(root.left), height(root.right)) + 1;
        newRoot.height = Math.max((height(newRoot.left)), height(newRoot.right)) + 1;
        return newRoot;
    }

    private static class AvlTreeNode<K, V> {

        public K key;
        public V value;
        public AvlTreeNode<K, V> left;
        public AvlTreeNode<K, V> right;
        public int height;

        public AvlTreeNode(K key, V value) {
            this(key, value, null, null, 0);
        }

        public AvlTreeNode(K key, V value, AvlTreeNode<K, V> left, AvlTreeNode<K, V> right, int height) {
            this.key = key;
            this.value = value;
            this.left = left;
            this.right = right;
            this.height = height;
        }

        public String toString() {
            return key + "=" + value;
        }

    }

    private class AvlTreeDictionaryIterator implements Iterator<KVPair<K, V>> {

        private int index;
        private KVPair<K, V>[] treeArray;

        @SuppressWarnings("unchecked")
        public AvlTreeDictionaryIterator(AvlTreeNode<K, V> root, int size) {
            index = -1;
            treeArray = (KVPair<K, V>[]) (new KVPair[size]);
            inOrderTraversal(root, index + 1);
        }

        @Override
        public boolean hasNext() {
            return index + 1 < treeArray.length;
        }

        @Override
        public KVPair<K, V> next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            index++;
            return treeArray[index];
        }

        private int inOrderTraversal(AvlTreeNode<K, V> root, int i) {
            if (root == null) {
                return i;
            }
            i = inOrderTraversal(root.left, i);
            KVPair<K, V> pair = new KVPair<K, V>(root.key, root.value);
            treeArray[i] = pair;
            i++;
            i = inOrderTraversal(root.right, i);
            return i;
        }

    }

}
