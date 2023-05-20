package dev.mateusm.env;

import java.io.File;
import java.io.FileNotFoundException;

public class Env {
  public static void load() throws FileNotFoundException {
    load(".env");
  }

  public static void load(String filename) throws FileNotFoundException {
    File file = new File(filename);
    if (!file.exists()) {
      throw new FileNotFoundException();
    }
  }

}
