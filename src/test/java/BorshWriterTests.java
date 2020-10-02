/* This is free and unencumbered software released into the public domain. */

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import org.junit.jupiter.api.Test;
import org.near.borshj.BorshWriter;

public class BorshWriterTests {
  @Test
  void test_writeU8() {
    final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    final BorshWriter out = new BorshWriter(buffer);
    out.writeU8(0);
    //assertEquals(1, buffer.toByteArray().length);  // TODO
  }
}
