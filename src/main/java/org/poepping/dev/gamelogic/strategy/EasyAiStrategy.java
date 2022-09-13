package org.poepping.dev.gamelogic.strategy;

import org.poepping.dev.cards.Card;
import org.poepping.dev.cards.Hand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Stack;

/**
 * EasyAiStrategy represents just.. stupid decisions. Not purposefully bad, but just completely random.
 */
public class EasyAiStrategy extends AiStrategy {
  private static final Logger LOGGER = LoggerFactory.getLogger(EasyAiStrategy.class);

  @Override
  public Card chooseCardToDiscard(Hand hand) {
    LOGGER.debug("choosing a random card to discard");
    return hand.getRandom();
  }

  @Override
  public Card chooseCardToPlay(Hand hand, Stack<Card> runningCards, int runningCount) {
    int numberLeftTo31 = 31 - runningCount;
    for (int i = 0; i < hand.size(); i++) {
      Card chosenCard = hand.get(i);
      if (chosenCard.getValue().getValue() <= numberLeftTo31) {
        return chosenCard;
      }
    }
    return null;
  }
}
