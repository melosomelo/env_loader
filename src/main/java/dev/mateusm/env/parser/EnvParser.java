package dev.mateusm.env.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import dev.mateusm.env.exceptions.BadEnvFileException;

public class EnvParser implements Parser {

  @Override
  public Map<String, String> parseFile(File envFile) throws FileNotFoundException, BadEnvFileException {
    Map<String, String> keys = new HashMap<>();

    Scanner sc = new Scanner(envFile);
    int i = 0;
    while (sc.hasNextLine()) {
      parseLine(i, sc.nextLine().trim(), keys);
      i++;
    }

    sc.close();
    return keys;
  }

  private void parseLine(int i, String line, Map<String, String> keys) throws BadEnvFileException {
    if (line.length() == 0)
      return;
    String lineWithoutComment = removeComments(line);
    if (lineWithoutComment.length() == 0)
      return;
    int equalSymbolIndex = getFirstIndexForUnescapedChar('=', lineWithoutComment);
    if (equalSymbolIndex == -1)
      throw new BadEnvFileException(i, "Missing '=' token");
    String value = removeCounterbarsForEscapedCharacters(lineWithoutComment.substring(equalSymbolIndex + 1).trim());
    if (equalSymbolIndex == 0)
      throw new BadEnvFileException(i, "Missing key for value " + value);
    String key = removeCounterbarsForEscapedCharacters(lineWithoutComment.substring(0, equalSymbolIndex).trim());
    keys.put(key, value);
  }

  private String removeComments(String line) {
    int commentBeginning = getFirstIndexForUnescapedChar('#', line);
    if (commentBeginning == -1)
      return line;
    return line.substring(0, commentBeginning).trim();
  }

  private int getFirstIndexForUnescapedChar(char c, String line) {
    for (int i = 0; i < line.length(); i++) {
      // It's unescaped if it's either first character on the line
      // or if the character before it isn't a counterbar.
      if (line.charAt(i) == c && (i == 0 || (i > 0 && line.charAt(i - 1) != '\\'))) {
        return i;
      }
    }
    return -1;
  }

  private String removeCounterbarsForEscapedCharacters(String line) {
    StringBuilder builder = new StringBuilder(line);
    for (int i = 0; i < builder.length(); i++) {
      if (isEscapable(builder.charAt(i)) && (i > 0 && builder.charAt(i - 1) == '\\')) {
        builder.deleteCharAt(i - 1);
      }
    }
    return builder.toString();
  }

  private boolean isEscapable(char c) {
    return c == '=' || c == '#';
  }

}
