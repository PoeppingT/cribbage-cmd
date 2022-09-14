package org.poepping.dev.event;

import org.poepping.dev.gamelogic.player.CribbagePlayer;

public class ScoreEvent extends Event {
  private final int score;
  private final String reason;
  private final CribbagePlayer player;

  ScoreEvent(Builder builder) {
    score = builder.score;
    reason = builder.reason;
    player = builder.player;
  }

  public int score() {
    return score;
  }

  public String reason() {
    return reason;
  }

  public CribbagePlayer player() {
    return player;
  }

  @Override
  public String toString() {
    return "ScoreEvent: " + score + " to: " + player + " for: " + reason;
  }

  public static Builder builder() {
    return new Builder();
  }

  @Override
  public Type getType() {
    return Type.SCORE;
  }

  public static class Builder {
    private int score = 0;
    private String reason = "";
    private CribbagePlayer player;

    public Builder() {

    }

    public Builder score(int score) {
      this.score = score;
      return this;
    }
    
    public int score() {
      return this.score;
    }

    public Builder addScore(int score) {
      this.score += score;
      return this;
    }

    public Builder reason(String reason) {
      this.reason = reason;
      return this;
    }

    public Builder addReason(String reason) {
      // this.reason = this.reason + System.lineSeparator() + reason;
      this.reason = this.reason + " " + reason;
      return this;
    }

    public Builder player(CribbagePlayer player) {
      this.player = player;
      return this;
    }

    public ScoreEvent build() {
      return new ScoreEvent(this);
    }
  }
}
