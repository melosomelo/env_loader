package dev.mateusm.env;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;

import dev.mateusm.env.exceptions.BadEnvFileException;

public class Env {
  private static Map<String, String> keys = new HashMap<>();

  private Env() {
  }

  public static void load() throws FileNotFoundException, BadEnvFileException {
    load(".env");
  }

  public static void load(String filename) throws FileNotFoundException, BadEnvFileException {
    File file = new File(filename);
    if (!file.exists()) {
      throw new FileNotFoundException();
    }
    parseFile(file);
  }

  public static String get(String key) {
    return keys.get(key);
  }

  public static Optional<String> optionalGet(String key) {
    String val = keys.get(key);
    return Objects.isNull(val) ? Optional.empty() : Optional.of(val);
  }

  public static boolean empty() {
    return keys.isEmpty();
  }

  private static void parseFile(File envFile) throws FileNotFoundException, BadEnvFileException {
    Scanner sc = new Scanner(envFile);
    int i = 0;
    while (sc.hasNextLine()) {
      String line = sc.nextLine();
      parseLine(i, line);
      i++;
    }
  }

  private static void parseLine(int i, String line) throws BadEnvFileException {
    int equalIndex = line.indexOf("=");
    if (equalIndex == -1)
      throw new BadEnvFileException(i, "Missing '=' token");
    String key = line.substring(0, equalIndex).trim();
    String value = line.substring(equalIndex + 1);
    if (equalIndex == 0)
      throw new BadEnvFileException(i, "Missing key for value " + value);
    keys.put(key, value);
  }

}
