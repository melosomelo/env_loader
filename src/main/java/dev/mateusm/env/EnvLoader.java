package dev.mateusm.env;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import dev.mateusm.env.exceptions.BadEnvFileException;
import dev.mateusm.env.parser.Parser;

public class EnvLoader {
  private Map<String, String> keys = new HashMap<>();
  private Parser parser;

  public EnvLoader(Parser p) {
    this.parser = p;
  }

  public void load() throws FileNotFoundException, BadEnvFileException {
    load(".env");
  }

  public void load(String filename) throws FileNotFoundException, BadEnvFileException {
    File file = new File(filename);
    if (!file.exists()) {
      throw new FileNotFoundException();
    }
    this.keys = parser.parseFile(file);
  }

  public String get(String key) {
    return keys.get(key);
  }

}
