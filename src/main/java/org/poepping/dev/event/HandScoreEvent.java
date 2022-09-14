package org.poepping.dev.event;

import org.poepping.dev.cards.Hand;

public class HandScoreEvent extends ScoreEvent {

  private final Hand hand;

  private HandScoreEvent(Builder b) {
    super(b);
    this.hand = b.hand;
  }

  public Hand getHand() {
    return this.hand;
  }

  @Override
  public Type getType() {
    return Type.HAND_SCORE;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder extends ScoreEvent.Builder {

    private Hand hand;

    private Builder() {

    }

    public Builder hand(Hand hand) {
      this.hand = hand;
      return this;
    }

    public HandScoreEvent build() {
      super.build(); // will return, but since we're using it for validation just drop the ScoreEvent
      if (hand == null) {
        throw new NullPointerException("Cannot create a HandScoreEvent with a null hand.");
      }
      return new HandScoreEvent(this);
    }
  }
}
