/* This is free and unencumbered software released into the public domain. */

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.near.borshj.Borsh;

public class BorshTests {
  static public class Point2Df implements Borsh {
    public float x;
    public float y;

    public Point2Df() {}

    public Point2Df(final float x, final float y) {
      this.x = x;
      this.y = y;
    }

    @Override
    public String toString() {
      return String.format("Point2Df(%f, %f)", this.x, this.y);
    }

    @Override
    public boolean equals(final Object object) {
      if (object == null || object.getClass() != this.getClass()) return false;
      final Point2Df other = (Point2Df)object;
      return this.x == other.x && this.y == other.y;
    }
  }

  static public class Rect2Df implements Borsh {
    public Point2Df topLeft;
    public Point2Df bottomRight;

    public Rect2Df() {}

    public Rect2Df(final Point2Df topLeft, final Point2Df bottomRight) {
      this.topLeft = topLeft;
      this.bottomRight = bottomRight;
    }

    @Override
    public String toString() {
      return String.format("Rect2Df(%s, %s)", this.topLeft.toString(), this.bottomRight.toString());
    }

    @Override
    public boolean equals(final Object object) {
      if (object == null || object.getClass() != this.getClass()) return false;
      final Rect2Df other = (Rect2Df)object;
      return this.topLeft.equals(other.topLeft) && this.bottomRight.equals(other.bottomRight);
    }
  }

  @Test
  void roundtripPoint2Df() {
    final Point2Df point = new Point2Df(123, 456);
    assertEquals(point, Borsh.deserialize(Borsh.serialize(point), Point2Df.class));
  }

  @Test
  void roundtripRect2Df() {
    final Point2Df topLeft = new Point2Df(-123, -456);
    final Point2Df bottomRight = new Point2Df(123, 456);
    final Rect2Df rect = new Rect2Df(topLeft, bottomRight);
    assertEquals(rect, Borsh.deserialize(Borsh.serialize(rect), Rect2Df.class));
  }

  @Test
  void writeOptional() {
    assertArrayEquals(new byte[]{0}, Borsh.serialize(Optional.empty()));
    assertArrayEquals(new byte[]{1, 42, 0, 0, 0}, Borsh.serialize(Optional.of(42)));
  }
}
