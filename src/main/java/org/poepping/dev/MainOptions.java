package org.poepping.dev;

/**
 * Command line options
 *   --ui [text|swing|jetty]
 *   --headless : uses no UI, accepts no user input(?). required to be paired with other options (AI training?)
 */
public final class MainOptions {

  private static enum Option {
    UI("--ui",
        "To specify a different UI experience.",
        "text", 
        new String[]{"text"}, 
        true);

    final String commandLineOption;
    final String description;
    final String defaultValue;
    final String[] allowedValues;
    final boolean acceptsValue;

    private Option(
        String commandLineOption,
        String description,
        String defaultValue,
        String[] allowedValues,
        boolean acceptsValue) {
      this.commandLineOption = commandLineOption;
      this.description = description;
      this.defaultValue = defaultValue;
      this.allowedValues = allowedValues;
      this.acceptsValue = acceptsValue;
    }

    static Option forCommandLineOption(String commandLineOption) {
      for (Option option : values()) {
        if (option.commandLineOption.equals(commandLineOption)) {
          return option;
        }
      }
      throw new IllegalArgumentException("No such option with commandLineOption " + commandLineOption);
    }
  }

  public static MainOptions parseArgs(String[] args) {
    return new MainOptions();
  }

  public static String usage() {
    StringBuilder sb = new StringBuilder();
    sb.append("Usage: \n");
    for (Option o : Option.values()) {
      sb.append("\t");
      sb.append(o.commandLineOption);
      if (o.acceptsValue) {
        // do something
      }
    }
    return sb.toString();
  }
}
