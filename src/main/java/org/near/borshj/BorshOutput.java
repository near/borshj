/* This is free and unencumbered software released into the public domain. */

package org.near.borshj;

import androidx.annotation.NonNull;
import java.math.BigInteger;
import java.util.Optional;

public interface BorshOutput {
  default public @NonNull BorshBuffer writeU8(final int value) {
    return this.writeU8((byte)value);
  }

  public @NonNull BorshBuffer writeU8(final byte value);

  default public @NonNull BorshBuffer writeU16(final int value) {
    return this.writeU16((short)value);
  }

  public @NonNull BorshBuffer writeU16(final short value);

  public @NonNull BorshBuffer writeU32(final int value);

  public @NonNull BorshBuffer writeU64(final long value);

  default public @NonNull BorshBuffer writeU128(final long value) {
    return this.writeU128(BigInteger.valueOf(value));
  }

  public @NonNull BorshBuffer writeU128(final @NonNull BigInteger value);

  public @NonNull BorshBuffer writeF32(final float value);

  public @NonNull BorshBuffer writeF64(final double value);

  public @NonNull BorshBuffer writeString(final String string);

  public @NonNull BorshBuffer writeFixedArray(final @NonNull byte[] array);

  public @NonNull BorshBuffer writeArray(final @NonNull Object[] array);

  public @NonNull <T> BorshBuffer writeOptional(final @NonNull Optional<T> optional);
}
