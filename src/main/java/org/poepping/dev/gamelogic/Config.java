package org.poepping.dev.gamelogic;

import org.poepping.dev.gamelogic.player.strategy.AiStrategy;
import org.poepping.dev.ui.UiType;

public class Config {
  public final int numberOfPlayers;
  public final int humanPlayers;
  public final int maxScore;
  public final boolean aiCribFirst;
  public final AiStrategy.Level aiDifficulty;
  public final UiType uiType;

  private Config(Builder b) {
    this.numberOfPlayers = b.numberOfPlayers;
    this.humanPlayers = b.humanPlayers;
    this.maxScore = b.maxScore;
    this.aiCribFirst = b.aiCribFirst;
    this.aiDifficulty = b.aiDifficulty;
    this.uiType = UiType.TEXT;
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
    private AiStrategy.Level aiDifficulty = AiStrategy.DEFAULT_LEVEL;
    private UiType uiType = UiType.TEXT;

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

    public Builder aiDifficulty(AiStrategy.Level level) {
      this.aiDifficulty = level;
      return this;
    }

    public Builder uiType(UiType uiType) {
      this.uiType = uiType;
      return this;
    }

    public Config build() {
      return new Config(this);
    }
  }
}
