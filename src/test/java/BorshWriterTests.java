/* This is free and unencumbered software released into the public domain. */

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.near.borshj.BorshWriter;

public class BorshWriterTests {
  private ByteArrayOutputStream output;
  private BorshWriter writer;

  @BeforeEach
  void newWriter() {
    output = new ByteArrayOutputStream();
    writer = new BorshWriter(output);
  }

  @Test
  void constructWithNull() {
    assertThrows(NullPointerException.class, () -> new BorshWriter(null));
  }

  @Test
  void captureOutput() {
    writer.writeString("Borsh");
    assertArrayEquals(new byte[] {5, 0, 0, 0, 'B', 'o', 'r', 's', 'h'}, output.toByteArray());
  }
}
