/* This is free and unencumbered software released into the public domain. */

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigInteger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.near.borshj.BorshBuffer;

public class BorshBufferTests {
  private BorshBuffer buffer;

  @BeforeEach
  void newBuffer() {
    buffer = BorshBuffer.allocate(256);
  }

  @Test
  void readU8() {
    buffer = BorshBuffer.wrap(new byte[] {0x42});
    assertEquals(0x42, buffer.readU8());
  }

  @Test
  void readU16() {
    buffer = BorshBuffer.wrap(new byte[] {0x11, 0x00});
    assertEquals(0x0011, buffer.readU16());
  }

  @Test
  void readU32() {
    buffer = BorshBuffer.wrap(new byte[] {0x33, 0x22, 0x11, 0x00});
    assertEquals(0x00112233, buffer.readU32());
  }

  @Test
  void readU64() {
    buffer = BorshBuffer.wrap(new byte[] {0x77, 0x66, 0x55, 0x44, 0x33, 0x22, 0x11, 0x00});
    assertEquals(0x0011223344556677L, buffer.readU64());
  }

  @Test
  void readU128() {
    final byte[] input = new byte[] {
      0x77, 0x66, 0x55, 0x44, 0x33, 0x22, 0x11, 0x00,
      0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
    };
    buffer = BorshBuffer.wrap(input);
    assertEquals(BigInteger.valueOf(0x0011223344556677L), buffer.readU128());
  }

  @Test
  void writeU8() {
    buffer.writeU8(0x42);
    final byte[] expected = new byte[] {0x42};
    final byte[] actual = buffer.toByteArray();
    assertEquals(expected.length, actual.length);
    assertArrayEquals(expected, actual);
  }

  @Test
  void writeU16() {
    buffer.writeU16(0x0011);
    final byte[] expected = new byte[] {0x11, 0x00};
    final byte[] actual = buffer.toByteArray();
    assertEquals(expected.length, actual.length);
    assertArrayEquals(expected, actual);
  }

  @Test
  void writeU32() {
    buffer.writeU32(0x00112233);
    final byte[] expected = new byte[] {0x33, 0x22, 0x11, 0x00};
    final byte[] actual = buffer.toByteArray();
    assertEquals(expected.length, actual.length);
    assertArrayEquals(expected, actual);
  }

  @Test
  void writeU64() {
    buffer.writeU64(0x0011223344556677L);
    final byte[] expected = new byte[] {
      0x77, 0x66, 0x55, 0x44, 0x33, 0x22, 0x11, 0x00,
    };
    final byte[] actual = buffer.toByteArray();
    assertEquals(expected.length, actual.length);
    assertArrayEquals(expected, actual);
  }

  @Test
  void writeU128() {
    buffer.writeU128(0x0011223344556677L);
    final byte[] expected = new byte[] {
      0x77, 0x66, 0x55, 0x44, 0x33, 0x22, 0x11, 0x00,
      0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
    };
    final byte[] actual = buffer.toByteArray();
    assertEquals(expected.length, actual.length);
    assertArrayEquals(expected, actual);
  }

  @Test
  void writeString() {
    buffer.writeString("Borsh");
    final byte[] expected = new byte[] {5, 0, 0, 0, 'B', 'o', 'r', 's', 'h'};
    final byte[] actual = buffer.toByteArray();
    assertEquals(expected.length, actual.length);
    assertArrayEquals(expected, actual);
  }

  @Test
  void writeFixedArray() {
    buffer.writeFixedArray(new byte[]{1, 2, 3, 4, 5});
    final byte[] expected = new byte[]{1, 2, 3, 4, 5};
    final byte[] actual = buffer.toByteArray();
    assertEquals(expected.length, actual.length);
    assertArrayEquals(expected, actual);
  }

  @Test
  void writeArray() {
    // TODO
  }
}
