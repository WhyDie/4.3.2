package ua.whydie.hash;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinearProbingHashTable<K, V> implements Iterable<K> {
    private static final int INIT_CAPACITY = 16;
    private static final double DEFAULT_LOAD_FACTOR = 0.5;

    private double loadFactor;
    private int threshold;
    private int countOfItems;
    private int capacity;
    private HashEntry<K, V>[] table;

    public LinearProbingHashTable() {
        this(INIT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public LinearProbingHashTable(int capacity) {
        this(capacity, DEFAULT_LOAD_FACTOR);
    }

    @SuppressWarnings("unchecked")
    public LinearProbingHashTable(int capacity, double loadFactor) {
        this.capacity = capacity;
        this.loadFactor = loadFactor;
        this.threshold = (int) (capacity * loadFactor);
        this.table = new HashEntry[capacity];
        this.countOfItems = 0;
    }

    public int size() {
        return countOfItems;
    }

    public boolean isEmpty() {
        return countOfItems == 0;
    }

    public boolean contains(K key) {
        return get(key) != null;
    }

    private int hash(K key) {
        return (key.hashCode() & 0x7fffffff) % capacity;
    }

    private int getBucketIndex(K key) {
        int index = hash(key);
        while (table[index] != null) {
            if (table[index].key.equals(key)) {
                return index;
            }
            index = (index + 1) % capacity;
        }
        return index;
    }

    public void put(K key, V value) {
        if (countOfItems >= threshold) {
            resize(2 * capacity);
        }

        int index = getBucketIndex(key);
        if (table[index] == null) {
            countOfItems++;
        }
        table[index] = new HashEntry<>(key, value);
    }

    public V get(K key) {
        int index = getBucketIndex(key);
        if (table[index] != null && table[index].key.equals(key)) {
            return table[index].value;
        }
        return null;
    }

    public void delete(K key) {
        int index = getBucketIndex(key);
        if (table[index] == null) {
            return;
        }

        table[index] = null;

        index = (index + 1) % capacity;
        while (table[index] != null) {
            K rehashKey = table[index].key;
            V rehashValue = table[index].value;
            table[index] = null;
            countOfItems--;
            put(rehashKey, rehashValue);
            index = (index + 1) % capacity;
        }
        countOfItems--;

        if (countOfItems > 0 && countOfItems <= capacity / 8) {
            resize(capacity / 2);
        }
    }

    private void resize(int newCapacity) {
        LinearProbingHashTable<K, V> temp = new LinearProbingHashTable<>(newCapacity, loadFactor);
        for (int i = 0; i < capacity; i++) {
            if (table[i] != null) {
                temp.put(table[i].key, table[i].value);
            }
        }
        table = temp.table;
        capacity = temp.capacity;
        threshold = temp.threshold;
    }

    @Override
    public Iterator<K> iterator() {
        return new Iterator<K>() {
            private int current = 0;

            @Override
            public boolean hasNext() {
                while (current < capacity && table[current] == null) {
                    current++;
                }
                return current < capacity;
            }

            @Override
            public K next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return table[current++].key;
            }
        };
    }

    public Iterable<K> keys() {
        return this;
    }
}
