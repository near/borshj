/* This is free and unencumbered software released into the public domain. */

package org.near.borshj;

import static java.util.Objects.requireNonNull;

import androidx.annotation.NonNull;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class BorshBuffer {
  protected final @NonNull ByteBuffer buffer;

  protected BorshBuffer(final @NonNull ByteBuffer buffer) {
    this.buffer = requireNonNull(buffer);
    this.buffer.order(ByteOrder.LITTLE_ENDIAN);
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

  public byte readU8() {
    return this.buffer.get();
  }

  public short readU16() {
    return this.buffer.getShort();
  }

  public int readU32() {
    return this.buffer.getInt();
  }

  public long readU64() {
    return this.buffer.getLong();
  }

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

  public double readF32() {
    return this.buffer.getFloat();
  }

  public double readF64() {
    return this.buffer.getDouble();
  }

  public @NonNull String readString() {
    final int length = readU32();
    final byte[] bytes = new byte[length];
    this.buffer.get(bytes);
    return new String(bytes, StandardCharsets.UTF_8);
  }

  public @NonNull byte[] readFixedArray(final int length) {
    if (length < 0) {
      throw new IllegalArgumentException();
    }
    final byte[] bytes = new byte[length];
    this.buffer.get(bytes);
    return bytes;
  }

  public @NonNull Object[] readArray() {
    return null; // TODO
  }

  public @NonNull BorshBuffer writeU8(final int value) {
    return this.writeU8((byte)value);
  }

  public @NonNull BorshBuffer writeU8(final byte value) {
    this.buffer.put(value);
    return this;
  }

  public @NonNull BorshBuffer writeU16(final int value) {
    return this.writeU16((short)value);
  }

  public @NonNull BorshBuffer writeU16(final short value) {
    this.buffer.putShort(value);
    return this;
  }

  public @NonNull BorshBuffer writeU32(final int value) {
    this.buffer.putInt(value);
    return this;
  }

  public @NonNull BorshBuffer writeU64(final long value) {
    this.buffer.putLong(value);
    return this;
  }

  public @NonNull BorshBuffer writeU128(final long value) {
    return this.writeU128(BigInteger.valueOf(value));
  }

  public @NonNull BorshBuffer writeU128(final @NonNull BigInteger value) {
    if (value.signum() == -1) {
      throw new ArithmeticException("integer underflow");
    }
    if (value.bitLength() > 128) {
      throw new ArithmeticException("integer overflow");
    }
    final byte[] bytes = value.toByteArray();
    for (int i = bytes.length - 1; i >= 0; i--) {
      this.buffer.put(bytes[i]);
    }
    for (int i = 0; i < 16 - bytes.length; i++) {
      this.buffer.put((byte)0);
    }
    return this;
  }

  public @NonNull BorshBuffer writeF32(final float value) {
    this.buffer.putFloat(value);
    return this;
  }

  public @NonNull BorshBuffer writeF64(final double value) {
    this.buffer.putDouble(value);
    return this;
  }

  public @NonNull BorshBuffer writeString(final String string) {
    final byte[] bytes = string.getBytes(StandardCharsets.UTF_8);
    this.writeU32(bytes.length);
    this.buffer.put(bytes);
    return this;
  }

  public @NonNull BorshBuffer writeFixedArray(final @NonNull byte[] array) {
    this.buffer.put(array);
    return this;
  }

  public @NonNull BorshBuffer writeArray(final @NonNull Object[] array) {
    // TODO
    return this;
  }
}
