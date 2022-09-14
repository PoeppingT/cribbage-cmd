package org.poepping.dev.event;

import org.poepping.dev.gamelogic.player.CribbagePlayer;

public class GameOverEvent extends Event {
  private CribbagePlayer winner;

  private GameOverEvent(Builder b) {
    this.winner = b.winner;
  }

  public CribbagePlayer winner() {
    return this.winner;
  }

  public static Builder builder() {
    return new Builder();
  }

  @Override
  public Type getType() {
    return Type.GAME_OVER;
  }

  public static class Builder {
    private CribbagePlayer winner;

    private Builder() {

    }

    public Builder winner(CribbagePlayer winner) {
      this.winner = winner;
      return this;
    }

    public GameOverEvent build() {
      if (winner == null) {
        throw new NullPointerException("CheckEvent cannot be built with a null player");
      }
      return new GameOverEvent(this);
    }
  }
}
