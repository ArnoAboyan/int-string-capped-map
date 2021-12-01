package com.epam.autotasks.collections;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.epam.autotasks.collections.IntStringCappedMapTest.toStringSet;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class IntStringCappedMapRandomTest {

    static Stream<Arguments> testCases() {
        return Stream.of(
                42,
                654692,
                7714,
                845632,
                15966,
                12358,
                852139,
                8569,
                112584,
                3263
        ).map(i -> arguments(i, expectedList(i)));
    }

    @ParameterizedTest(name = "[{index}] {0}")
    @MethodSource("testCases")
    void testInitialState(int seed, List<String> expected) {
        Random random = new Random(seed);
        IntStringCappedMap map = new IntStringCappedMap(10 + random.nextInt(20));

        Iterator<String> expectedIt = expected.iterator();

        int iterations = 10 + random.nextInt(10);
        for (int i = 0; i < iterations; i++) {
            if (random.nextInt(4) != 0) {
                map.put(random.nextInt(10),
                        Integer.toString(random.nextInt(10))
                                .repeat(random.nextInt(8))
                );
            } else {
                map.remove(random.nextInt(5));
            }
            assertEquals(expectedIt.next(), toString(map));
        }
    }

    private String toString(final IntStringCappedMap map) {
        return "map(" + map.getCapacity() + "):" + toStringSet(map).toString();
    }

    private void writeFile(final int seed, final String actual) {
        try {
            Files.writeString(
                    Path.of("src", "test", "resources", seed + ".txt"),
                    actual + "\n", StandardOpenOption.APPEND, StandardOpenOption.CREATE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<String> expectedList(int seed) {
        try {
            return Files.readAllLines(Path.of("src", "test", "resources", seed + ".txt"));
        } catch (IOException e) {
            return List.of();
        }
    }

}