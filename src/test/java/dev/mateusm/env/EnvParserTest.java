package dev.mateusm.env;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import dev.mateusm.env.exceptions.BadEnvFileException;
import dev.mateusm.env.parser.EnvParser;

public class EnvParserTest {

  private EnvParser parser;

  @Before
  public void setup() {
    parser = new EnvParser();
  }

  @Test
  public void shouldThrowWhenLineHasValueWithoutKey() throws IOException {
    EnvFileCreator.createAndThen(".temp.env", "=value", (file) -> {
      assertThrows(BadEnvFileException.class, () -> parser.parseFile(file));
    });
  }

  @Test
  public void shouldThrownWhenLineHasValueWithoutKeyAndOtherNormalLines() throws IOException {
    EnvFileCreator.createAndThen(".temp.env", "=value\nok=itsok\notherkey=othervalue", (file) -> {
      assertThrows(BadEnvFileException.class, () -> parser.parseFile(file));
    });
  }

  @Test
  public void shouldThrowWhenLineHasOnlyKey() throws IOException {
    EnvFileCreator.createAndThen(".temp.env", "KEYWITHOUTVALUE", (file) -> {
      assertThrows(BadEnvFileException.class, () -> parser.parseFile(file));
    });
  }

  @Test
  public void shouldThrowWhenLineHasOnlyKeyAndOtherLines() throws IOException {
    EnvFileCreator.createAndThen(".temp.env", "key=withvalue\nKEYWITHOUTVALUE\notherkey=othervalue", (file) -> {
      assertThrows(BadEnvFileException.class, () -> parser.parseFile(file));
    });
  }

  @Test
  public void shouldThrowWhenOneLineHasNeitherKeyNorValue() throws IOException {
    EnvFileCreator.createAndThen(".temp.env", "agood=line\n=\nanother=goodline", (file) -> {
      assertThrows(BadEnvFileException.class, () -> parser.parseFile(file));
    });
  }

  @Test
  public void shouldProperlyParseFileWithASingleLine() throws IOException, FileNotFoundException, BadEnvFileException {
    EnvFileCreator.createAndThen(".temp.env", "key=value", (file) -> {
      var keys = parser.parseFile(file);
      assertEquals("value", keys.get("key"));
    });
  }

  @Test
  public void shouldProperlyParseFileWithMultipleLines() throws IOException {
    EnvFileCreator.createAndThen(".temp.env", "one=two\nthree=four\nfive=six", (file) -> {
      var keys = parser.parseFile(file);
      assertEquals("two", keys.get("one"));
      assertEquals("four", keys.get("three"));
      assertEquals("six", keys.get("five"));
    });
  }

  @Test
  public void shouldBeEmptyWhenEnvFileIsEmpty() throws IOException {
    EnvFileCreator.createAndThen(".temp.env", "", (file) -> {
      var keys = parser.parseFile(file);
      assertTrue(keys.isEmpty());
    });
  }

  @Test
  public void shouldOverwriteValueForRepeatedKey() throws IOException {
    EnvFileCreator.createAndThen(".temp.env", "key=value\nkey=newvalue", (file) -> {
      var keys = parser.parseFile(file);
      assertEquals("newvalue", keys.get("key"));
    });
  }

  @Test
  public void shouldIgnoreLeadingWhitespacesInLine() throws IOException {
    EnvFileCreator.createAndThen(".temp.env", "     key=value", (file) -> {
      var keys = parser.parseFile(file);
      assertEquals("value", keys.get("key"));
    });
  }

  @Test
  public void shouldNotParseLineWithLeandingCommentCharacter() throws IOException {
    EnvFileCreator.createAndThen(".temp.env", "   #key=value", (file) -> {
      var keys = parser.parseFile(file);
      assertNull(keys.get("key"));
      assertNull(keys.get("#key"));
    });
  }

  @Test
  public void shouldBeEmptyWithFileWithJustComments() throws IOException {
    EnvFileCreator.createAndThen(".temp.env", "#key=value\n#key=value\n#key=value", (file) -> {
      var keys = parser.parseFile(file);
      assertTrue(keys.isEmpty());
    });
  }

  @Test
  public void shouldIgnoreCommentsAtEndOfLine() throws IOException {
    EnvFileCreator.createAndThen(".temp.env", "key=value #this is another comment", (file) -> {
      var keys = parser.parseFile(file);
      assertEquals("value", keys.get("key"));
    });
  }

  @Test
  public void shouldThrowWhenCommentBreaksSyntax() throws IOException {
    EnvFileCreator.createAndThen(".temp.env", "key#=value", (file) -> {
      assertThrows(BadEnvFileException.class, () -> parser.parseFile(file));
    });
  }

  @Test
  public void shouldBeEmptyWithFileWithAllEmptyLines() throws IOException {
    EnvFileCreator.createAndThen(".temp.env", "  \n   \n  \n", (file) -> {
      var keys = parser.parseFile(file);
      assertTrue(keys.isEmpty());
    });
  }

  @Test
  public void shouldReturnEmptyStringForStringWithoutValue() throws IOException {
    EnvFileCreator.createAndThen(".temp.env", "key=\nanotherkey=anothervalue", (file) -> {
      var keys = parser.parseFile(file);
      assertEquals("", keys.get("key"));
    });
  }

  @Test
  public void shouldCorrectlyParseEscapedHashSymbol() throws IOException {
    EnvFileCreator.createAndThen(".temp.env", "key=\\#value  ", (file) -> {
      var keys = parser.parseFile(file);
      assertEquals("#value", keys.get("key"));
    });
  }
}
