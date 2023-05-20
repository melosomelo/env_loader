package dev.mateusm.env;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class EnvFileCreator {

  public static void makeFromValues(String filename, String... keysAndValues) {
    StringBuilder strBuilder = new StringBuilder();
    System.out.println(keysAndValues.length);
  }

  public static File makeFromContent(String filename, String content) throws IOException {
    File file = new File(filename);
    file.createNewFile();
    FileWriter writer = new FileWriter(file);
    writer.write(content);
    writer.close();
    return file;
  }

}
