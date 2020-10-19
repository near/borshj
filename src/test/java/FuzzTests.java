/* This is free and unencumbered software released into the public domain. */

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import net.sf.cglib.beans.BeanGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.near.borshj.Borsh;

public class FuzzTests {
  private BeanGenerator generator;

  @BeforeEach
  void newGenerator() {
    generator = new BeanGenerator();
  }

  static public class Base implements Borsh {
    @Override
    public String toString() {
      final StringBuilder buffer = new StringBuilder();
      buffer.append(this.getClass().getName());
      buffer.append('(');
      try {
        for (final Field field : this.getClass().getDeclaredFields()) {
          field.get(this); // TODO
        }
      }
      catch (final IllegalAccessException e) {}
      buffer.append(')');
      return buffer.toString();
    }

    @Override
    public boolean equals(final Object object) {
      if (object == null || object.getClass() != this.getClass()) return false;
      try {
        for (final Field field : this.getClass().getDeclaredFields()) {
          field.setAccessible(true);
          if (!field.get(this).equals(field.get(object))) {
            return false;
          }
        }
        return true;
      }
      catch (final IllegalAccessException e) {
        return false;
      }
    }
  }

  @Test
  void testGenerator() {
    // TODO
  }
}
