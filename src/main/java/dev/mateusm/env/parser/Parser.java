package dev.mateusm.env.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;

import dev.mateusm.env.exceptions.BadEnvFileException;

public interface Parser {
  public Map<String, String> parseFile(File envFile) throws FileNotFoundException, BadEnvFileException;
}
