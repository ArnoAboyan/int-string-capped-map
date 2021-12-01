package com.epam.autotasks.collections;

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

class IntStringCappedMap extends AbstractMap<Integer, String> {

    private final long capacity;

    public IntStringCappedMap(final long capacity) {
        this.capacity = capacity;
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
                    @Override
                    public boolean hasNext() {
                        //implement this method
                        throw new UnsupportedOperationException();
                    }

                    @Override
                    public Entry<Integer, String> next() {
                        //implement this method
                        throw new UnsupportedOperationException();
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
        //implement this method
        throw new UnsupportedOperationException();
    }

    @Override
    public String put(final Integer key, final String value) {
        //implement this method
        throw new UnsupportedOperationException();
    }

    @Override
    public String remove(final Object key) {
        //implement this method
        throw new UnsupportedOperationException();
    }

    @Override
    public int size() {
        //implement this method
        throw new UnsupportedOperationException();
    }

}
