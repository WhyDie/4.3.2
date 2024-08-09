package ua.whydie.hash;

public class HashEntry<K, V> {
    K key;
    V value;

    HashEntry(K key, V value) {
        this.key = key;
        this.value = value;
    }
}
