package org.poepping.dev.event;

import org.poepping.dev.cards.Card;
import org.poepping.dev.player.CribbagePlayer;

public class CardPlayEvent extends Event {
  private final CribbagePlayer player; // who played the card
  private final Card card; // what was played

  private CardPlayEvent(Builder b) {
    this.card = b.card;
    this.player = b.player;
  }

  public CribbagePlayer player() {
    return this.player;
  }

  public Card card() {
    return this.card;
  }

  public static Builder builder() {
    return new Builder();
  }

  @Override
  public Type getType() {
    return Type.CARD_PLAY;
  }

  public static class Builder {
    private CribbagePlayer player;
    private Card card;

    private Builder() {

    }

    public Builder card(Card card) {
      this.card = card;
      return this;
    }

    public Builder player(CribbagePlayer player) {
      this.player = player;
      return this;
    }

    public CardPlayEvent build() {
      if (card == null || player == null) {
        throw new NullPointerException("CardPlayEvent requires non-null card and player.");
      }
      return new CardPlayEvent(this);
    }
  }
}
