/* This is free and unencumbered software released into the public domain. */

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.near.borshj.BorshReader;

public class BorshReaderTests {
  private ByteArrayInputStream input;
  private BorshReader reader;

  protected void newReader(final byte[] bytes) {
    input = new ByteArrayInputStream(bytes);
    reader = new BorshReader(input);
  }
}
