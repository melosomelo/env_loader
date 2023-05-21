package dev.mateusm.env;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.function.Consumer;

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

  // Utility method to encapsulate the logic of creating and deleting temporary
  // files to
  // test parsers. It accepts a runnable that will be the actions the test can
  // perform
  // whilst the file is present.
  public static void createAndThen(String filename, String content, ThrowingConsumer<File> c) throws IOException {
    File file = new File(filename);
    file.createNewFile();
    FileWriter writer = new FileWriter(file);
    writer.write(content);
    writer.close();
    c.accept(file);
    file.delete();
  }

}
