package com.epam.autotasks.collections;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class IntStringCappedMapTest {

    @Test
    void testInitialState() {
        var map = new IntStringCappedMap(100);
        assertAll(
                () -> assertEquals(Set.of(), toStringSet(map)),
                () -> assertEquals(0, map.size())
        );
    }

    @Test
    void testSimplePut() {
        var map = new IntStringCappedMap(100);
        assertNull(map.put(1, "1"));
        assertNull(map.put(3, "333"));
        assertNull(map.put(4, "4444"));
        assertNull(map.put(2, "22"));
        assertAll(
                () -> assertEquals(Set.of("1:1", "2:22", "3:333", "4:4444"), toStringSet(map)),
                () -> assertEquals(4, map.size())
        );
    }

    @Test
    void testPutWithSingleEvict() {
        var map = new IntStringCappedMap(10);
        assertNull(map.put(12345, "12345"));
        assertNull(map.put(123, "123"));
        assertNull(map.put(1234, "1234"));
        assertAll(
                () -> assertEquals(Set.of("123:123", "1234:1234"), toStringSet(map)),
                () -> assertEquals(2, map.size())
        );
    }

    @Test
    void testPutWithMultipleEvictions() {
        {
            var map = new IntStringCappedMap(10);

            assertNull(map.put(12, "12"));
            assertNull(map.put(123, "123"));
            assertNull(map.put(1234, "1234"));
            assertNull(map.put(12345, "12345"));

            assertAll(
                    () -> assertEquals(Set.of("1234:1234", "12345:12345"), toStringSet(map)),
                    () -> assertEquals(2, map.size())
            );
        }
        {
            var map = new IntStringCappedMap(10);
            assertNull(map.put(1234, "1234"));
            assertNull(map.put(123, "123"));
            assertNull(map.put(12, "12"));
            assertNull(map.put(1, "1"));
            assertNull(map.put(12345678, "12345678"));

            assertAll(
                    () -> assertEquals(Set.of("12345678:12345678", "1:1"), toStringSet(map)),
                    () -> assertEquals(2, map.size())
            );
        }
    }

    @Test
    void testPutTooLargeInput() {
        var map = new IntStringCappedMap(10);
        assertThrows(IllegalArgumentException.class, () -> map.put(1, "Too large input!"));
        assertNull(map.put(12345, "12345"));

        assertThrows(IllegalArgumentException.class, () -> map.put(1, "Too large input!"));
        assertThrows(IllegalArgumentException.class, () -> map.put(12345, "Too large input!"));

        assertNull(map.put(1234, "1234"));

        assertThrows(IllegalArgumentException.class, () -> map.put(1, "Too large input!"));

        assertThrows(IllegalArgumentException.class,
                () -> new IntStringCappedMap(2).put(123, "123"));
    }

    @Test
    void testPutSubstituting() {
        var map = new IntStringCappedMap(100);
        assertNull(map.put(12, "12"));
        assertNull(map.put(1234, "1234"));
        assertNull(map.put(123, "123"));
        assertNull(map.put(1, "1"));

        assertEquals("1234", map.put(1234, "4321"));
        assertEquals("1", map.put(1, "one"));


        assertAll(
                () -> assertEquals(Set.of("1:one", "12:12", "123:123", "1234:4321"), toStringSet(map)),
                () -> assertEquals(4, map.size())
        );
    }

    @Test
    void testPutSubstitutingNoEvictionIsNeeded() {
        {
            var map = new IntStringCappedMap(5);
            assertNull(map.put(12, "12"));
            assertNull(map.put(123, "123"));

            assertEquals("123", map.put(123, "321"));

            assertAll(
                    () -> assertEquals(Set.of("12:12", "123:321"), toStringSet(map)),
                    () -> assertEquals(2, map.size())
            );
        }

        {
            var map = new IntStringCappedMap(10);
            for (int i = 0; i < 7; i++) {
                assertNull(map.put(i + 1, Integer.toString(i + 1)));
            }
            assertEquals("4", map.put(4, "4444"));
            assertAll(
                    () -> assertEquals(
                            Set.of("1:1", "2:2", "3:3", "4:4444", "5:5", "6:6", "7:7"),
                            toStringSet(map)),
                    () -> assertEquals(7, map.size())
            );
        }
    }

    @Test
    void testPutSubstitutingWithEviction() {
        var map = new IntStringCappedMap(10);
        for (int i = 0; i < 8; i++) {
            assertNull(map.put(8 - i, Integer.toString(8 - i)));
        }
        assertEquals("5", map.put(5, "55555"));
        assertAll(
                () -> assertEquals(
                        Set.of("1:1", "2:2", "3:3", "4:4", "5:55555", "6:6"),
                        toStringSet(map)),
                () -> assertEquals(6, map.size())
        );
    }

    @Test
    void testPutWithEvictionsChain() {
        var map = new IntStringCappedMap(25);
        map.put(5, "Five");
        map.put(6, "Six");
        map.put(7, "Seven");
        map.put(8, "Eight");
        map.put(12, "Twelve");
        map.put(9, "Nine");
        map.put(1, "One");

        assertAll(
                () -> assertEquals(
                        Set.of("1:One", "7:Seven", "8:Eight", "9:Nine", "12:Twelve"),
                        toStringSet(map)),
                () -> assertEquals(5, map.size())
        );

        for (Integer key : new HashSet<>(map.keySet())) {
            map.put(key, map.get(key).toLowerCase());
        }

        assertAll(
                () -> assertEquals(
                        Set.of("1:one", "7:seven", "8:eight", "9:nine", "12:twelve"),
                        toStringSet(map)),
                () -> assertEquals(5, map.size())
        );

        for (String value : new TreeSet<>(map.values())) {
            map.put(value.length(), value.repeat(2));
        }

        assertAll(
                () -> assertEquals(
                        Set.of("5:sevenseven", "6:twelvetwelve"),
                        toStringSet(map)),
                () -> assertEquals(2, map.size())
        );

        map.put(13, "1111_1111_111");

        assertAll(
                () -> assertEquals(
                        Set.of("6:twelvetwelve", "13:1111_1111_111"),
                        toStringSet(map)),
                () -> assertEquals(2, map.size())
        );

        map.put(13, "2222_1111_333");

        assertAll(
                () -> assertEquals(
                        Set.of("6:twelvetwelve", "13:2222_1111_333"),
                        toStringSet(map)),
                () -> assertEquals(2, map.size())
        );

        map.put(14, "1111_1111_1111");

        assertAll(
                () -> assertEquals(
                        Set.of("14:1111_1111_1111"),
                        toStringSet(map)),
                () -> assertEquals(1, map.size())
        );

        assertThrows(IllegalArgumentException.class, () -> map.put(26, "1".repeat(26)));

        map.put(1, "a".repeat(25));

        assertAll(
                () -> assertEquals(
                        Set.of("1:aaaaaaaaaaaaaaaaaaaaaaaaa"),
                        toStringSet(map)),
                () -> assertEquals(1, map.size())
        );

        map.put(2, "a");

        assertAll(
                () -> assertEquals(
                        Set.of("2:a"),
                        toStringSet(map)),
                () -> assertEquals(1, map.size())
        );

    }

    @Test
    void testRemove() {
        var map = new IntStringCappedMap(5);
        for (int i = 1; i <= 5; i++) {
            assertNull(map.put(i, Integer.toString(i)));
        }

        assertAll(
                () -> assertEquals(Set.of("1:1", "2:2", "3:3", "4:4", "5:5"), toStringSet(map)),
                () -> assertEquals(5, map.size())
        );

        assertEquals("2", map.remove(2));
        assertEquals("5", map.remove(5));
        assertNull(map.remove(5));
        assertNull(map.remove(999));

        assertAll(
                () -> assertEquals(Set.of("1:1", "3:3", "4:4"), toStringSet(map)),
                () -> assertEquals(3, map.size())
        );

    }

    @Test
    void testRemoveThenPutNoEvictions() {
        var map = new IntStringCappedMap(5);
        for (int i = 1; i <= 5; i++) {
            assertNull(map.put(i, Integer.toString(i)));
        }

        assertEquals("2", map.remove(2));
        assertEquals("5", map.remove(5));

        assertNull(map.put(88, "88"));

        assertAll(
                () -> assertEquals(Set.of("1:1", "3:3", "4:4", "88:88"), toStringSet(map)),
                () -> assertEquals(4, map.size())
        );
    }

    @Test
    void testRemoveThenPutWithEvictions() {
        {
            var map = new IntStringCappedMap(6);
            for (int i = 1; i <= 5; i++) {
                assertNull(map.put(i, Integer.toString(i)));
            }

            assertEquals("2", map.remove(2));
            assertEquals("5", map.remove(5));

            assertNull(map.put(8, "88888"));

            assertAll(
                    () -> assertEquals(Set.of("4:4", "8:88888"), toStringSet(map)),
                    () -> assertEquals(2, map.size())
            );
        }
        {
            var map = new IntStringCappedMap(6);
            for (int i = 1; i <= 5; i++) {
                assertNull(map.put(i, Integer.toString(i)));
            }

            assertEquals("2", map.remove(2));
            assertEquals("5", map.remove(5));

            assertNull(map.put(8, "8888"));

            assertAll(
                    () -> assertEquals(Set.of("3:3", "4:4", "8:8888"), toStringSet(map)),
                    () -> assertEquals(3, map.size())
            );
        }
    }

    static Set<String> toStringSet(IntStringCappedMap map) {
        return map.entrySet().stream()
                .map(e -> e.getKey() + ":" + e.getValue())
                .collect(Collectors.toCollection(TreeSet::new));
    }
}