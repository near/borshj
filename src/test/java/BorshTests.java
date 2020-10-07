/* This is free and unencumbered software released into the public domain. */

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.near.borshj.Borsh;

public class BorshTests {
  static public class Point2Di implements Borsh {
    public int x;
    public int y;

    public Point2Di() {}

    public Point2Di(final int x, final int y) {
      this.x = x;
      this.y = y;
    }

    @Override
    public String toString() {
      return String.format("Point2Di(%d, %d)", this.x, this.y);
    }

    @Override
    public boolean equals(Object object) {
      if (object == null || object.getClass() != this.getClass()) {
        return false;
      }
      final Point2Di other = (Point2Di)object;
      return this.x == other.x && this.y == other.y;
    }
  }

  @Test
  void roundtripPoint2Di() {
    final Point2Di pt = new Point2Di(123, 456);
    assertEquals(pt, Borsh.deserialize(Borsh.serialize(pt), Point2Di.class));
  }
}
