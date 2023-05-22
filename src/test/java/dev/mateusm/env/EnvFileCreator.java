package dev.mateusm.env;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class EnvFileCreator {
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
