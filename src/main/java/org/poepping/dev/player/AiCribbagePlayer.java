package org.poepping.dev.player;

import org.poepping.dev.cards.Card;

public class AiCribbagePlayer extends CribbagePlayer {

  public AiCribbagePlayer(String name) {
    super(name);
  }

  @Override
  public Card[] chooseCardsToDiscardToCrib(int numberToDiscard) {
    Card[] discardedCards = new Card[numberToDiscard];
    for (int i = 0; i < numberToDiscard; i++) {
      discardedCards[i] = hand.choose(i);
    }
    return discardedCards;
  }

  @Override
  public Card chooseCardToPlay(int numberLeftTo31) {
    for (Card card : hand) {
      if (card.getValue().getValue() <= numberLeftTo31) {
        return card;
      }
    }
    return null;
  }

  @Override
  public void waitToContinue() {
    // do nothing, AI doesn't need to wait.
  }
}
