package dev.mateusm.env;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;

import dev.mateusm.env.exceptions.BadEnvFileException;

public class EnvTest {

  @Test
  public void shouldThrowWhenEnvFileDoesntExist() {
    assertThrows(FileNotFoundException.class, () -> Env.load("somefilenamethatdoesntexist"));
  }

}
