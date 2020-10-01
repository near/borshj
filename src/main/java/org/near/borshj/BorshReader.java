/* This is free and unencumbered software released into the public domain. */

package org.near.borshj;

import java.io.InputStream;
import java.math.BigInteger;

import androidx.annotation.NonNull;

public class BorshReader {
  private final InputStream stream;

  public BorshReader(final @NonNull InputStream stream) {
    this.stream = stream;
  }

  public byte readU8() {
    return 0; // TODO
  }

  public int readU32() {
    return 0; // TODO
  }

  public long readU64() {
    return 0; // TODO
  }

  public @NonNull BigInteger readU128() {
    return null; // TODO
  }

  public @NonNull String readString() {
    return null; // TODO
  }

  public @NonNull byte[] readFixedArray() {
    return null; // TODO
  }

  public @NonNull Object[] readArray() {
    return null; // TODO
  }
}
