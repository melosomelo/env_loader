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
      parseLine(i, sc.nextLine(), keys);
      i++;
    }

    sc.close();
    return keys;
  }

  private void parseLine(int i, String line, Map<String, String> keys) throws BadEnvFileException {
    int equalIndex = line.indexOf("=");
    if (equalIndex == -1)
      throw new BadEnvFileException(i, "Missing '=' token");
    String value = line.substring(equalIndex + 1);
    if (equalIndex == 0)
      throw new BadEnvFileException(i, "Missing key for value " + value);
    String key = line.substring(0, equalIndex).trim();
    keys.put(key, value);
  }

}
