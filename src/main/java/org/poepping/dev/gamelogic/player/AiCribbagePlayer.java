package org.poepping.dev.gamelogic.player;

import org.poepping.dev.cards.Card;
import org.poepping.dev.cards.Hand;
import org.poepping.dev.gamelogic.player.strategy.AiStrategy;
import org.poepping.dev.ui.CribbageUi;

public class AiCribbagePlayer extends CribbagePlayer {

  private AiStrategy.Level aiLevel;

  public AiCribbagePlayer(CribbageUi ui, String name) {
    super(ui, name);
    this.setLevel(AiStrategy.DEFAULT_LEVEL);
  }

  @Override
  public Card[] chooseCardsToDiscardToCrib() {
    final int numberToDiscard = hand.size() - 4;
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
  public Card chooseCardToPlay() {
    return aiLevel.strategy().chooseCardToPlay(hand, ui.getContext().cardsPlayed, ui.getContext().runningCount);
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
