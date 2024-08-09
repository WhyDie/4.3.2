package ua.whydie;

import ua.whydie.hash.LinearProbingHashTable;

public class Main {
    public static void main(String[] args) {
        LinearProbingHashTable<String, Integer> table = new LinearProbingHashTable<>();

        table.put("one", 1);
        table.put("two", 2);
        table.put("three", 3);

        System.out.println("Size: " + table.size());
        System.out.println("Get 'one': " + table.get("one"));
        System.out.println("Get 'two': " + table.get("two"));

        table.delete("two");
        System.out.println("Get 'two' after deletion: " + table.get("two"));

        for (String key : table.keys()) {
            System.out.println("Key: " + key + ", Value: " + table.get(key));
        }
    }
}
