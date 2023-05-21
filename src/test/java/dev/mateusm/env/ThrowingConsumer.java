package dev.mateusm.env;

import java.util.function.Consumer;

// Used in tests so that lambda functions don't have to
// deal with try catch within them.
@FunctionalInterface
public interface ThrowingConsumer<T> extends Consumer<T> {
  @Override
  default void accept(T elem) {
    try {
      acceptThrows(elem);
    } catch (final Exception e) {
    }
  }

  void acceptThrows(T elem) throws Exception;
}
