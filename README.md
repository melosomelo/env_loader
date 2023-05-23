# Env loader

A simple `.env` loader library written in Java, made with the intent of applying/learning Design Patterns and
Test-Driven Development.
Project idea taken from [ProjectBook](https://projectbook.code.brettchalupa.com/_introduction.html).

## How to use

Using this library is very simple. The main class is `EnvLoader`, and to instantiate it, you need to choose
a `Parser`. The only parser available so far is the `EnvParser`, which parses `.env` files with the syntax
of one variable being set per line, but also allows empty lines and comments starting with a `#`.

```
DB_ROOT=localhost
DB_USER=mateus
DB_PASSWORD=mateusmelo123
```

```java

public class Main{
  public static void main(String[] args){
    EnvParser parser = new Parser();
    EnvLoader loader = new EnvLoader(parser);
    loader.load(); // Also accepts a path to the env file, but defaults to .env.

    // Different ways of getting the variable's values:
    String root = loader.get("DB_ROOT"); // localhost
    String username = System.getProperty("DB_USER"); // mateus
  }
}
```
