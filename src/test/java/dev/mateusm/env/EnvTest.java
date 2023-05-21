package dev.mateusm.env;

import static org.junit.Assert.assertThrows;
import java.io.FileNotFoundException;
import org.junit.Test;

public class EnvTest {

  @Test
  public void shouldThrowWhenEnvFileDoesntExist() {
    assertThrows(FileNotFoundException.class, () -> Env.load("somefilenamethatdoesntexist"));
  }

}
