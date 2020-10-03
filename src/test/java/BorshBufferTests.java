/* This is free and unencumbered software released into the public domain. */

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.near.borshj.BorshBuffer;

public class BorshBufferTests {
  private BorshBuffer buffer;

  @BeforeEach
  void newBuffer() {
    buffer = BorshBuffer.allocate(256);
  }
}
