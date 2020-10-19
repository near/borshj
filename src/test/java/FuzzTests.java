/* This is free and unencumbered software released into the public domain. */

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import net.sf.cglib.beans.BeanGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.near.borshj.Borsh;

public class FuzzTests {
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

  Object newPersonBean() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
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
    return bean;
  }

  @Test
  void testBeanGenerator() throws Exception {
    final Object bean = newPersonBean();
    assertEquals(bean, Borsh.deserialize(Borsh.serialize(bean), bean.getClass()));
  }
}
