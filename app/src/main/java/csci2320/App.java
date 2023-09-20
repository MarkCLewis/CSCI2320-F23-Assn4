/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package csci2320;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;  

public class App {
    public static final int KEY_LEN = 8;
    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            Random rand = new Random(sc.nextLong());
            Map<String, Integer> map = buildMap(sc.nextInt(), rand);
            printMapSums(map);
            String testType = sc.next();
            switch (testType) {
                case "pg":
                    testPutGet(map, rand);
                    break;
                case "remove":
                    testRemove(map, rand);
                    break;
                case "goe":
                    testGetOrElse(map, rand);
                    break;
                case "keyset":
                    testKeySet(map, rand);
                    break;
                case "map":
                    map = testMapValues(map, rand);
                    break;
                case "filter":
                    map = testFilter(map, rand);
                    break;
                case "find":
                    testFind(map, rand);
                    break;
                case "fold":
                    testFold(map, rand);
                    break;
                case "exists":
                    testExists(map, rand);
                    break;
                case "forall":
                    testForall(map, rand);
                    break;
            }
            printMapSums(map);
        }
    }

    static String randomString(Random rand, int len) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; ++i) {
            sb.append((char)('a'+rand.nextInt(26)));
        }
        return sb.toString();
    }

    static HashMap<String, Integer> buildMap(int size, Random rand) {
        HashMap<String, Integer> ret = new HashMap<>();
        for (int i = 0; i < size; ++i) {
            String key = randomString(rand, KEY_LEN);
            while (ret.contains(key)) key = randomString(rand, KEY_LEN);
            ret.put(key, rand.nextInt());
        }
        return ret;
    }

    static List<String> selectedKeys(Map<String, Integer> map, Random rand, int estCount) {
        List<String> ret = new ArrayList<>();
        for (Map.KeyValuePair<String, Integer> kvp: map) {
            if (rand.nextInt(map.size()/estCount) == 0) ret.add(kvp.key());
        }
        return ret;
    }

    static void printMapSums(Map<String, Integer> map) {
        int sum = 0;
        int strSum = 0;
        for (Map.KeyValuePair<String, Integer> kvp: map) {
            sum += kvp.value();
            strSum += kvp.key().hashCode();
        }
        System.out.println(strSum+" "+sum);
    }

    static void testPutGet(Map<String, Integer> map, Random rand) {
        List<String> keys = selectedKeys(map, rand, 20);
        List<Map.KeyValuePair<String, Integer>> pairs = new ArrayList<>();
        for (String key: keys) {
            int value = rand.nextInt();
            pairs.add(new Map.KeyValuePair<String, Integer>(key, value));
            map.put(key, value);
        }
        for (Map.KeyValuePair<String, Integer> kvp: pairs) {
            if (!kvp.value().equals(map.get(kvp.key()).get())) {
                System.out.println("put-get pair failed " + kvp.value() + " " + kvp.key() + " " + map.get(kvp.key()));
                return;
            }
        }
    }

    static void testRemove(Map<String, Integer> map, Random rand) {
        List<String> keys = selectedKeys(map, rand, 10);
        for (String key: keys) {
            int len = map.size();
            var at = map.get(key);
            var removed = map.remove(key);
            if (!removed.equals(at)) {
                System.out.println("Removed value doesn't match the get. " + at + " != " + removed);
                return;
            }
            if (at.isPresent() && map.size() != len-1) {
                System.out.println("Size didn't decrease by one on remove.");
                return;
            }
        }
    }

    static void testGetOrElse(Map<String, Integer> map, Random rand) {
        List<String> keys = selectedKeys(map, rand, 20);
        for (int i = 0; i < 5; ++i) {
            var key = randomString(rand, 10);
            if (map.contains(key) || keys.contains(key)) {
                i--;
            } else {
                keys.add(key);
            }
        }
        for (String key: keys) {
            var at = map.get(key);
            if (map.contains(key)) {
                var goe = map.getOrElse(key, 999);
                if (goe.equals(999)) {
                    System.out.println("Got default when contains says key is there.");
                }
                if (!goe.equals(at.get())) {
                    System.out.println("getOrElse disagrees with get when contains says key is there.");
                }
            } else {
                if (!map.getOrElse(key, 999).equals(999)) {
                    System.out.println("Got non-default when contains says key is not there.");
                }
                if (at.isPresent()) {
                    System.out.println("Get didn't return empty when contain says key is not there.");
                }
            }
        }
    }

    static void testKeySet(Map<String, Integer> map, Random rand) {
        var keySet = map.keySet();
        if (keySet.size() != map.size()) {
            System.out.println("Key set size is different.");
        }
        int cnt = 0;
        for (String key: keySet) {
            cnt++;
            if (!map.contains(key)) {
                System.out.println("Key set has key not in map. " + key);
            }
        }
        if (cnt != map.size()) {
            System.out.println("Key set iterator not the right length. "+cnt+" != "+map.size());
        }
        for (Map.KeyValuePair<String, Integer> kvp: map) {
            if (!keySet.contains(kvp.key())) {
                System.out.println("Key in map not in key set. "+kvp.key());
            }
        }
    }

    static Map<String, Integer> testMapValues(Map<String, Integer> map, Random rand) {
        var map2 = map.mapValues(i -> i/2);
        for (Map.KeyValuePair<String, Integer> kvp: map) {
            if (map2.get(kvp.key()).get() != kvp.value() / 2) {
                System.out.println("Division maps didn't match.");
                return map2;
            }
        }
        var map3 = map.mapValues(i -> "str:" + i);
        for (Map.KeyValuePair<String, Integer> kvp: map) {
            if (!map3.get(kvp.key()).get().equals("str:" + kvp.value())) {
                System.out.println("String maps didn't match. " + map3.get(kvp.key()) + "!= str:" + kvp.value());
                return map2;
            }
        }
        return map2;
    }

    static Map<String, Integer> testFilter(Map<String, Integer> map, Random rand) {
        var evens = map.filter(kvp -> kvp.value() % 2 == 0);
        int j = 0;
        for (Map.KeyValuePair<String, Integer> kvp: map) {
            if (kvp.value() % 2 == 0) {
                if (kvp.value() != evens.get(kvp.key()).get()) {
                    System.out.println("Filter match error.");
                    return evens;
                }
                j++;
            }
        }
        if (j != evens.size()) {
            System.out.println("Filter length mismatch.");
        }
        return evens;
    }

    static void testFind(Map<String, Integer> map, Random rand) {
        var div5loc = map.find(kvp -> kvp.value() % 5 == 0);
        int j = 0;
        for(Map.KeyValuePair<String, Integer> kvp: map) {
            if (kvp.value() % 5 != 0) ++j;
            else break;
        }
        if (div5loc.isPresent() && !map.get(div5loc.get().key()).get().equals(div5loc.get().value()) || !div5loc.isPresent() && j < map.size()) {
            System.out.println("Find failed % 5.");
        }

        final int bigVal = 1800000000;
        var bigloc = map.find(kvp -> kvp.value() > bigVal);
        j = 0;
        for(Map.KeyValuePair<String, Integer> kvp: map) {
            if (kvp.value() <= bigVal) ++j;
            else break;
        }
        if (bigloc.isPresent() && !map.get(bigloc.get().key()).get().equals(bigloc.get().value()) || !bigloc.isPresent() && j < map.size()) {
            System.out.println("Find failed for big.");
        }
    }

    static void testFold(Map<String, Integer> map, Random rand) {
        var sumL = 0L;
        for (var kvp: map) sumL += kvp.value();
        var foldSum = map.fold(0L, (s, kvp) -> s + kvp.value());
        if (sumL != foldSum) {
            System.out.println("Fold sums don't match. " + sumL + " " + foldSum);
        }
        String stringcat = map.fold("", (s, kvp) -> s+kvp.key());
        int lenSum = 0;
        for (var kvp: map) lenSum += kvp.key().length();
        if (lenSum != stringcat.length()) {
            System.out.println("Fold to string wrong length.");
        }
    }

    static void testExists(Map<String, Integer> map, Random rand) {
        var keys = selectedKeys(map, rand, 10);
        for (var key: keys) {
            if (!map.exists(kvp -> kvp.key().equals(key))) {
                System.out.println("Key from map not found by exists.");
            }
        }
        var nonKey = randomString(rand, KEY_LEN+2);
        if (map.exists(kvp -> kvp.key().equals(nonKey))) {
            System.out.println("Exists found key with wrong length.");
        }
    }

    static void testForall(Map<String, Integer> map, Random rand) {
        var keySet = map.keySet();
        if (!map.forall(kvp -> keySet.contains(kvp.key()))) {
            System.out.println("Forall failed checking map keys in key set.");
        }
        var keys = selectedKeys(map, rand, 10);
        for (var key: keys) {
            if (map.forall(kvp -> kvp.key().equals(key))) {
                System.out.println("All keys matched single value.");
            }
        }
    }
}
