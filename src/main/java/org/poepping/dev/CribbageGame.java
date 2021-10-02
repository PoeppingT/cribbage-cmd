package org.poepping.dev;

public class CribbageGame implements Runnable {

  private CribbageGame() {

  }

  @Override
  public void run() {
    // do nothing for now
  }

  public static Builder builder() {
    return new Builder();
  }

  static class Builder {
    // ai level, score to win, display strategy,
  }
}
