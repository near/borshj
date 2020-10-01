/* This is free and unencumbered software released into the public domain. */

package org.near.borshj;

import java.io.OutputStream;
import java.math.BigInteger;

import androidx.annotation.NonNull;

public class BorshWriter {
  private final OutputStream stream;

  public BorshWriter(final @NonNull OutputStream stream) {
    this.stream = stream;
  }

  public void writeU8(final byte value) {
    // TODO
  }

  public void writeU32(final int value) {
    // TODO
  }

  public void writeU64(final long value) {
    // TODO
  }

  public void writeU128(final @NonNull BigInteger value) {
    // TODO
  }

  public void writeString(final @NonNull String string) {
    // TODO
  }

  public void writeFixedArray(final @NonNull byte[] array) {
    // TODO
  }

  public void writeArray(final @NonNull Object[] array) {
    // TODO
  }
}
