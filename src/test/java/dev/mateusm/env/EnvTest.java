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

  @Test
  public void shouldThrowWhenWhenLineHasValueWithoutKey() throws IOException {
    var file = EnvFileCreator.makeFromContent(".temp.env", "=VALUEWITHOUTKEY");
    assertThrows(BadEnvFileException.class, () -> Env.load(".temp.env"));
    file.delete();
  }

  @Test
  public void shouldThrowWhenFileHasLineOfValueWithoutKeyAndOtherLines() throws IOException {
    var file = EnvFileCreator.makeFromContent(".temp.env",
        "OTHERVALUE=OTHERKEY\nVALUE=KEY\n=VALUEWITHOUTKEY");
    assertThrows(BadEnvFileException.class, () -> Env.load(".temp.env"));
    file.delete();
  }

  @Test
  public void shouldThrowWhenLineHasOnlyKey() throws IOException {
    var file = EnvFileCreator.makeFromContent(".temp.env", "KEYWITHOUTVALUE");
    assertThrows(BadEnvFileException.class, () -> Env.load(".temp.env"));
    file.delete();
  }

  @Test
  public void shouldThrowWhenOneLineHasOnlyKey() throws IOException {
    var file = EnvFileCreator.makeFromContent(".temp.env", "agood=line\nbadline");
    assertThrows(BadEnvFileException.class, () -> Env.load(".temp.env"));
    file.delete();
  }

  @Test
  public void shouldThrowWhenOneLineHasNeitherKeyNorValue() throws IOException {
    var file = EnvFileCreator.makeFromContent(".temp.env", "agood=line\n=\nanother=goodline");
    assertThrows(BadEnvFileException.class, () -> Env.load(".temp.env"));
    file.delete();
  }

  @Test
  public void shouldBeEmptyWithEmptyFile() throws BadEnvFileException, IOException {
    var file = EnvFileCreator.makeFromContent(".temp.env", "");
    Env.load(".temp.env");
    assertTrue(Env.empty());
    file.delete();
  }

  @Test
  public void shouldProperlyParseFileWithSingleLine() throws BadEnvFileException, IOException {
    var file = EnvFileCreator.makeFromContent(".temp.env", "key=value");
    Env.load(".temp.env");
    assertEquals("value", Env.get("key"));
    file.delete();
  }

  @Test
  public void shouldProperlyParseFileWithMultipleLines() throws IOException, BadEnvFileException {
    var file = EnvFileCreator.makeFromContent(".temp.env",
        "key=value\notherkey=othervalue\notherotherkey=otherothervalue");
    Env.load(".temp.env");
    assertEquals("value", Env.get("key"));
    assertEquals("othervalue", Env.get("otherkey"));
    assertEquals("otherothervalue", Env.get("otherotherkey"));
    file.delete();
  }

  @Test
  public void shouldOverwriteValueForRepeatedKey() throws IOException, BadEnvFileException {
    var file = EnvFileCreator.makeFromContent(".temp.env", "key=value\nkey=finalvalue");
    Env.load(".temp.env");
    assertEquals("finalvalue", Env.get("key"));
    file.delete();
  }

  @Test
  public void shouldIgnoreLeadingWhitespacesInLine() throws BadEnvFileException, IOException {
    var file = EnvFileCreator.makeFromContent(".temp.env", "    key=value");
    Env.load(".temp.env");
    assertEquals("value", Env.get("key"));
    file.delete();
  }

}
