/* This is free and unencumbered software released into the public domain. */

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.util.Random;
import net.sf.cglib.beans.BeanGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.Test;
import org.near.borshj.Borsh;

public class FuzzTests {
  static final int MAX_ITERATIONS = 1000;
  static final int MAX_RECURSION  = 2;
  static final int MAX_FIELDS     = 10;
  static final int MAX_STRING_LEN = 100;

  @Test
  void testBeanGenerator() throws Exception {
    final BeanGenerator beanGenerator = new BeanGenerator();
    beanGenerator.setSuperclass(Bean.class);
    beanGenerator.addProperty("name", String.class);
    beanGenerator.addProperty("email", String.class);
    beanGenerator.addProperty("age", Integer.class);
    beanGenerator.addProperty("twitter", Boolean.class);
    final Object bean = beanGenerator.create();
    bean.getClass().getMethod("setName", String.class).invoke(bean, "J. Random Hacker");
    bean.getClass().getMethod("setEmail", String.class).invoke(bean, "jhacker@example.org");
    bean.getClass().getMethod("setAge", Integer.class).invoke(bean, 42);
    bean.getClass().getMethod("setTwitter", Boolean.class).invoke(bean, true);
    assertEquals(bean, Borsh.deserialize(Borsh.serialize(bean), bean.getClass()));
  }

  @RepeatedTest(MAX_ITERATIONS)
  void testRandomBean(final RepetitionInfo test) throws Exception {
    final Random random = new Random(test.getCurrentRepetition());
    final Object bean = newRandomBean(random, 0);
    assertEquals(bean, Borsh.deserialize(Borsh.serialize(bean), bean.getClass()));
  }

  private Object newRandomBean(final Random random, final int level) throws Exception {
    final BeanGenerator beanGenerator = new BeanGenerator();
    beanGenerator.setSuperclass(Bean.class);
    final int fieldCount = random.nextInt(MAX_FIELDS);
    final Object[] fieldValues = new Object[fieldCount];
    for (int i = 0; i < fieldCount; i++) {
      final String fieldName = String.format("field%d", i);
      fieldValues[i] = newRandomValue(random, level);
      beanGenerator.addProperty(fieldName, fieldValues[i].getClass());
    }
    final Object bean = beanGenerator.create();
    for (int i = 0; i < fieldCount; i++) {
      final String setterName = String.format("setField%d", i);
      bean.getClass().getMethod(setterName, fieldValues[i].getClass()).invoke(bean, fieldValues[i]);
    }
    //System.err.println(bean.toString());  // DEBUG
    return bean;
  }

  private Object newRandomValue(final Random random, final int level) throws Exception {
    switch (Math.abs(random.nextInt()) % 10) {
      case 0: if (level < MAX_RECURSION) return newRandomBean(random, level + 1); else {/* fallthrough */}
      case 1: return random.nextBoolean();
      case 2: return (byte)random.nextInt(Byte.MAX_VALUE);
      case 3: return (short)random.nextInt(Short.MAX_VALUE);
      case 4: return random.nextInt();
      case 5: return random.nextLong();
      case 6: return BigInteger.valueOf(random.nextLong()).abs();
      case 7: return random.nextFloat();
      case 8: return random.nextDouble();
      case 9:
        return random.ints('a', 'z' + 1)
          .limit(random.nextInt(MAX_STRING_LEN))
          .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
          .toString();
      default: throw new AssertionError("unreachable");
    }
  }

  static public class Bean implements Borsh {
    @Override
    public String toString() {
      final StringBuilder buffer = new StringBuilder();
      buffer.append(this.getClass().getName());
      buffer.append('(');
      try {
        for (final Field field : this.getClass().getDeclaredFields()) {
          field.setAccessible(true);
          buffer.append(field.getName());
          buffer.append('=');
          buffer.append(field.get(this));
          buffer.append(',');
        }
      }
      catch (final IllegalAccessException e) {
        e.printStackTrace();
      }
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
        e.printStackTrace();
        return false;
      }
    }
  }
}
