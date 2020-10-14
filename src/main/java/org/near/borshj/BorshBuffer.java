/* This is free and unencumbered software released into the public domain. */

package org.near.borshj;

import static java.util.Objects.requireNonNull;

import androidx.annotation.NonNull;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Optional;

public class BorshBuffer implements BorshInput, BorshOutput<BorshBuffer> {
  protected final @NonNull ByteBuffer buffer;

  protected BorshBuffer(final @NonNull ByteBuffer buffer) {
    this.buffer = requireNonNull(buffer);
    this.buffer.order(ByteOrder.LITTLE_ENDIAN);
    this.buffer.mark();
  }

  public static @NonNull BorshBuffer allocate(final int capacity) {
    return new BorshBuffer(ByteBuffer.allocate(capacity));
  }

  public static @NonNull BorshBuffer allocateDirect(final int capacity) {
    return new BorshBuffer(ByteBuffer.allocateDirect(capacity));
  }

  public static @NonNull BorshBuffer wrap(final byte[] array) {
    return new BorshBuffer(ByteBuffer.wrap(array));
  }

  public @NonNull byte[] toByteArray() {
    assert(this.buffer.hasArray());
    final int arrayOffset = this.buffer.arrayOffset();
    return Arrays.copyOfRange(this.buffer.array(),
      arrayOffset, arrayOffset + this.buffer.position());
  }

  public int capacity() {
    return this.buffer.capacity();
  }

  public @NonNull BorshBuffer reset() {
    this.buffer.reset();
    return this;
  }

  public <T> T read(final @NonNull Class klass) {
    try {
      if (klass == Byte.class || klass == byte.class) {
        return (T)Byte.valueOf(this.readU8());
      }
      else if (klass == Short.class || klass == short.class) {
        return (T)Short.valueOf(this.readU16());
      }
      else if (klass == Integer.class || klass == int.class) {
        return (T)Integer.valueOf(this.readU32());
      }
      else if (klass == Long.class || klass == long.class) {
        return (T)Long.valueOf(this.readU64());
      }
      else if (klass == BigInteger.class) {
        return (T)this.readU128();
      }
      else if (klass == Float.class || klass == float.class) {
        return (T)Float.valueOf(this.readF32());
      }
      else if (klass == Double.class || klass == double.class) {
        return (T)Double.valueOf(this.readF64());
      }
      else if (klass == String.class) {
        return (T)this.readString();
      }
      else if (klass == Optional.class) {
        return (T)this.readOptional();
      }
      else { // TODO: check if implements Borsh
        final Object object = klass.getConstructor().newInstance();
        for (final Field field : klass.getDeclaredFields()) {
          this.readField(field, object);
        }
        return (T)object;
      }
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

  protected void readField(final @NonNull Field field, final @NonNull Object object)
      throws IllegalAccessException {
    final Class fieldClass = field.getType();
    if (fieldClass == Optional.class) {
      final Type fieldType = field.getGenericType();
      if (!(fieldType instanceof ParameterizedType)) {
        throw new AssertionError("unsupported Optional type");
      }
      final Type[] optionalArgs = ((ParameterizedType)fieldType).getActualTypeArguments();
      assert(optionalArgs.length == 1);
      final Class optionalClass = (Class)optionalArgs[0];
      field.set(object, this.readOptional(optionalClass));
    }
    else {
      field.set(object, this.read(field.getType()));
    }
  }

  @Override
  public byte readU8() {
    return this.buffer.get();
  }

  @Override
  public short readU16() {
    return this.buffer.getShort();
  }

  @Override
  public int readU32() {
    return this.buffer.getInt();
  }

  @Override
  public long readU64() {
    return this.buffer.getLong();
  }

  @Override
  public @NonNull BigInteger readU128() {
    final byte[] bytes = new byte[16];
    this.buffer.get(bytes);
    for (int i = 0; i < 8; i++) {
      final byte a = bytes[i];
      final byte b = bytes[15 - i];
      bytes[i] = b;
      bytes[15 - i] = a;
    }
    return new BigInteger(bytes);
  }

  @Override
  public float readF32() {
    return this.buffer.getFloat();
  }

  @Override
  public double readF64() {
    return this.buffer.getDouble();
  }

  @Override
  public @NonNull String readString() {
    final int length = this.readU32();
    final byte[] bytes = new byte[length];
    this.buffer.get(bytes);
    return new String(bytes, StandardCharsets.UTF_8);
  }

  @Override
  public @NonNull byte[] readFixedArray(final int length) {
    if (length < 0) {
      throw new IllegalArgumentException();
    }
    final byte[] bytes = new byte[length];
    this.buffer.get(bytes);
    return bytes;
  }

  @Override
  public @NonNull Object[] readArray() {
    return null; // TODO
  }

  @Override
  public <T> @NonNull Optional<T> readOptional() {
    final boolean isPresent = (this.readU8() != 0);
    if (!isPresent) {
      return (Optional<T>)Optional.empty();
    }
    throw new AssertionError("Optional type has been erased and cannot be reconstructed");
  }

  @Override
  public <T> @NonNull Optional<T> readOptional(final @NonNull Class klass) {
    final boolean isPresent = (this.readU8() != 0);
    return isPresent ? Optional.of(this.read(klass)) : Optional.empty();
  }

  @Override
  public @NonNull BorshBuffer writeU16(final short value) {
    this.buffer.putShort(value);
    return this;
  }

  @Override
  public @NonNull BorshBuffer writeU32(final int value) {
    this.buffer.putInt(value);
    return this;
  }

  @Override
  public @NonNull BorshBuffer writeU64(final long value) {
    this.buffer.putLong(value);
    return this;
  }

  @Override
  public @NonNull BorshBuffer writeF32(final float value) {
    this.buffer.putFloat(value);
    return this;
  }

  @Override
  public @NonNull BorshBuffer writeF64(final double value) {
    this.buffer.putDouble(value);
    return this;
  }

  @Override
  public @NonNull BorshBuffer write(final @NonNull byte[] bytes) {
    this.buffer.put(bytes);
    return this;
  }

  @Override
  public @NonNull BorshBuffer write(final byte b) {
    this.buffer.put(b);
    return this;
  }

  protected byte[] array() {
    assert(this.buffer.hasArray());
    return this.buffer.array();
  }
}
