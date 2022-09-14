package org.poepping.dev.gamelogic.player.strategy;

import org.poepping.dev.cards.Card;
import org.poepping.dev.cards.Hand;

import java.util.Stack;

/*
 * Maximize hand points
 * but don't give away points (or prospective points, like 5s)
 * Discard to crib as a pair, not one at a time
 * Don't play into 31s
 * Don't play into runs (if you can't answer)
 * Sometimes go for a gut shot?
 * play into pairs if you can triple them
 */
public class HardAiStrategy extends AiStrategy {
  @Override
  public Card chooseCardToDiscard(Hand hand) {
    return null;
  }

  @Override
  public Card chooseCardToPlay(Hand hand, Stack<Card> runningCards, int runningCount) {
    return null;
  }
}
