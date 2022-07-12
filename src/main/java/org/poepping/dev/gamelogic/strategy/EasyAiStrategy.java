package org.poepping.dev.gamelogic.strategy;

import org.poepping.dev.cards.Card;
import org.poepping.dev.cards.Hand;

import java.util.Stack;

/**
 * EasyAiStrategy represents just.. stupid decisions. Not purposefully bad, but just completely random.
 */
public class EasyAiStrategy extends AiStrategy {
  @Override
  public Card chooseCardToDiscard(Hand hand) {
    return hand.get((int)(Math.random() * (double)hand.size()));
  }

  @Override
  public Card chooseCardToPlay(Hand hand, Stack<Card> runningCards, int numberLeftTo31) {
    for (int i = 0; i < hand.size(); i++) {
      Card chosenCard = hand.get(i);
      if (chosenCard.getValue().getValue() <= numberLeftTo31) {
        return chosenCard;
      }
    }
    return null;
  }
}
