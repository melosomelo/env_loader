package dev.mateusm.env;

import static org.junit.Assert.assertThrows;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;

import dev.mateusm.env.exceptions.BadEnvFileException;

public class EnvTest {
  @Test
  public void shouldThrowWhenEnvFileDoesntExistWithParameter() {
    assertThrows(FileNotFoundException.class, () -> Env.load("somefilenamethatdoesntexist"));
  }

  @Test
  public void shouldThrowWhenWhenFileHasBadSyntax1() throws IOException {
    var file = EnvFileCreator.makeFromContent(".temp.env", "=VALUEWITHOUTKEY");
    assertThrows(BadEnvFileException.class, () -> Env.load("temp.env"));
    file.delete();
  }

}