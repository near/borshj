/* This is free and unencumbered software released into the public domain. */

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.near.borshj.Borsh;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Objects;

public class BorshTests {
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

  static public class Point2Df implements Borsh {
    private float x;
    private float y;

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
    private Point2Df topLeft;
    private Point2Df bottomRight;

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

  static class PojoTestEntity {
    final byte[] byteField;
    final String stringField;

    PojoTestEntity(byte[] byteField, String stringField) {
      this.byteField = byteField;
      this.stringField = stringField;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof PojoTestEntity)) return false;
      PojoTestEntity that = (PojoTestEntity) o;
      return Arrays.equals(byteField, that.byteField) && stringField.equals(that.stringField);
    }

    @Override
    public int hashCode() {
      int result = Objects.hash(stringField);
      result = 31 * result + Arrays.hashCode(byteField);
      return result;
    }
  }

  static class PojoTestWrongTypeEntity{
    final BigDecimal bigDecimalField;

    PojoTestWrongTypeEntity(BigDecimal bigDecimalField) {
      this.bigDecimalField = bigDecimalField;
    }
  }
}
