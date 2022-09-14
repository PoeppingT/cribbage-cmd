package org.poepping.dev.event;

import org.poepping.dev.cards.Card;

public class CutEvent extends Event {
  private final Card card; // what was cut?

  private CutEvent(Builder b) {
    this.card = b.card;
  }

  public Card card() {
    return this.card;
  }

  public static Builder builder() {
    return new Builder();
  }

  @Override
  public Type getType() {
    return Type.CUT;
  }

  public static class Builder {
    private Card card;

    private Builder() {

    }

    public Builder card(Card card) {
      this.card = card;
      return this;
    }

    public CutEvent build() {
      if (card == null) {
        throw new NullPointerException("Cannot create a CutEvent with a null card");
      }
      return new CutEvent(this);
    }
  }
}
