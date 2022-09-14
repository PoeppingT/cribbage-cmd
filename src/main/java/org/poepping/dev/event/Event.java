package org.poepping.dev.event;

public abstract class Event {
  // doesn't do anything but indicate that it's an Event
  public enum Type {
    CARD_PLAY,
    CHECK,
    CUT,
    GAME_OVER,
    SCORE,
    HAND_SCORE;
  }

  public abstract Type getType();
}
