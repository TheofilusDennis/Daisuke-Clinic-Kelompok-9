public class Map {
    private static class Entry {
        String key;
        String[] value;
        Entry next;

        public Entry(String key, String[] value) {
            this.key = key;
            this.value = value;
        }
    }

    private Entry[] table;
    private int capacity;
    private int size;

    public Map(int capacity) {
        this.capacity = capacity;
        this.table = new Entry[capacity];
    }

    private int hash(String key) {
        return Math.abs(key.hashCode()) % capacity;
    }

    public void put(String key, String[] value) {
        int index = hash(key);
        Entry newEntry = new Entry(key, value);

        if (table[index] == null) {
            table[index] = newEntry;
        } else {
            Entry current = table[index];
            while (current.next != null) {
                if (current.key.equals(key)) {
                    current.value = value;
                    return;
                }
                current = current.next;
            }
            current.next = newEntry;
        }
        size++;
    }

    public String[] get(String key) {
        int index = hash(key);
        Entry current = table[index];

        while (current != null) {
            if (current.key.equals(key)) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    public boolean containsKey(String key) {
        return get(key) != null;
    }

    public int size() {
        return size;
    }
}