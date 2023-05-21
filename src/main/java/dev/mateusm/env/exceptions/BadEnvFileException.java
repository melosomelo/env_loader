package dev.mateusm.env.exceptions;

public class BadEnvFileException extends Exception {
  public BadEnvFileException() {

  }

  public BadEnvFileException(String msg) {
    super(msg);
  }

  public BadEnvFileException(int i, String msg) {
    super("Error at line " + i + ". " + msg);
  }

}
