package dev.mateusm.env;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import dev.mateusm.env.parser.EnvParser;

public class EnvTest {

  @Test
  public void shouldSetEnvironmentVariablesForFileWithDefaultName() throws IOException {
    EnvFileCreator.createAndThen(".env", "key=value\notherkey=othervalue", (file) -> {
      EnvLoader loader = new EnvLoader(new EnvParser());
      loader.load();
      assertEquals("value", loader.get("key"));
      assertEquals("othervalue", loader.get("otherkey"));
    });
  }

  @Test
  public void shouldSetEnvironmentVariablesForFileWithProvidedName() throws IOException {
    EnvFileCreator.createAndThen("prod.env", "key=value\notherkey=othervalue", (file) -> {
      EnvLoader loader = new EnvLoader(new EnvParser());
      loader.load("prod.env");
      assertEquals("value", loader.get("key"));
      assertEquals("othervalue", loader.get("otherkey"));
    });
  }

}
