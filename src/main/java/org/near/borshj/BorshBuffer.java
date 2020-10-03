/* This is free and unencumbered software released into the public domain. */

package org.near.borshj;

import static java.util.Objects.requireNonNull;

import androidx.annotation.NonNull;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
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
    return Arrays.copyOfRange(
      this.buffer.array(),
      arrayOffset + this.buffer.position(),
      arrayOffset + this.buffer.limit());
  }
}
