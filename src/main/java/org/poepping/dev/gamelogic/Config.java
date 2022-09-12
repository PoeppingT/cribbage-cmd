package org.poepping.dev.gamelogic;

public class Config {
  public int numberOfPlayers;
  public int humanPlayers;
  public int maxScore;
  public boolean aiCribFirst;

  private Config(Builder b) {
    this.numberOfPlayers = b.numberOfPlayers;
    this.humanPlayers = b.humanPlayers;
    this.maxScore = b.maxScore;
    this.aiCribFirst = b.aiCribFirst;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static Config defaultConfig() {
    return builder().build();
  }

  public static class Builder {
    private int numberOfPlayers = 2;
    private int humanPlayers = 1;
    private int maxScore = 121;
    private boolean aiCribFirst = true; // who goes first? what if no AIs?

    private Builder() {

    }

    public Builder numberOfPlayers(int numberOfPlayers) {
      this.numberOfPlayers = numberOfPlayers;
      return this;
    }

    public Builder humanPlayers(int humanPlayers) {
      this.humanPlayers = humanPlayers;
      return this;
    }

    public Builder maxScore(int maxScore) {
      this.maxScore = maxScore;
      return this;
    }

    public Config build() {
      return new Config(this);
    }
  }
}
