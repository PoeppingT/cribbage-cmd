package org.poepping.dev.event;

import org.poepping.dev.player.CribbagePlayer;

public class CheckEvent extends Event {
  private CribbagePlayer player;

  private CheckEvent(Builder b) {
    this.player = b.player;
  }

  public CribbagePlayer player() {
    return this.player;
  }

  public static Builder builder() {
    return new Builder();
  }

  @Override
  public Type getType() {
    return Type.CHECK;
  }

  public static class Builder {
    private CribbagePlayer player;

    private Builder() {

    }

    public Builder player(CribbagePlayer player) {
      this.player = player;
      return this;
    }

    public CheckEvent build() {
      if (player == null) {
        throw new NullPointerException("CheckEvent cannot be built with a null player");
      }
      return new CheckEvent(this);
    }
  }
}
