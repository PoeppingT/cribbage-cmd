package org.poepping.dev.player;

import org.poepping.dev.cards.Card;

public class HumanCribbagePlayer extends CribbagePlayer {

  public HumanCribbagePlayer(String name) {
    super(name);
  }

  @Override
  public Card[] chooseCardsToDiscardToCrib(int numberToDiscard) {
    return new Card[]{};
  }

  @Override
  public Card chooseCardToPlay() {
    System.out.println();
    return new Card(Card.Suit.SPADES, Card.Value.ACE);
  }
}
