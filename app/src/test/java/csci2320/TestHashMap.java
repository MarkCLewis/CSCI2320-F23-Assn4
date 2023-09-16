package csci2320;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class TestHashMap {
  @Test void emptyOnCreate() {
    Map<String, Integer> hash = new HashMap<>();
    assertEquals(0, hash.size());
  }
}
