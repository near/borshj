/* This is free and unencumbered software released into the public domain. */

package org.near.borshj;

import androidx.annotation.NonNull;
import java.math.BigInteger;
import java.util.Optional;

public interface BorshInput {
  public byte readU8();

  public short readU16();

  public int readU32();

  public long readU64();

  public @NonNull BigInteger readU128();

  public float readF32();

  public double readF64();

  public @NonNull String readString();

  public @NonNull byte[] readFixedArray(final int length);

  public @NonNull Object[] readArray();

  public <T> @NonNull Optional<T> readOptional();

  public <T> @NonNull Optional<T> readOptional(final @NonNull Class klass);
}
