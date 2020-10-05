/* This is free and unencumbered software released into the public domain. */

package org.near.borshj;

import static java.util.Objects.requireNonNull;

import androidx.annotation.NonNull;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.math.BigInteger;
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

  public @NonNull byte[] toByteArray() {
    assert(this.buffer.hasArray());
    final int arrayOffset = this.buffer.arrayOffset();
    return Arrays.copyOfRange(this.buffer.array(),
      arrayOffset, arrayOffset + this.buffer.position());
  }

  public @NonNull BorshBuffer writeU8(final int value) {
    return this.writeU8((byte)value);
  }

  public @NonNull BorshBuffer writeU8(final byte value) {
    this.buffer.put(value);
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

  public @NonNull BorshBuffer writeString(final String string) {
    final byte[] bytes = string.getBytes(StandardCharsets.UTF_8);
    this.writeU32(bytes.length);
    this.buffer.put(bytes);
    return this;
  }
}
