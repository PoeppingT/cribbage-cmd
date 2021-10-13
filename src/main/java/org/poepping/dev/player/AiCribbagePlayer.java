package org.poepping.dev.player;

import org.poepping.dev.cards.Card;
import org.poepping.dev.cards.Hand;
import org.poepping.dev.gamelogic.strategy.AiStrategy;

import java.util.Stack;

public class AiCribbagePlayer extends CribbagePlayer {

  private AiStrategy.Level aiLevel;

  public AiCribbagePlayer(String name) {
    super(name);
    this.setLevel(AiStrategy.DEFAULT_LEVEL);
  }

  @Override
  public Card[] chooseCardsToDiscardToCrib(int numberToDiscard) {
    Card[] discardedCards = new Card[numberToDiscard];
    Hand handCopy = Hand.copyOf(hand);
    for (int i = 0; i < numberToDiscard; i++) {
      Card discarded = aiLevel.strategy().chooseCardToDiscard(handCopy);
      discardedCards[i] = discarded;
      handCopy.remove(discarded);
    }
    return discardedCards;
  }

  @Override
  public Card chooseCardToPlay(Stack<Card> runningCards, int numberLeftTo31) {
    return aiLevel.strategy().chooseCardToPlay(hand, runningCards, numberLeftTo31);
  }

  @Override
  public void waitToContinue() {
    // do nothing, AI doesn't need to wait.
  }

  public void levelUp() {
    aiLevel = this.aiLevel.harder();
  }

  public void levelDown() {
    aiLevel = this.aiLevel.easier();
  }

  public void setLevel(AiStrategy.Level level) {
    this.aiLevel = level;
  }
}
