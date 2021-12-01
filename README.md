# Int String Capped Map

The purpose of this exercise is to train you in implementing Collections.

Estimated workload of this exercise is _180 min_.

### Description

Please, proceed to [IntStringCappedMap](src/main/java/com/epam/autotasks/collections/IntStringCappedMap.java)
and implement its methods.

`IntStringCappedMap` is a `Map` with `Integer` keys and `String` values.\
"Capped" means that this map has a *capacity* property. \
Total length of all `String` values in a map must not exceed its *capacity*.\
If a new added value would lead to such overflowing, 
the map must *evict* its current entries until adding new value would not exceed its capacity.\
Eviction must follow the order of adding values to the map -
the oldest value must be evicted first.\
Note that if length of the new `String` value is more than capacity,
map must throw an `IllegalArgumentException` and evict no entry.


You need to implement following methods:

- **entrySet** - the method is partially implemented. \
It returns an `AbstractSet` and you must only provide implementations for its iterator `next` and `hasNext` methods.
- **get** - return a value by its key.
- **put** - set a value by a given key.\
If it leads to exceeding capacity, be sure to evict as many of the oldest elements as needed. 
- **remove** - removes a value by key.
- **size** - returns number of map entries.

### Example

```java
IntStringCappedMap map = new IntStringCappedMap(25);
map.put(5, "Five");
map.put(6, "Six");
map.put(7, "Seven");
map.put(8, "Eight");
map.put(12, "Twelve");
map.put(9, "Nine");
map.put(1, "One");

System.out.println(new TreeMap<>(map)); 
//{1=One, 7=Seven, 8=Eight, 9=Nine, 12=Twelve}
```