package com.epam.autotasks.collections;

import java.util.*;


class IntStringCappedMap extends AbstractMap<Integer, String> {

    private final long capacity;

    private Map<Integer, String> map ;

    public IntStringCappedMap(final long capacity) {
        this.capacity = capacity;
        map = new LinkedHashMap<>((int)capacity);
    }

    public static <K, V> Entry<K, V> getFirst(Map<K, V> map) {
        if (map.isEmpty()) return null; return map.entrySet().iterator().next();
    }

    public long getCapacity() {
        return capacity;
    }

    @Override
    public Set<Entry<Integer, String>> entrySet() {
        return new AbstractSet<>() {
            @Override
            public Iterator<Entry<Integer, String>> iterator() {
                return new Iterator<>() {
                    Iterator<Entry<Integer, String>> value = map.entrySet().iterator();

                    @Override
                    public boolean hasNext() {
                        return value.hasNext();
                    }

                    @Override
                    public Entry<Integer, String> next() {
                        if (value.hasNext()) {
                            return value.next();
                        } else {
                            throw new NoSuchElementException();
                        }
                    }
                };
            }

            @Override
            public int size() {
                return IntStringCappedMap.this.size();
            }
        };
    }

    @Override
    public String get(final Object key) {
        return map.get(key);
    }

    private long totalSizeOfValues() {
        long total_size = 0;
        for (Map.Entry<Integer, String> entry: map.entrySet()) {
            total_size += entry.getValue().length();
        }
        return total_size;
    }
    @Override
    public String put(final Integer key, final String value) {
        if (value.length() > capacity) throw new IllegalArgumentException();
        String previousValue = null;
        if (map.containsKey(key)) {
            previousValue = map.remove(key);
        }
        while (totalSizeOfValues() + value.length() > capacity) {
            map.remove(getFirst(map).getKey());
        }

        String str = map.put(key, value);
        if (previousValue != null) return previousValue;
        return str;

    }


    @Override
    public String remove(final Object key) {
         return map.remove(key);
    }

    @Override
    public int size() {
        return map.size();

    }

}
