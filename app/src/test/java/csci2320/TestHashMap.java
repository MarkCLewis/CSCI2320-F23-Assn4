package csci2320;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class TestHashMap {
  @Test void emptyOnCreate() {
    Map<String, Integer> hash = new HashMap<>();
    assertEquals(0, hash.size());
  }

  // I'm giving you this test of your iterator that does a petty good job of finding
  // standard errors.
  // @Test void testIterator() {
  //   Map<String, Integer> hash = new HashMap<>();
  //   assertEquals(0, hash.size());
  //   var keys = List.of("abc", "xyz", "def", "bob", "pat");
  //   var vals = List.of(3, 19, 42, 16, 32);
  //   for (int i = 0; i < keys.size(); ++i) {
  //     hash.put(keys.get(i), vals.get(i));
  //   }
  //   assertEquals(keys.size(), hash.size());
  //   int cnt = 0;
  //   for (var kvp: hash) {
  //     int index = keys.indexOf(kvp.key());
  //     assertEquals(kvp.value(), vals.get(index));
  //     cnt++;
  //     assertTrue(cnt <= keys.size()); // This prevents you from going into an infinite loop if your iterator doesn't stop.
  //   }
  //   assertEquals(keys.size(), cnt);
  // }
}
