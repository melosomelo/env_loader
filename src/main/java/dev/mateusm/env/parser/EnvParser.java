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

    keys.forEach((k, v) -> System.setProperty(k, v));
    return keys;
  }

  private void parseLine(int i, String line, Map<String, String> keys) throws BadEnvFileException {
    StringBuilder carry = new StringBuilder();
    String target = "key";
    String key = "";
    String value = "";
    for (int j = 0; j < line.length(); j++) {
      // Escaped character incoming. Ignore the counterbar, add the character and move
      // on.
      if (line.charAt(j) == '\\' && j < line.length() - 1 && isEscapable(line.charAt(j + 1))) {
        carry.append(line.charAt(j + 1));
        j++;
        continue;
      }

      // We've reached the end of the key, empty the carry.
      if (line.charAt(j) == '=') {
        key = carry.toString().trim();
        carry.setLength(0);
        target = "value";
      } else if (line.charAt(j) == '#') {
        // The rest of the line is a comment. Ignore and don't parse it.
        break;
      } else {
        carry.append(line.charAt(j));
      }
    }

    if (key.length() == 0)
      throw new BadEnvFileException(i, "Missing key");
    if (target == "key")
      throw new BadEnvFileException(i, "Missing '=' symbol");

    value = carry.toString().trim();
    keys.put(key, value);
  }

  private boolean isEscapable(char c) {
    return c == '=' || c == '#';
  }

}
