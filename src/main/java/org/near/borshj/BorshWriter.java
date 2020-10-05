/* This is free and unencumbered software released into the public domain. */

package org.near.borshj;

import static java.util.Objects.requireNonNull;

import androidx.annotation.NonNull;
import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

public class BorshWriter implements Closeable, Flushable {
  private final OutputStream stream;
  private final BorshBuffer buffer;

  public BorshWriter(final @NonNull OutputStream stream) {
    this.stream = requireNonNull(stream);
    this.buffer = BorshBuffer.allocate(16);
  }

  @Override
  public void close() throws IOException {
    this.stream.close();
  }

  @Override
  public void flush() throws IOException {
    this.stream.flush();
  }

  public void writeU8(final byte value) {
    try {
      this.stream.write(value);
    }
    catch (final IOException error) {
      throw new RuntimeException(error);
    }
  }

  public void writeU16(final short value) {
    this.buffer.writeU16(value);
    this.writeAndResetBuffer();
  }

  public void writeU32(final int value) {
    this.buffer.writeU32(value);
    this.writeAndResetBuffer();
  }

  public void writeU64(final long value) {
    this.buffer.writeU64(value);
    this.writeAndResetBuffer();
  }

  public void writeU128(final @NonNull BigInteger value) {
    this.buffer.writeU128(value);
    this.writeAndResetBuffer();
  }

  public void writeString(final @NonNull String string) {
    final byte[] bytes = string.getBytes(StandardCharsets.UTF_8);
    this.writeU32(bytes.length);
    this.writeFixedArray(bytes);
  }

  public void writeFixedArray(final @NonNull byte[] array) {
    try {
      this.stream.write(array);
    }
    catch (final IOException error) {
      throw new RuntimeException(error);
    }
  }

  public void writeArray(final @NonNull Object[] array) {
    // TODO
  }

  protected void writeAndResetBuffer() {
    this.writeBuffer(this.buffer);
    this.buffer.reset();
  }

  protected void writeBuffer(final @NonNull BorshBuffer buffer) {
    try {
      this.stream.write(buffer.toByteArray());  // TODO: optimize
    }
    catch (final IOException error) {
      throw new RuntimeException(error);
    }
  }
}
