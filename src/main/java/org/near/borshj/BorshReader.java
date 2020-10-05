/* This is free and unencumbered software released into the public domain. */

package org.near.borshj;

import static java.util.Objects.requireNonNull;

import androidx.annotation.NonNull;
import java.io.Closeable;
import java.io.EOFException;
import java.io.InputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

public class BorshReader implements Closeable {
  private final InputStream stream;
  private final BorshBuffer buffer;

  public BorshReader(final @NonNull InputStream stream) {
    this.stream = requireNonNull(stream);
    this.buffer = BorshBuffer.allocate(16);
  }

  @Override
  public void close() throws IOException {
    this.stream.close();
  }

  public byte readU8() {
    try {
      final int result = this.stream.read();
      if (result == -1) {
        throw new EOFException();
      }
      return (byte)result;
    }
    catch (final IOException error) {
      throw new RuntimeException(error);
    }
  }

  public int readU32() {
    this.readIntoBuffer(4);
    return this.buffer.readU32();
  }

  public long readU64() {
    this.readIntoBuffer(8);
    return this.buffer.readU64();
  }

  public @NonNull BigInteger readU128() {
    this.readIntoBuffer(16);
    return this.buffer.readU128();
  }

  public @NonNull String readString() {
    final int length = this.readU32();
    final byte[] bytes = new byte[length];
    this.readIntoBuffer(bytes, length);
    return new String(bytes, StandardCharsets.UTF_8);
  }

  public @NonNull byte[] readFixedArray(final int length) {
    if (length < 0) {
      throw new IllegalArgumentException();
    }
    final byte[] bytes = new byte[length];
    this.readIntoBuffer(bytes, length);
    return bytes;
  }

  public @NonNull Object[] readArray() {
    return null; // TODO
  }

  protected void readIntoBuffer(final int length) {
    this.buffer.reset();
    this.readIntoBuffer(this.buffer.array(), length);
  }

  protected void readIntoBuffer(final @NonNull byte[] buffer, final int length) {
    assert(length <= buffer.length);
    try {
      final int bytesRead = this.stream.read(buffer, 0, length);
      if (bytesRead == -1 || bytesRead < length) {
        throw new EOFException();
      }
    }
    catch (final IOException error) {
      throw new RuntimeException(error);
    }
  }
}
