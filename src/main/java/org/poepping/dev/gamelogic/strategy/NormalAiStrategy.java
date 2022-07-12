package org.poepping.dev.gamelogic.strategy;

import org.poepping.dev.cards.Card;
import org.poepping.dev.cards.Hand;
import org.poepping.dev.gamelogic.Scoring;

import java.util.Stack;

/**
 *
 * Follows a basic set of discarding rules:
 *  1. maximize existing points in hand
 * Follows a basic set of playing rules:
 *  1. play to 15 if you can
 *  2. play to 31 if you can
 *  3. play pairs/runs if you can
 */
public class NormalAiStrategy extends AiStrategy {
  @Override
  public Card chooseCardToDiscard(Hand hand) {
    // choose the discard that leaves the highest score
    Card bestDiscard = null;
    int bestScore = -1;
    for (int i = 0; i < hand.size(); i++) {
      Hand thisPlayHand = Hand.copyOf(hand);
      Card thisDiscard = thisPlayHand.get(i);
      int thisScore = Scoring.scoreHand(thisPlayHand.remove(thisDiscard), null);
      if (thisScore > bestScore) {
        bestScore = thisScore;
        bestDiscard = thisDiscard;
      }
    }
    return bestDiscard;
  }

  @Override
  public Card chooseCardToPlay(Hand hand, Stack<Card> runningCards, int numberLeftTo31) {
    return null;
  }
}
