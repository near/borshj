/* This is free and unencumbered software released into the public domain. */

package org.near.borshj;

import static java.util.Objects.requireNonNull;

import androidx.annotation.NonNull;
import java.nio.ByteBuffer;

public class BorshBuffer {
  private final @NonNull ByteBuffer buffer;

  public BorshBuffer(final @NonNull ByteBuffer buffer) {
    this.buffer = requireNonNull(buffer);
  }

  public static @NonNull BorshBuffer allocate(final int capacity) {
    return new BorshBuffer(ByteBuffer.allocate(capacity));
  }

  public static @NonNull BorshBuffer allocateDirect(final int capacity) {
    return new BorshBuffer(ByteBuffer.allocateDirect(capacity));
  }
}
