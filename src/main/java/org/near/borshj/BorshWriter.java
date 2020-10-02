/* This is free and unencumbered software released into the public domain. */

package org.near.borshj;

import androidx.annotation.NonNull;
import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;

public class BorshWriter implements Closeable, Flushable {
  private final OutputStream stream;

  public BorshWriter(final @NonNull OutputStream stream) {
    this.stream = stream;
  }

  @Override
  public void close() throws IOException {
    this.stream.close();
  }

  @Override
  public void flush() throws IOException {
    this.stream.flush();
  }

  public void writeU8(final int value) {
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
