package org.poepping.dev.player;

import org.poepping.dev.cards.Card;

public class AICribbagePlayer extends CribbagePlayer {

  public AICribbagePlayer(String name) {
    super(name);
  }

  @Override
  public Card[] chooseCardsToDiscardToCrib(int numberToDiscard) {
    return new Card[]{};
  }

  @Override
  public Card chooseCardToPlay() {
    return new Card(Card.Suit.HEARTS, Card.Value.TWO);
  }
}
