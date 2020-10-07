/* This is free and unencumbered software released into the public domain. */

package org.near.borshj;

import static java.util.Objects.requireNonNull;

import androidx.annotation.NonNull;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.util.Optional;

public interface Borsh {
  public static @NonNull byte[] serialize(final @NonNull Object object) {
    requireNonNull(object);
    final BorshBuffer buffer = BorshBuffer.allocate(4096);
    buffer.write(object);
    return buffer.toByteArray();
  }

  public static @NonNull <T> T deserialize(final @NonNull byte[] bytes, final @NonNull Class klass) {
    return deserialize(BorshBuffer.wrap(requireNonNull(bytes)), klass);
  }

  public static @NonNull <T> T deserialize(final @NonNull BorshBuffer buffer, final @NonNull Class klass) {
    requireNonNull(buffer);
    requireNonNull(klass);
    try {
      final Object object = klass.getConstructor().newInstance();
      for (final Field field : klass.getDeclaredFields()) {
        deserializeField(field, object, buffer);
      }
      return (T)object;
    }
    catch (NoSuchMethodException error) {
      throw new RuntimeException(error);
    }
    catch (InstantiationException error) {
      throw new RuntimeException(error);
    }
    catch (IllegalAccessException error) {
      throw new RuntimeException(error);
    }
    catch (InvocationTargetException error) {
      throw new RuntimeException(error);
    }
  }

  public static void deserializeField(final @NonNull Field field, final @NonNull Object object, final @NonNull BorshBuffer buffer)
      throws IllegalAccessException {
    final Class fieldType = field.getType();
    if (fieldType == byte.class || fieldType == Byte.class) {
      field.setByte(object, buffer.readU8());
    }
    else if (fieldType == short.class || fieldType == Short.class) {
      field.setShort(object, buffer.readU16());
    }
    else if (fieldType == int.class || fieldType == Integer.class) {
      field.setInt(object, buffer.readU32());
    }
    else if (fieldType == long.class || fieldType == Long.class) {
      field.setLong(object, buffer.readU64());
    }
    else if (fieldType == float.class || fieldType == Float.class) {
      field.setFloat(object, buffer.readF32());
    }
    else if (fieldType == double.class || fieldType == Double.class) {
      field.setDouble(object, buffer.readF64());
    }
    else if (fieldType == BigInteger.class) {
      field.set(object, buffer.readU128());
    }
    else if (fieldType == String.class) {
      field.set(object, buffer.readString());
    }
  }
}
