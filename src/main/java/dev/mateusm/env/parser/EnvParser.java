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
    String lineContent = extractContent(line);
    if (lineContent.length() == 0)
      return;

    int equalIndex = lineContent.indexOf("=");
    if (equalIndex == -1)
      throw new BadEnvFileException(i, "Missing '=' token");
    String value = lineContent.substring(equalIndex + 1).trim();
    if (equalIndex == 0)
      throw new BadEnvFileException(i, "Missing key for value " + value);
    String key = lineContent.substring(0, equalIndex).trim();
    keys.put(key, value);
  }

  private String extractContent(String line) {
    StringBuilder builder = new StringBuilder(line);
    for (int i = 0; i < builder.length(); i++) {
      if (builder.charAt(i) == '#') {
        // This is an escaped hash symbol. Continue parsing.
        if (i > 0 && builder.charAt(i - 1) == '\\') {
          // Delete the counterbar, it serves only to escape the hash symbol.
          builder.deleteCharAt(i - 1);
        } else {
          // This is an unescaped hash symbol. The rest of the string is a comment.
          return builder.substring(0, i);
        }
      }
    }
    return builder.toString().trim();
  }

}
